package com.vulner.bend_server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.bulk.BulkWriteResult;
import com.vulner.bend_server.bean.po.CnvdSharePo;
import com.vulner.bend_server.global.Page;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.DateFormat;
import com.vulner.common.utils.StringUtils;
import lombok.Data;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class LoopholeDBService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Criteria criterias(List lists) {
        Criteria[] arr = new Criteria[lists.size()];
        lists.toArray(arr);
        return new Criteria().andOperator(arr);
    }

    /**
     * 漏洞库查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    public Page<CnvdSharePo> search(Integer pageNum, Integer pageSize, String name) {
        Criteria criteriaQuery = null;
        if (StringUtils.isValid(name)){
            Criteria c1 = Criteria.where("title").regex(".*?" + name + ".*?");
            Criteria c2 = Criteria.where("number").regex(".*?" + name + ".*?");
            Criteria cr = new Criteria();
            criteriaQuery = cr.orOperator(c1, c2);
        }

        Query query = null;
        if (criteriaQuery != null) {
            query = new Query(criteriaQuery);
        } else {
            query = new Query();
        }

        Long totalCount = mongoTemplate.count(query, CnvdSharePo.class); //总数

        Page<CnvdSharePo> page = null;
        if (pageNum == null || pageSize == null ){
            pageSize = (pageSize == null) ? 10 : totalCount.intValue();
            page = new Page<>(1, pageSize);
        } else {
            int offset = (pageNum-1) * pageSize;
            query.skip(offset).limit(pageSize); // 分页

            page = new Page<>(pageNum, pageSize);
        }

        query.with(Sort.by(Sort.Order.desc("_id")));  //排序

        List<CnvdSharePo> lists = mongoTemplate.find(query, CnvdSharePo.class);

        page.setData(lists);
        page.setTotalResults(totalCount);
        return page;
    }

    /**
     * 漏洞库新增
     * @param params
     * @return
     */
    public Object addVul(Map<String, Object> params) {

        mapToData(params);
        params.put("submitTime", DateFormat.getCurrentDateStr());

        Map<String, Object> exploitInfoMap = mongoTemplate.insert(params, "cnvd_share");

        return ResponseHelper.success(exploitInfoMap);
    }

    public void mapToData (Map<String, Object> params) {
        String cves = "" + params.get("cves");
        if (StringUtils.isValid(cves)) {
            String[] cveArry = cves.split(",");
            List<Map<String, String>> cveList = new ArrayList<>();

            for (String cve : cveArry) {
                Map<String, String> mp = new HashMap<>();
                mp.put("cveNumber", cve);
                cveList.add(mp);
            }
            params.put("cves", cveList);
        }

        String bids = "" + params.get("bids");
        if (StringUtils.isValid(bids)) {
            String[] bidArry = bids.split(",");
            List<Map<String, String>> bidList = new ArrayList<>();

            for (String bid : bidArry) {
                Map<String, String> mp = new HashMap<>();
                mp.put("bidNumber", bid);
                bidList.add(mp);
            }
            params.put("bids", bidList);
        }

        String products = "" + params.get("products");
        if (StringUtils.isValid(products)) {
            String[] productArry = products.split(",");
            List<Map<String, String>> productList = new ArrayList<>();

            for (String product_info : productArry) {
                String[] product = product_info.split(":");

                Map<String, String> mp = new HashMap<>();
                mp.put("product", product[0]);
                if (product.length > 1)
                    mp.put("version", product[1]);
                productList.add(mp);
            }
            params.put("products", productList);
        }
    }

    /**
     * 漏洞库数据修改
     * @param params
     * @return
     */
    public Object modifyVul(Map<String, Object> params) {
        Criteria criterias = Criteria.where("_id").is(params.get("id"));
        Query query = new Query(criterias);

        Update update = new Update();

        mapToData(params);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        //执行
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "cnvd_share");
        BulkWriteResult result = bulkOperations.upsert(query, update).execute();

        return ResponseHelper.success(result);
    }

    /**
     * 漏洞库数据删除
     * @param vulId
     * @return
     */
    public Boolean delVul(String vulId) {
        if (!StringUtils.isValid(vulId)){
            return null;
        }
        Criteria criterias = Criteria.where("_id").is(vulId);
        Query query = new Query(criterias);

        mongoTemplate.remove(query, CnvdSharePo.class);

        return true;
    }

    /**
     * 年份统计查询
     * @return
     */
    public Object statisticsYear() {
        //封装查询条件
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.project("$openTime").andExpression("{ $substr: {'$openTime', 0, 4}}").as("year"));
        operations.add(Aggregation.group("year").count().as("total"));
        operations.add(Aggregation.sort(Sort.Direction.ASC,"_id"));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        AggregationResults<Document> statisticsYearData = mongoTemplate.aggregate(aggregation, "cnvd_share", Document.class);
        Document rawResults = statisticsYearData.getRawResults();
        Object results = rawResults.get("results");

        return ResponseHelper.success(results);
    }

    /**
     * 厂商统计
     * @return
     */
    public Object statisticsDiscoverer() {
        //封装查询条件
        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(Aggregation.group("discovererName").count().as("total"));
        operations.add(Aggregation.sort(Sort.Direction.DESC,"total"));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        AggregationResults<Document> statisticsYearData = mongoTemplate.aggregate(aggregation, "cnvd_share", Document.class);
        Document rawResults = statisticsYearData.getRawResults();

        ArrayList results = (ArrayList) rawResults.get("results");

        List<JSONObject> retMp = new ArrayList<>();
        JSONObject otherData = new JSONObject();
        int otherTotal = 0;
        for (int i = 0; i < results.size(); i++) {
            JSONObject jsonObj = (JSONObject) JSON.toJSON(results.get(i));
            if (i > 10) {
                String total = "" + jsonObj.get("total");
                otherTotal += Integer.parseInt(total);
            } else {
                retMp.add(jsonObj);
            }

        }
        otherData.put("_id", "其他");
        otherData.put("total", otherTotal);

        retMp.add(otherData);

        return ResponseHelper.success(retMp);
    }

    /**
     * 等级统计
     * @return
     */
    public Object statisticsLevel() {
        //封装查询条件
        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(Aggregation.group("serverity").count().as("total"));
        operations.add(Aggregation.sort(Sort.Direction.ASC,"total"));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        AggregationResults<Document> statisticsYearData = mongoTemplate.aggregate(aggregation, "cnvd_share", Document.class);
        Document rawResults = statisticsYearData.getRawResults();
        Object results = rawResults.get("results");

        return ResponseHelper.success(results);
    }

    /**
     * 主要厂商公开漏洞趋势
     * @return
     */
    public Object statisticsTrend() {
        String discovererNames = "Siemens(西门子),Schneider(施耐德),Advantech,SAP,HP";

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int endYear = c.get(Calendar.YEAR);
        int startYear = endYear - 10; // 展示近10年漏洞数量

        Map<String, Map<String, String>> retMp = new HashMap<>();
        setMap(retMp, discovererNames, endYear, startYear);

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.project("$openTime", "$discovererName").andExpression("{ $substr: {'$openTime', 0, 4}}").as("year"));
        operations.add(Aggregation.group("year", "discovererName").count().as("total"));
//        operations.add(Aggregation.sort(Sort.Direction.DESC,"total"));
//        operations.add(Aggregation.match(Criteria.where("year").gte("2010").lte("2020")));

        Aggregation aggregation = Aggregation.newAggregation(operations);

        AggregationResults<Document> statisticsYearData = mongoTemplate.aggregate(aggregation, "cnvd_share", Document.class);
        Document rawResults = statisticsYearData.getRawResults();
        ArrayList results = (ArrayList) rawResults.get("results");

        for (int i = 0; i < results.size(); i++) {
            JSONObject jsonObj = (JSONObject) JSON.toJSON(results.get(i));

            Object id = jsonObj.get("_id");
            JSONObject keys = (JSONObject) JSON.toJSON(jsonObj.get("_id"));
            String year = "" + keys.get("year");
            String disName = "" + keys.get("discovererName");
            String total = "" + jsonObj.get("total");

            if (StringUtils.isValid(disName) && StringUtils.isValid(year) && discovererNames.indexOf(disName) > -1) {
                Map<String, String> disMap = retMp.get(disName);
                if (disMap != null && Integer.parseInt(year) >= startYear)
                    disMap.put(year, total);
            }
        }

        return ResponseHelper.success(retMp);
    }

    public void setMap (Map<String, Map<String, String>> retMp, String discovererNames, int endYear, int startYear) {
        String[] disNames = discovererNames.split(",");

        Map<String, String> totalMp = new HashMap<>();
        for (int i = startYear ; i <= endYear; i++) {
            totalMp.put(i + "", "0");
        }

        for (String disName : disNames) {
            retMp.put(disName, totalMp);
        }
    }

}

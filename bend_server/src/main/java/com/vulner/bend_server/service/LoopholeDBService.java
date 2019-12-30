package com.vulner.bend_server.service;

import com.mongodb.bulk.BulkWriteResult;
import com.vulner.bend_server.bean.po.ExploitInfoTinyPo;
import com.vulner.bend_server.global.Page;
import com.vulner.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    public Page<ExploitInfoTinyPo> search(Integer pageNum, Integer pageSize, String name) {
        Criteria c1 = null;
        ArrayList<Criteria> list = new ArrayList<>();
        if (StringUtils.isValid(name)){
            c1 = Criteria.where("name").regex(".*?" + name + ".*?");
            list.add(c1);
        }

        Query query = null;
        if (0 < list.size()) {
            query = new Query(criterias(list));
        } else {
            query = new Query();
        }

        long count = mongoTemplate.count(query, ExploitInfoTinyPo.class); //总数

        Page<ExploitInfoTinyPo> page = null;
        if (pageNum == null || pageSize == null ){
            page = new Page<>(1, (int) count);
        } else {
            int pageTotal = (int) (count%pageSize==0?count/pageSize:count/pageSize+1); //总页数
            int offset=(pageNum-1)*pageSize;
            query.skip(offset).limit(pageSize); // 分页

            page = new Page<>(pageNum, pageSize);
            page.setTotalResults(pageTotal);  // 总页数
        }

        query.with(Sort.by(Sort.Order.desc("_id")));  //排序

        List<ExploitInfoTinyPo> lists = mongoTemplate.find(query, ExploitInfoTinyPo.class);

        page.setData(lists);
        return page;
    }

    /**
     * 漏洞库新增
     * @param exploitInfo
     * @return
     */
    public Boolean addVul(ExploitInfoTinyPo exploitInfo) {
        ExploitInfoTinyPo insert = mongoTemplate.insert(exploitInfo);

        return true;
    }

    /**
     * 漏洞库数据修改
     * @return
     */
    public Boolean modifyVul(Map<String, String> mp) {
        Criteria criterias = Criteria.where("_id").is(mp.get("id"));
        Query query = new Query(criterias);

        Update update = new Update();

        for (Map.Entry<String, String> entry : mp.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        //执行
        BulkOperations bulkOperations=mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "exploit_info_tiny");
//        BulkWriteResult result = bulkOperations.updateOne(query, update).execute();
        BulkWriteResult result = bulkOperations.upsert(query, update).execute();
        return true;
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

        mongoTemplate.remove(query, ExploitInfoTinyPo.class);

        return true;
    }


}

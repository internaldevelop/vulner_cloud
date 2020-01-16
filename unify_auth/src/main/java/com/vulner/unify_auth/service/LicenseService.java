package com.vulner.unify_auth.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.vulner.common.bean.dto.ExcelDataDto;
import com.vulner.common.utils.*;
import com.vulner.unify_auth.bean.po.LicenseDataPo;
import com.vulner.unify_auth.bean.po.LicenseExpirePo;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.dao.LicenseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class LicenseService {

    @Autowired
    private LicenseDao licenseDao;
    @Autowired
    private AccountsDao accountsDao;

    /**
     * 生成License
     * @param response
     * @param user
     * @param userUuid
     * @param expireTime
     */
    public void createLicense(HttpServletResponse response, Principal user, String userUuid, String expireTime) {

        String issuerName = user.getName(); // 发行用户名
        String licenseUuid = StringUtils.generateUuid(); // 授权码uuid

        String issuerUuid = accountsDao.getAccountUuidByName(issuerName);
        String userName = accountsDao.getAccountNameByUuid(userUuid);

        if (StringUtils.isValid(issuerUuid)) {

            Map<String, String> mp = new HashMap<>();
            mp.put("license_uuid", licenseUuid);   // licenseUuid
            mp.put("issuer_uuid", issuerUuid);  // 发行用户uuid
            mp.put("issuer_name", issuerName);  // 发行用户名
            mp.put("expire_time",expireTime);  // 到期时间
            mp.put("user_uuid",userUuid);  // 被授权用户uuid
            mp.put("user_name",userName);  // 被授权用户名

            LicenseDataPo po = new LicenseDataPo();
            po.setUuid(licenseUuid);
            po.setIssuer_name(issuerName);
            po.setCreate_time(TimeUtils.getCurrentDate());
            po.setIssuer_uuid(issuerUuid);

            String strMap = JSONObject.toJSONString(mp);
            
            try {
                String strEncrypt = AesUtils.encryptAES(strMap);  // 加密串
                System.out.println("===================================");
                System.out.println(strEncrypt);
                System.out.println("===================================");

                po.setLicense_data(strEncrypt);
                licenseDao.createLicense(po);  // 存库

                ExcelDataDto dto = new ExcelDataDto();
                dto.setContent(strEncrypt);
                String seq = DateFormat.dateToString(new Date(), DateFormat.PAT_LOCAL_PAT_ID_TIME_FORMAT);
                FileUtil.exportFile(response, "license" + seq, dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 导入License
     * @param user
     * @param file
     */
    public void importLicense(Principal user, MultipartFile file) {
        Date now = new Date();
        Reader reader = null;
        try {
            reader = new InputStreamReader(file.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(reader);
            String line;
            StringBuffer sb = new StringBuffer();

            //读取文件
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            String fileContent = "" + sb;  // 文件内容

            String strDecrypt = AesUtils.decryptAES( fileContent);  // 解密串
            if (StringUtils.isValid(strDecrypt)){
                Map<String, String> mp = JSONObject.parseObject(strDecrypt, new TypeReference<Map<String, String>>(){});
                String liUuid = mp.get("license_uuid");
                LicenseDataPo liPo = licenseDao.getInfoByUuid(liUuid, 1);
                if (liPo != null){
                    String liData = liPo.getLicense_data();
                    if (fileContent.equals(liData)){
                        LicenseExpirePo liExpPo = new LicenseExpirePo();
                        liExpPo.setUuid(StringUtils.generateUuid());
                        liExpPo.setIssuer_uuid(mp.get("issuer_uuid"));  // 发行用户uuid
                        liExpPo.setIssuer_name(mp.get("issuer_name"));  // 发行用户名
                        liExpPo.setUser_uuid(mp.get("user_uuid"));  // 被授权用户uuid
                        liExpPo.setUser_name(mp.get("user_name"));  // 被授权用户名
                        liExpPo.setExpire_time(DateFormat.stringToDate(mp.get("expire_time"), DateFormat.SQL_FORMAT));
                        liExpPo.setCreate_time(now);
                        licenseDao.addLicenseExpire(liExpPo);

                        liPo.setUse_flag(0);
                        liPo.setUse_time(now);
                        licenseDao.updLicense(liPo);  // 更新License使用状态
                    }
                } else {
                    System.out.println("无效的License......");
                }
            } else {
                System.out.println("无效的License......");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

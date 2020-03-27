package com.vulner.unify_auth.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.vulner.common.bean.dto.ExcelDataDto;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.*;
import com.vulner.unify_auth.bean.po.AccountRolePo;
import com.vulner.unify_auth.bean.po.LicenseDataPo;
import com.vulner.unify_auth.bean.po.LicenseExpirePo;
import com.vulner.unify_auth.dao.AccountRolesDao;
import com.vulner.unify_auth.dao.AccountsDao;
import com.vulner.unify_auth.dao.LicenseDao;
import com.vulner.unify_auth.dao.RolesDao;
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
    @Autowired
    private AccountRolesDao accountRolesDao;

    /**
     * 生成License
     * @param response
     * @param user
     * @param accountUuid
     * @param expireTime
     */
    public void createLicense(HttpServletResponse response, Principal user, String accountUuid, String expireTime, String sign, String roleUuids) {

        String issuerName = user.getName(); // 发行用户名
        String licenseUuid = StringUtils.generateUuid(); // 授权码uuid

        String issuerUuid = accountsDao.getAccountUuidByName(issuerName);

        String accountName = "";
        if(StringUtils.isValid(accountUuid)){
            accountName = accountsDao.getAccountNameByUuid(accountUuid);
        }

        if (StringUtils.isValid(issuerUuid)) {

            Map<String, String> mp = new HashMap<>();
            mp.put("license_uuid", licenseUuid);   // licenseUuid
            mp.put("issuer_uuid", issuerUuid);  // 发行用户uuid
            mp.put("issuer_name", issuerName);  // 发行用户名
            mp.put("expire_time",expireTime);  // 到期时间
            if(StringUtils.isValid(accountUuid)){
                mp.put("account_uuid",accountUuid);  // 被授权用户uuid
                mp.put("account_name",accountName);  // 被授权用户名
            }

            if (StringUtils.isValid(roleUuids) && "1".equals(sign)){  // 标识 0:续期; 1:角色授权
                mp.put("sign", "1");
                mp.put("role_uuids", roleUuids);
            } else {
                mp.put("sign", "0");
            }

            LicenseDataPo po = new LicenseDataPo();
            po.setUuid(licenseUuid);
            po.setIssuer_name(issuerName);
            po.setCreate_time(TimeUtils.getCurrentDate());
            po.setIssuer_uuid(issuerUuid);

            String strMap = JSONObject.toJSONString(mp);
            
            try {
                System.out.println(strMap);
                String strEncrypt = AesUtils.encryptAES(strMap);  // 加密串

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
    public Object importLicense(Principal user, MultipartFile file) {
        Date now = new Date();
        Reader reader = null;
        Map<String, String> data = new HashMap<>();
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
                        String accountUuid = mp.get("account_uuid");

                        String accountName = user.getName();
                        data.put("account_uuid", accountName);
                        if (StringUtils.isValid(accountUuid)){
                            liExpPo.setAccount_uuid(accountUuid);  // 被授权用户uuid
                        } else {
                            accountUuid = accountsDao.getAccountUuidByName(accountName);
                            liExpPo.setAccount_uuid(accountUuid);
                        }
                        liExpPo.setAccount_name(accountName);  // 被授权用户名
                        String expireTime = mp.get("expire_time");
                        data.put("expire_time", expireTime);

                        liExpPo.setExpire_time(DateFormat.stringToDate(expireTime, DateFormat.SQL_FORMAT));
                        liExpPo.setCreate_time(now);
                        licenseDao.addLicenseExpire(liExpPo);

                        String sign = mp.get("sign");
                        if (StringUtils.isValid(sign) && "1".equals(sign) && StringUtils.isValid(accountUuid)){  // 标识 0:续期; 1:角色授权
                            String roleUuids = mp.get("role_uuids");
                            if (StringUtils.isValid(roleUuids)){
                                String[] rUuids = roleUuids.split(",");
                                accountRolesDao.deleteAllMapsByAccountUuid(accountUuid);  // 删除原有角色
                                for(String rUuid : rUuids){
                                    AccountRolePo aRolePo = new AccountRolePo();
                                    aRolePo.setUuid(StringUtils.generateUuid());
                                    aRolePo.setAccount_uuid(accountUuid);
                                    aRolePo.setRole_uuid(rUuid);
                                    aRolePo.setCreate_time(now);
                                    accountRolesDao.addAccountRoleMap(aRolePo);
                                }
                            }
                        }

                        liPo.setUse_flag(0);
                        liPo.setUse_time(now);
                        licenseDao.updLicense(liPo);  // 更新License使用状态
                        data.put("role_names", accountRolesDao.getAccountRoleName(accountUuid));
                    }
                } else {
                    return ResponseHelper.error("ERROR_LICENSE_INVALID");
                }
            } else {
                return ResponseHelper.error("ERROR_LICENSE_INVALID");
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

        return ResponseHelper.success(data);
    }

}

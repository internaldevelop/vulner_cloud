package com.vulner.bend_server.service;

import com.alibaba.fastjson.JSONObject;
import com.vulner.bend_server.bean.po.AssetAuthenticatePo;
import com.vulner.bend_server.dao.AuthenticateMapper;
import com.vulner.bend_server.global.RestTemplateUtil;
import com.vulner.bend_server.global.algorithm.AESEncrypt;
import com.vulner.bend_server.global.algorithm.RSAEncrypt;
import com.vulner.bend_server.global.algorithm.SHAEncrypt;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticateService {

    @Autowired
    AuthenticateMapper authenticateMapper;

    /**
     * 获取设备指纹生成对称秘钥 sym_key
     * @param assetUuid
     * @return
     */
    public Object getFingerprintToSymKey(String assetUuid) {

        String assetIp = "localhost";
        // 构造URL
        String url = "http://" + assetIp + ":8191/asset-info/get-fingerprint";

        // 构造参数mp
        HashMap<String, String> mp = new HashMap<>();

        if (!this.uptData(assetUuid, url, mp))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        return ResponseHelper.success();
    }

    /**
     * 获取公钥
     * @param assetUuid
     * @return
     */
    public Object getPublicKey(String assetUuid) {
        String assetIp = "localhost";
        // 构造URL
        String url = "http://" + assetIp + ":8191/asset-info/get-public-key";
        // 构造参数mp
        HashMap<String, String> mp = new HashMap<>();

        if (!this.uptData(assetUuid, url, mp))
            return ResponseHelper.error("ERROR_GENERAL_ERROR");

        return ResponseHelper.success();
    }

    /**
     * 更新保存
     * @param assetUuid
     * @param url
     * @param mp
     * @return
     */
    public boolean uptData(String assetUuid, String url, Map<String, String> mp){
        boolean retFlag = false;

        Timestamp now = TimeUtils.getCurrentSystemTimestamp();
        try {
            String symKey = "";  // 对称秘钥
            String fingerprintStr = "";  // 设备指纹
            String publicKey = ""; // 公钥

            ResponseBean respBean = RestTemplateUtil.reqData(url, mp);
            Object payload = respBean.getPayload();
            if (payload != null){
                JSONObject jsonObj = (JSONObject) JSONObject.toJSON(payload);
                Object fingerprint = jsonObj.get("device_fingerprint");
                if (fingerprint != null){
                    fingerprintStr = fingerprint.toString();
                    symKey = SHAEncrypt.SHA256(fingerprintStr);
                }
                Object pKey = jsonObj.get("public_key");
                if (pKey != null)
                    publicKey = pKey.toString();
            }

            boolean flag = true;  // true:更新; false:新增
            AssetAuthenticatePo aaPo = authenticateMapper.getAuthenticate(assetUuid);
            if (aaPo == null) {
                flag = false;
                aaPo = new AssetAuthenticatePo();
                aaPo.setUuid(StringUtils.generateUuid());
                aaPo.setCreate_time(now);
            }
            aaPo.setUpdate_time(now);
            aaPo.setAsset_uuid(assetUuid);
            if(StringUtils.isValid(fingerprintStr))
                aaPo.setDev_fingerprint(fingerprintStr);  // 设备指纹
            if (StringUtils.isValid(symKey))
                aaPo.setSym_key(symKey);  // 对称秘钥
            if (StringUtils.isValid(publicKey))
                aaPo.setPublic_key(publicKey);  // 公钥

            if (flag){
                authenticateMapper.updAuthenticate(aaPo);
            } else {
                authenticateMapper.addAuthenticate(aaPo);
            }
            retFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retFlag;
    }

    /**
     * 认证
     * @param assetUuid
     * @return
     */
    public Object authenticate(String assetUuid) {
        String errorCode = "ERROR_AUTHENTICATE_FAIL";

        String assetIp = "localhost";
        // 构造URL
        String url = "http://" + assetIp + ":8191/asset-info/authenticate";
        // 构造参数mp
        HashMap<String, String> mp = new HashMap<>();

        AssetAuthenticatePo aaPo = authenticateMapper.getAuthenticate(assetUuid);
        if (aaPo == null)
            return ResponseHelper.error(errorCode);

        try {
            ResponseBean respBean = RestTemplateUtil.reqData(url, mp);
            Object payload = respBean.getPayload();
            JSONObject jsonObj = (JSONObject) JSONObject.toJSON(payload);
            Object orgData = jsonObj.get("org_data");
            Object sign = jsonObj.get("sign");
            String publicKey = aaPo.getPublic_key();

            if (orgData != null && sign != null && StringUtils.isValid(publicKey)) {
                String oData = orgData.toString();
                boolean checkSign = RSAEncrypt.verifySignature(oData, sign.toString(), new BASE64Decoder().decodeBuffer(publicKey));
                if (checkSign) {
                    String decrypt = AESEncrypt.decrypt(oData, aaPo.getSym_key());

                    if (StringUtils.isValid(decrypt) && decrypt.indexOf("success") > -1){
                        aaPo.setAuthenticate_flag(1);
                        errorCode = "ERROR_AUTHENTICATE_SUCCESS";
                    } else {
                        aaPo.setAuthenticate_flag(-1);
                    }
                    aaPo.setUpdate_time(TimeUtils.getCurrentSystemTimestamp());
                    authenticateMapper.updAuthenticate(aaPo);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHelper.error(errorCode);
    }
}

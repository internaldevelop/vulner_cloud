package com.vulner.embed_terminal.service;

import com.alibaba.fastjson.JSONObject;
import com.vulner.common.utils.DateFormat;
import com.vulner.embed_terminal.bean.po.AssetAuthenticatePo;
import com.vulner.embed_terminal.bean.po.AssetsPo;
import com.vulner.embed_terminal.dao.AssetsMapper;
import com.vulner.embed_terminal.dao.AuthenticateMapper;
import com.vulner.embed_terminal.global.RestTemplateUtil;
import com.vulner.embed_terminal.global.algorithm.AESEncrypt;
import com.vulner.embed_terminal.global.algorithm.RSAEncrypt;
import com.vulner.embed_terminal.global.algorithm.SHAEncrypt;
import com.vulner.embed_terminal.global.websocket.SockMsgTypeEnum;
import com.vulner.embed_terminal.global.websocket.WebSocketServer;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class AuthenticateService {

    @Autowired
    AuthenticateMapper authenticateMapper;

    @Autowired
    AssetsMapper assetsMapper;

    private String SYSTYPE;  // 系统类型
    private String SYSVERSION;  // 系统版本
    private String SYSNAME;  // 系统名称

    /**
     * 扫描设备 获取设备指纹
     * @param startIp
     * @param endIp
     * @return
     */
    public Object scanGetEquipment(String startIp, String endIp) {
        if (StringUtils.isValid(startIp) && StringUtils.isValid(endIp)) {

            int startInt = getLastNum(startIp);
            int endInt = getLastNum(endIp);

            String frontIp = startIp.substring(0, startIp.lastIndexOf(".") + 1);  // ip前半部分 示例192.168.1.

            asynScanDevice(frontIp, startInt, endInt);

        }
        return ResponseHelper.success();
    }

    /**
     * 获取设备指纹生成对称秘钥 sym_key
     * @param assetUuid
     * @return
     */
    public Object getFingerprintToSymKey(String assetUuid) {
        AssetsPo AssetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (AssetsPo == null || !StringUtils.isValid(AssetsPo.getIp())) {
            ResponseHelper.error("ERROR_GENERAL_ERROR");
        }

        if (getFingerprintBoolen(assetUuid, AssetsPo.getIp()))
            return ResponseHelper.success();
        return ResponseHelper.error("ERROR_GENERAL_ERROR");
    }

    /**
     * 获取指纹
     * @param assetUuid
     * @param assetIp
     * @return
     */
    public boolean getFingerprintBoolen (String assetUuid, String assetIp) {

        // 构造URL
        String url = "http://" + assetIp + ":8191/authenticate/get-fingerprint";

        if (this.uptData(assetUuid, url)){  // 更新保存数据
            this.sendOutSymKey(assetIp, assetUuid);  // 返回sym_key  TODO
            return true;
        }
        return false;
    }


    /**
     * 审核
     * @param assetUuid
     * @param classify 1:白名单; -1:黑名单
     * @return
     */
    public Object toReview(String assetUuid, int classify) {
        // 1、返回sym_key  2、获取公钥  3、保存公钥修并改授权状态
        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp())) {
            return ResponseHelper.error("ERROR_GENERAL_ERROR");
        }
        String assetIp = assetsPo.getIp();
        // 构造URL
        String url = "http://" + assetIp + ":8191/authenticate/get-public-key?sym_key={sym_key}";

        if (!this.uptData(assetUuid, url)) {
            return ResponseHelper.error("ERROR_GENERAL_ERROR");
        }

        Timestamp now = TimeUtils.getCurrentSystemTimestamp();
        assetsPo.setClassify(classify);
        assetsPo.setUpdate_time(now);
        if (classify == 1) {  // 审核通过
            Date date = DateFormat.addMonth(now, 12);  // 有效期一年
            assetsPo.setExpire_time(new Timestamp(date.getTime()));
        }

        assetsMapper.updAssets(assetsPo);

        return ResponseHelper.success();
    }

    public int getLastNum(String ip) {
        int retNum = 0;
        try {
            String lastNum = ip.substring(ip.lastIndexOf("."));
            if (StringUtils.isValid(lastNum))
                retNum = Integer.parseInt(lastNum.replace(".", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retNum;
    }



    /**
     * 异步扫描设备
     * @param frontIp
     * @param startInt
     * @param endInt
     * @return
     */
    public boolean asynScanDevice (String frontIp, int startInt, int endInt) {
        Timestamp now = TimeUtils.getCurrentSystemTimestamp();

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Boolean> future = executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call(){
                try {
                    for (int laNum = startInt; laNum <= endInt; laNum++) {
                        Double percent = 100D * (laNum - startInt + 1) / (endInt - startInt + 1);
                        System.out.println("进度：" + String.format("%.2f", percent));

                        JSONObject jsonMsg = new JSONObject();
                        jsonMsg.put("scan_percent", String.format("%.2f", percent));
                        jsonMsg.put("user_uuid", "user_uuid");  // TODO
                        WebSocketServer.broadcastAssetInfo(SockMsgTypeEnum.SCAN_ASSET_INFO, jsonMsg);

                        String assetIp = frontIp + laNum;
                        if(verifyIp(assetIp)){  // 网络通畅为true
                            AssetsPo assetsPo = assetsMapper.getAssetsByIp(assetIp);
                            if (assetsPo == null) {
                                String assetUuid = StringUtils.generateUuid();
                                assetsPo = new AssetsPo();

                                assetsPo.setIp(assetIp);
                                assetsPo.setUuid(assetUuid);
                                assetsPo.setClassify(0);
                                assetsPo.setCreate_time(now);

                                if (getFingerprintBoolen(assetUuid, assetIp)) {
                                    String sysType = SYSTYPE.toLowerCase();
                                    assetsPo.setOs_type(sysType.indexOf("windows") > -1 ? "1" : "-1");
                                    assetsPo.setOs_ver(SYSVERSION);
                                    assetsPo.setName(SYSNAME);

                                    assetsMapper.addAssets(assetsPo);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        return true;
    }

    /**
     * 验证ip是否通畅
     * @param ipAddr
     * @return
     */
    public static boolean verifyIp (String ipAddr) {
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(ipAddr);
            reachable = address.isReachable(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ipAddr + "=" + reachable);
        return reachable;
    }

    /**
     * 回调agent返回sym_key
     * @param assetIp
     * @param assetUuid
     */
    public void sendOutSymKey(String assetIp, String assetUuid) {
        String url = "http://" + assetIp + ":8191/authenticate/save-sym-key?sym_key={sym_key}";
        AssetAuthenticatePo aaPo = authenticateMapper.getAuthenticate(assetUuid);
        if (aaPo != null) {
            // 构造参数mp
            HashMap<String, String> mp = new HashMap<>();
            mp.put("sym_key", aaPo.getSym_key());
            RestTemplateUtil.reqData(url, mp);
        }

    }

    /**
     * 获取公钥
     * @param assetUuid
     * @return
     */
    public Object getPublicKey(String assetUuid) {
        AssetsPo assetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (assetsPo == null || !StringUtils.isValid(assetsPo.getIp())) {
            ResponseHelper.error("ERROR_GENERAL_ERROR");
        }
        String assetIp = assetsPo.getIp();
        // 构造URL
        String url = "http://" + assetIp + ":8191/authenticate/get-public-key?sym_key={sym_key}";

        if (!this.uptData(assetUuid, url)) {
            return ResponseHelper.error("ERROR_GENERAL_ERROR");
        }

        return ResponseHelper.success();
    }

    /**
     * 更新保存
     * @param assetUuid
     * @param url
     * @return
     */
    public boolean uptData(String assetUuid, String url){
        boolean retFlag = false;

        Timestamp now = TimeUtils.getCurrentSystemTimestamp();
        try {
            String symKey = "";  // 对称秘钥
            String fingerprintStr = "";  // 设备指纹
            String publicKey = ""; // 公钥

            AssetAuthenticatePo aaPo = authenticateMapper.getAuthenticate(assetUuid);
            // 构造参数mp
            HashMap<String, String> mp = new HashMap<>();
            if (aaPo != null && StringUtils.isValid(aaPo.getSym_key())) {
                mp.put("sym_key", aaPo.getSym_key());
            }

            ResponseBean respBean = RestTemplateUtil.reqData(url, mp);
            if (respBean == null)
                return retFlag;
            Object payload = respBean.getPayload();
            if (payload != null){
                JSONObject jsonObj = (JSONObject) JSONObject.toJSON(payload);
//                Object fingerprint = jsonObj.get("device_fingerprint");
//                if (fingerprint != null){
//                    fingerprintStr = fingerprint.toString();
//                    symKey = SHAEncrypt.SHA256(fingerprintStr);
//                }

                if (url.indexOf("get-fingerprint") > 0) {
                    fingerprintStr = payload.toString();
                    symKey = SHAEncrypt.SHA256(fingerprintStr);
                }

                Object pKey = jsonObj.get("public_key");
                if (pKey != null)
                    publicKey = pKey.toString();
                Object sysType = jsonObj.get("sys_type");
                if (sysType != null)
                    SYSTYPE = sysType.toString();
                Object sysVersion = jsonObj.get("sys_version");
                if (sysVersion != null)
                    SYSVERSION = sysVersion.toString();
                Object sysName = jsonObj.get("sys_name");
                if (sysName != null)
                    SYSNAME = sysName.toString();

            }

            boolean flag = true;  // true:更新; false:新增
            if (aaPo == null) {
                flag = false;
                aaPo = new AssetAuthenticatePo();
                aaPo.setUuid(StringUtils.generateUuid());
                aaPo.setCreate_time(now);
            }
            aaPo.setUpdate_time(now);
            aaPo.setAsset_uuid(assetUuid);
            aaPo.setAuthenticate_flag(0);  //   // 0未认证; -1:失败；1:成功
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

        AssetsPo AssetsPo = assetsMapper.getAssetsByUuid(assetUuid);
        if (AssetsPo == null || !StringUtils.isValid(AssetsPo.getIp())) {
            return ResponseHelper.error("ERROR_AUTHENTICATE_FAIL");
        }
        String assetIp = AssetsPo.getIp();

        // 构造URL
        String url = "http://" + assetIp + ":8191/authenticate/authenticate";

        AssetAuthenticatePo aaPo = authenticateMapper.getAuthenticate(assetUuid);
        if (aaPo == null)
            return ResponseHelper.error(errorCode);

        try {
            ResponseBean respBean = RestTemplateUtil.reqData(url);
            if (respBean == null)
                return ResponseHelper.error(errorCode);
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

                    boolean authenticateFlag = false;
                    if (StringUtils.isValid(decrypt) && decrypt.indexOf("success") > -1){
                        aaPo.setAuthenticate_flag(1);
                        authenticateFlag = true;
                    } else {
                        aaPo.setAuthenticate_flag(-1);
                    }
                    aaPo.setUpdate_time(TimeUtils.getCurrentSystemTimestamp());
                    authenticateMapper.updAuthenticate(aaPo);
                    if (authenticateFlag)
                        return ResponseHelper.success();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHelper.error(errorCode);
    }

}

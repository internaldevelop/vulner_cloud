package com.vulner.bend_server.global;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RestTemplateUtil {

    public static RestTemplate restTemplate = new RestTemplate();

    public static ResponseBean reqData(String url, Map<String, String> map) {
        ResponseBean retObj = null;
        try {
            // 向节点发送请求，并返回节点的响应结果
            ResponseEntity<ResponseBean> responseEntity = restTemplate.getForEntity(url, ResponseBean.class, map);
            retObj = responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retObj;
    }
}

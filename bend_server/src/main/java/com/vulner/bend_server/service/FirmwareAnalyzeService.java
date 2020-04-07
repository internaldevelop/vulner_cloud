package com.vulner.bend_server.service;

import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Service("firmwareAnalyzeService")
public class FirmwareAnalyzeService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 转发
     * @param request
     * @param reqUrl
     * @return
     */
    public Object forwardRequest(HttpServletRequest request, String reqUrl) {

        StringBuffer url = new StringBuffer("http://firmware-analyze/");
        url.append(reqUrl);

        Map<String, String[]> param = request.getParameterMap();

        boolean paramFalg = true;
        for (String key : param.keySet()) {
            if (paramFalg) {
                url.append("?");
                paramFalg = false;
            }

            url.append(key).append("=").append("{").append(key).append("}&");
        }

        try {
            ResponseEntity<ResponseBean> responseEntity = restTemplate.getForEntity(url.toString(), ResponseBean.class, param);
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHelper.error("ERROR_TIME_OUT");
    }


}

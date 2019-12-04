package com.vulner.common.response;

import com.vulner.common.utils.MyUtils;
import org.springframework.stereotype.Component;

public class ResponseHelper {
//    public ResponseBean error(ErrorCodeEnum err) {
//        return  error(err, null);
//    }
//
    static public ResponseBean error(String err, Object data) {
        ResponseBean responseBean = new ResponseBean();
//        responseBean.setCode(err.getCode());
//        responseBean.setError(err.getMsg());
        responseBean.setCode(200);
        responseBean.setError("OK");
        responseBean.setTimeStamp(MyUtils.getCurrentSystemTimestamp());
        responseBean.setPayload(data);
        return responseBean;
    }
//
//    public ResponseBean success() {
//        return success(null);
//    }
//
//    public ResponseBean success(Object data) {
//        return error(ErrorCodeEnum.ERROR_OK, data);
//    }
//
//    public boolean isSuccess(ResponseBean response) {
//        if ( response.getCode() == ErrorCodeEnum.ERROR_OK.getCode())
//            return true;
//        else
//            return false;
//    }
}

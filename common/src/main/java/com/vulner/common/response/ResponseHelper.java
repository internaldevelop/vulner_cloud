package com.vulner.common.response;

import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.utils.ObjUtils;
import com.vulner.common.utils.TimeUtils;

import java.util.List;

public class ResponseHelper {
    static List<ErrorCodeDto> errorCodeList = null;
    public static void setErrorCodeList(List<ErrorCodeDto> errList) {
        errorCodeList = ObjUtils.deepCopyList(errList, ErrorCodeDto.class);
    }

    public static ErrorCodeDto getError(String err) {
        // 未获取到错误代码列表
        if (errorCodeList == null) {
            ErrorCodeDto errorCodeDto = new ErrorCodeDto();
            errorCodeDto.setName("ERROR_SYS_CODE_UNREACHABLE");
            errorCodeDto.setId(1);
            errorCodeDto.setConcept("系统编码微服务不可访问，无法识别该错误代码");
            errorCodeDto.setGroup("network");
            return errorCodeDto;
        }

        // 从列表中匹配错误代码
        for (ErrorCodeDto errorCode: errorCodeList) {
            if (errorCode.getName().equalsIgnoreCase(err)) {
                return errorCode;
            }
        }

        // 匹配不到的错误代码，返回未知错误
        ErrorCodeDto errorUnknown = new ErrorCodeDto();
        errorUnknown.setName("ERROR_UNKNOWN");
        errorUnknown.setId(2);
        errorUnknown.setConcept("未知错误");
        errorUnknown.setGroup("general");
        return errorUnknown;
    }

    public static ResponseBean error(String err) {
        return  error(err, null);
    }

    public static ResponseBean error(String err, Object data) {
        ErrorCodeDto error = getError(err);
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(error.getId());
        responseBean.setError(error.getConcept());
        responseBean.setTimeStamp(TimeUtils.getCurrentSystemTimestamp());
        responseBean.setPayload(data);
        return responseBean;
    }

    public static ResponseBean success() {
        return success(null);
    }

    public static ResponseBean success(Object data) {
        return error("ERROR_OK", data);
    }

    public static boolean isSuccess(ResponseBean response) {
        return ( response.getCode() == 0);
    }
}

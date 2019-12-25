package com.vulner.common.response;

import com.alibaba.fastjson.JSONArray;
import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.utils.ObjUtils;
import com.vulner.common.utils.TimeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ResponseHelper {
    static List<ErrorCodeDto> errorCodeList = null;
    public static void setErrorCodeList(List<ErrorCodeDto> errList) {
        errorCodeList = ObjUtils.deepCopyList(errList, ErrorCodeDto.class);
    }
    public static List<ErrorCodeDto> getErrorCodeList() {
        return ObjUtils.deepCopyList(errorCodeList, ErrorCodeDto.class);
    }

    public static ErrorCodeDto getErrorCodesUnavail() {
        ErrorCodeDto errorCodeDto = new ErrorCodeDto();
        errorCodeDto.setCode("ERROR_SYS_CODE_UNREACHABLE");
        errorCodeDto.setId(1);
        errorCodeDto.setConcept("系统编码微服务不可访问，无法识别该错误代码");
        errorCodeDto.setGroup("network");
        return errorCodeDto;
    }

    public static ErrorCodeDto getErrorUnknown() {
        ErrorCodeDto errorUnknown = new ErrorCodeDto();
        errorUnknown.setCode("ERROR_UNKNOWN");
        errorUnknown.setId(2);
        errorUnknown.setConcept("未知错误");
        errorUnknown.setGroup("general");
        return errorUnknown;
    }

    public static ErrorCodeDto getErrorByCode(String errCode) {
        // 未获取到错误代码列表
        if (errorCodeList == null) {
            return getErrorCodesUnavail();
        }

        // 从列表中匹配错误代码
        for (ErrorCodeDto errorCode: errorCodeList) {
            if (errorCode.getCode().equalsIgnoreCase(errCode)) {
                return errorCode;
            }
        }

        // 匹配不到的错误代码，返回未知错误
        return getErrorUnknown();
    }

    public static ErrorCodeDto getErrorById(int id) {
        // 未获取到错误代码列表
        if (errorCodeList == null) {
            return getErrorCodesUnavail();
        }

        // 从列表中匹配错误代码
        for (ErrorCodeDto errorCode: errorCodeList) {
            if (errorCode.getId() == id) {
                return errorCode;
            }
        }

        // 匹配不到的错误代码，返回未知错误
        return getErrorUnknown();
    }

    public static ResponseBean error(String errCode) {
        return  error(errCode, null);
    }

    public static ResponseBean error(String errCode, Object data) {
        ErrorCodeDto error = getErrorByCode(errCode);
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(error.getCode());
        responseBean.setId(error.getId());
        responseBean.setError(error.getConcept());
        responseBean.setTimeStamp(TimeUtils.getCurrentSystemTimestamp());
        responseBean.setPayload(data);
        return responseBean;
    }

    public static ResponseBean invalidParams(BindingResult bindingResult) {
        List<String> errMsgList = new ArrayList<>();
        List<ObjectError> errorList = bindingResult.getAllErrors();
        for (ObjectError error : errorList) {
            errMsgList.add(error.getDefaultMessage());
        }
        return error("ERROR_INVALID_PARAMETER", errMsgList);
    }

    public static ResponseBean success() {
        return success(null);
    }

    public static ResponseBean success(Object data) {
        return error("ERROR_OK", data);
    }

    public static boolean isSuccess(ResponseBean response) {
        return ( response.getId() == 0);
    }
}

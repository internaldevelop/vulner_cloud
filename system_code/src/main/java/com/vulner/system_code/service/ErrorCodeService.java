package com.vulner.system_code.service;

import com.alibaba.excel.EasyExcel;
import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorCodeService {

    public List<ErrorCodeDto> loadErrorCodes() {
        try {

            String fileName = FileUtils.getClassRootPath() + "ErrorCode.xlsx";
            List<ErrorCodeDto> errorCodeList = EasyExcel.read(fileName).head(ErrorCodeDto.class).sheet().doReadSync();

            return errorCodeList;

        } catch (Exception e) {

            e.printStackTrace();
            List<ErrorCodeDto> errorCodeList = new ArrayList<>();

            ErrorCodeDto errorCodeDto = new ErrorCodeDto();
            errorCodeDto.setCode("ERROR_OK");
            errorCodeDto.setId(0);
            errorCodeDto.setConcept("成功");
            errorCodeDto.setGroup("general");
            errorCodeList.add(errorCodeDto);

            return errorCodeList;
        }
    }

    public ResponseBean getAllErrorCodes() {
        return ResponseHelper.success(ResponseHelper.getErrorCodeList());
    }

    public ResponseBean refreshErrorCodes() {
        List<ErrorCodeDto> errorCodeDtos = loadErrorCodes();
        ResponseHelper.setErrorCodeList(errorCodeDtos);
        return ResponseHelper.success();
    }

    public Object errorByCode(String code) {
        return ResponseHelper.success(ResponseHelper.getErrorByCode(code));
    }

    public Object errorById(int id) {
        return ResponseHelper.success(ResponseHelper.getErrorById(id));
    }

}

package com.vulner.system_code.service;

import com.alibaba.excel.EasyExcel;
import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.utils.FileUtils;
import com.vulner.system_code.global.ErrorCodeEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorCodeService {
    public List<ErrorCodeDto> readAll() {
        try {
            String fileName = FileUtils.getClassRootPath() + "ErrorCode.xlsx";
            List<ErrorCodeDto> errorCodeList = EasyExcel.read(fileName).head(ErrorCodeEntity.class).sheet().doReadSync();
            return errorCodeList;
        } catch (Exception e) {
            e.printStackTrace();
            List<ErrorCodeDto> errorCodeList = new ArrayList<>();
            ErrorCodeDto errorCodeDto = new ErrorCodeDto();
            errorCodeDto.setName("ERROR_OK");
            errorCodeDto.setId(0);
            errorCodeDto.setConcept("成功");
            errorCodeDto.setGroup("general");
            errorCodeList.add(errorCodeDto);
            return errorCodeList;
        }
    }
}

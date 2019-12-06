package com.vulner.system_log.service;

import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.system_log.bean.po.SystemLogPo;
import com.vulner.system_log.dao.SystemLogsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class SystemLogService {
    @Autowired
    SystemLogsMapper systemLogsMapper;

    public boolean addLog(String caller, String account_uuid, int type,
                          String title, String contents, String extra_info) {
        SystemLogPo systemLogPo = new SystemLogPo();

        systemLogPo.setUuid(StringUtils.generateUuid());
        systemLogPo.setCaller(caller);
        systemLogPo.setCreate_account_uuid(account_uuid);
        systemLogPo.setType(type);
        systemLogPo.setTitle(title);
        systemLogPo.setContents(contents);
        systemLogPo.setExtra_info(extra_info);
        systemLogPo.setCreate_time(TimeUtils.getCurrentSystemTimestamp());

        return systemLogsMapper.addLog(systemLogPo) == 1;
    }
}

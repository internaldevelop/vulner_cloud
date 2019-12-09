package com.vulner.system_log.service;

import com.vulner.common.enumeration.LogTypeEnum;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.system_log.bean.po.SystemLogPo;
import com.vulner.system_log.dao.SystemLogsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

@Component
public class SystemLogService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SystemLogsMapper systemLogsMapper;

    public ResponseBean addLog(String caller, String account_uuid, int type,
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

        int rv = systemLogsMapper.addLog(systemLogPo);
        if (rv == 1) {
            return ResponseHelper.success();
        } else {
            return ResponseHelper.error("ERROR_ADD_LOG_FAILED");
        }
    }

    public boolean writeLogFile(String caller, String account_uuid, int type,
                                String title, String contents, String extra_info) {
        if (LogTypeEnum.SUCCESS.getType() == type) {
            String debugStr = String.format("来自系统[%s]: [%s]调用成功。详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                    caller, title, account_uuid, type, contents, extra_info);
            logger.info(debugStr);
        } else if (LogTypeEnum.FAIL.getType() == type) {
            String debugStr = String.format("来自系统[%s]: [%s]调用失败。详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                    caller, title, account_uuid, type, contents, extra_info);
            logger.info(debugStr);
        } else if (LogTypeEnum.SYS_ERROR.getType() == type) {
            String errorStr = String.format("来自系统[%s]: [%s]系统错误。详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                    caller, title, account_uuid, type, contents, extra_info);
            logger.error(errorStr);
        } else if (LogTypeEnum.INFO.getType() == type) {
            String infoStr = String.format("来自系统[%s]: [%s]。详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                    caller, title, account_uuid, type, contents, extra_info);
            logger.info(infoStr);
        } else if (LogTypeEnum.EXCEPT.getType() == type) {
            String errorStr = String.format("来自系统[%s]: [%s]系统异常。详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                    caller, title, account_uuid, type, contents, extra_info);
            logger.error(errorStr);
        } else if (LogTypeEnum.WARNING.getType() == type) {
            String warnStr = String.format("来自系统[%s]: [%s]告警。详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                    caller, title, account_uuid, type, contents, extra_info);
            logger.warn(warnStr);
        }

        return true;
    }

    public ResponseBean getLogs(String caller, String title, int offset, int count) {
        List<SystemLogPo> logList = systemLogsMapper.getLogs(caller, title, offset, count);
        if (logList.size() > 0) {
            return ResponseHelper.success(logList);
        } else {
            return ResponseHelper.error("ERROR_LOGS_NOT_FOUND");
        }
    }

    public ResponseBean getLogs(String caller, String title,
                                Timestamp beginTime, Timestamp endTime,
                                int offset, int count) {
        List<SystemLogPo> logList = systemLogsMapper.getPeriodLogs(caller, title, beginTime, endTime, offset, count);
        if (logList.size() > 0) {
            return ResponseHelper.success(logList);
        } else {
            return ResponseHelper.error("ERROR_LOGS_NOT_FOUND");
        }
    }
}

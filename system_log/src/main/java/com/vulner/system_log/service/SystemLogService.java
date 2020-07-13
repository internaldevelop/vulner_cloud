package com.vulner.system_log.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.vulner.common.enumeration.LogTypeEnum;
import com.vulner.common.global.MyConst;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import com.vulner.common.utils.TimeUtils;
import com.vulner.system_log.bean.po.SystemLogInfoConfigPo;
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

    public ResponseBean addLog(String caller, String account_info, int type,
                               String title, String contents, String extra_info) {
        SystemLogPo systemLogPo = new SystemLogPo();

        systemLogPo.setUuid(StringUtils.generateUuid());
        systemLogPo.setCaller(caller);
        systemLogPo.setAccount_info(account_info);
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

    public boolean writeLogFile(String caller, String account_info, int type,
                                String title, String contents, String extra_info) {
        String detailInfo = String.format("详细信息如下：\n用户账户uuid: [%s], 日志种类: [%d], 内容: [%s], 扩展信息: [%s]",
                account_info, type, contents, extra_info);
        if (LogTypeEnum.SUCCESS.getType() == type) {
            String debugStr = String.format("来自系统[%s]: [%s]--调用成功。%s", caller, title, detailInfo);
            logger.info(debugStr);
        } else if (LogTypeEnum.FAIL.getType() == type) {
            String debugStr = String.format("来自系统[%s]: [%s]--调用失败。%s", caller, title, detailInfo);
            logger.info(debugStr);
        } else if (LogTypeEnum.SYS_ERROR.getType() == type) {
            String errorStr = String.format("来自系统[%s]: [%s]--系统错误。%s", caller, title, detailInfo);
            logger.error(errorStr);
        } else if (LogTypeEnum.INFO.getType() == type) {
            String infoStr = String.format("来自系统[%s]: [%s]。%s", caller, title, detailInfo);
            logger.info(infoStr);
        } else if (LogTypeEnum.EXCEPT.getType() == type) {
            String errorStr = String.format("来自系统[%s]: [%s]--系统异常。%s", caller, title, detailInfo);
            logger.error(errorStr);
        } else if (LogTypeEnum.WARNING.getType() == type) {
            String warnStr = String.format("来自系统[%s]: [%s]--告警。%s", caller, title, detailInfo);
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

    public ResponseBean searchLogsByFilters(int type,
                                            String caller,
                                            String accountName,
                                            String accountAlias,
                                            String title,
                                            String beginTime,
                                            String endTime,
                                            int offset,
                                            int count) {
        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        // 起始时间未指定，则使用1900年1月1日作为过滤器的起始时间
        if (Strings.isNullOrEmpty(beginTime)) {
            beginTime = "1900-01-01";
        }
        Timestamp timeFrom = TimeUtils.parseTimeFromString(beginTime, timeFormat);

        // 结束时间未指定，则使用当前时间作为过滤器的结束时间
        Timestamp timeTo;
        if (Strings.isNullOrEmpty(endTime)) {
            timeTo = TimeUtils.getCurrentSystemTimestamp();
        } else {
            timeTo = TimeUtils.parseTimeFromString(endTime, timeFormat);
        }

        // 组装账号名为待查询格式
        if (Strings.isNullOrEmpty(accountName)) {
            accountName = "";
        } else {
            accountName = String.format("\"account_name\":\"%s\"", accountName);
        }

        // 组装账号名为待查询格式
        if (Strings.isNullOrEmpty(accountAlias)) {
            accountAlias = "";
        } else {
            accountAlias = String.format("\"account_alias\":\"%s\"", accountAlias);
        }

        // caller 和 title 如为null，则设置为空字符串
        if (caller == null) {
            caller = "";
        }
        if (title == null) {
            title = "";
        }

        // 如果 count 为0，则默认为 MAX_RECORD_COUNT 条数据
        if (count == 0) {
            count = MyConst.MAX_RECORD_COUNT;
        }

        // 计算满足过滤器条件的记录总数
        int total = systemLogsMapper.countByFilters(type, caller, accountName, accountAlias,
                title, timeFrom, timeTo);

        // 读取满足条件的记录
        List<SystemLogPo> logsList = systemLogsMapper.searchByFilters(type, caller, accountName, accountAlias,
                title, timeFrom, timeTo, offset, count);

        // 响应数据包含记录总数，当前记录数，和查询出来的日志记录集合
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", total);
        jsonObject.put("count", logsList.size());
        jsonObject.put("logs", logsList);

        return ResponseHelper.success(jsonObject);
    }

    public Object getLogInfoConfig() {
        List<SystemLogInfoConfigPo> logInfoConfigList = systemLogsMapper.getLogInfoConfig();
        if (logInfoConfigList != null && logInfoConfigList.size() > 0) {
            return ResponseHelper.success(logInfoConfigList);
        } else {
            return ResponseHelper.error("ERROR_OK");
        }
    }

    public Object addLogInfoConfig(String logField, String logFieldDesc, String isDisplay) {
        if (!StringUtils.isValid(logField) || !StringUtils.isValid(isDisplay)) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }
        SystemLogInfoConfigPo slicPo = systemLogsMapper.getLogInfoConfigInfo(null, logField);
        if (slicPo == null) {
            slicPo = new SystemLogInfoConfigPo();
            slicPo.setUuid(StringUtils.generateUuid());
            slicPo.setLog_field(logField);
            slicPo.setLog_field_desc(logFieldDesc);
            slicPo.setIs_display(isDisplay);
            slicPo.setIs_default("1");
            slicPo.setCreate_time(TimeUtils.getCurrentSystemTimestamp());
            systemLogsMapper.addLogInfoConfig(slicPo);
        } else if ("1".equals(slicPo.getIs_default())) {
            slicPo.setIs_display(isDisplay);
            slicPo.setUpdate_time(TimeUtils.getCurrentSystemTimestamp());
            systemLogsMapper.uptLogInfoConfig(slicPo);
        } else {
            return ResponseHelper.error("ERROR_GENERAL_ERROR");
        }

        return ResponseHelper.success();
    }

    public Object uptLogInfoConfig(String uuid, String isDisplay) {

        if (!StringUtils.isValid(uuid) || !StringUtils.isValid(isDisplay))
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        SystemLogInfoConfigPo slicPo = systemLogsMapper.getLogInfoConfigInfo(uuid, null);
        if (slicPo == null)
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");

        if ("1".equals(slicPo.getIs_default())) {
            slicPo.setIs_display(isDisplay);
            slicPo.setUpdate_time(TimeUtils.getCurrentSystemTimestamp());
            systemLogsMapper.uptLogInfoConfig(slicPo);
        } else {
            return ResponseHelper.error("ERROR_GENERAL_ERROR");
        }

        return ResponseHelper.success();
    }
}

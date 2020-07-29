package com.vulner.system_log.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.vulner.common.bean.dto.ErrorCodeDto;
import com.vulner.common.bean.dto.ExcelDataDto;
import com.vulner.common.enumeration.LogTypeEnum;
import com.vulner.common.global.MyConst;
import com.vulner.common.response.ResponseBean;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.*;
import com.vulner.system_log.bean.po.SystemLogBackupPo;
import com.vulner.system_log.bean.po.SystemLogExcelPo;
import com.vulner.system_log.bean.po.SystemLogInfoConfigPo;
import com.vulner.system_log.bean.po.SystemLogPo;
import com.vulner.system_log.dao.SystemLogsMapper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 日志删除
     * @param uuid
     * @return
     */
    public Object delLog(String uuid) {
        if (!StringUtils.isValid(uuid)) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        systemLogsMapper.uptLog(uuid);
        return ResponseHelper.success();
    }

    /**
     * 导入
     * @param file
     * @return
     */
    public Object importLogs(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            List<SystemLogExcelPo> errorCodeList = EasyExcel.read(inputStream).head(SystemLogExcelPo.class).sheet().doReadSync();

            if (errorCodeList != null && errorCodeList.size() > 0) {
                for (SystemLogExcelPo sysLogExcelPo : errorCodeList) {
                    String uuid = sysLogExcelPo.getUuid();

                    HashMap<String, Object> params = new HashMap<>();
                    params.put("uuid", uuid);
                    List<SystemLogPo> logList = systemLogsMapper.getLogList(params);

                    if (logList == null || logList.size() < 1) {
                        SystemLogPo sysLogPo = new SystemLogPo();
                        sysLogPo.setUuid(uuid);
                        sysLogPo.setType(sysLogExcelPo.getType());
                        sysLogPo.setCaller(sysLogExcelPo.getCaller());
                        sysLogPo.setAccount_info(sysLogExcelPo.getAccount_info());
                        sysLogPo.setTitle(sysLogExcelPo.getTitle());
                        sysLogPo.setContents(sysLogExcelPo.getContents());
                        sysLogPo.setExtra_info(sysLogExcelPo.getExtra_info());
                        sysLogPo.setCreate_time(TimeUtils.parseTimeFromString(sysLogExcelPo.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
                        systemLogsMapper.addLog(sysLogPo);
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseHelper.success();
    }

    /**
     * 下载
     * @return
     */
    public void downloadLog(HttpServletResponse response, String uuid) {
        if (StringUtils.isValid(uuid)) {
            Map<String, Object> params = new HashMap<>();
            params.put("uuid", uuid);
            SystemLogBackupPo backupInfo = systemLogsMapper.getBackupInfo(params);
            if (backupInfo != null) {
                String fileUrl = backupInfo.getFile_url();
                String fileName = backupInfo.getFile_name();

                ExcelDataDto excelData = new ExcelDataDto();

                List<String> titles = new ArrayList<>();
                titles.add("uuid");
                titles.add("日志类型");
                titles.add("调用者");
                titles.add("账户信息");
                titles.add("日志标题");
                titles.add("日志内容");
                titles.add("扩展信息");
                titles.add("创建时间");

                excelData.setTitles(titles);
                List<SystemLogExcelPo> sysLogList = EasyExcel.read(fileUrl).head(SystemLogExcelPo.class).sheet().doReadSync();

                List<List<Object>> rows = new ArrayList<>();
                for (SystemLogExcelPo sysLogPo : sysLogList) {
                    List<Object> row = new ArrayList<>();
                    row.add(sysLogPo.getUuid());
                    row.add(sysLogPo.getType());
                    row.add(sysLogPo.getCaller());
                    row.add(sysLogPo.getAccount_info());
                    row.add(sysLogPo.getTitle());
                    row.add(sysLogPo.getContents());
                    row.add(sysLogPo.getExtra_info());
                    row.add(sysLogPo.getCreate_time());

                    rows.add(row);
                }
                excelData.setRows(rows);
                excelData.setName("日志记录");

                try {
                    ExcelUtil.exportExcel(response, fileName.substring(0, fileName.lastIndexOf(".")), excelData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }
    }

    /**
     * 日志备份
     * @param beginTime
     * @param endTime
     * @return
     */
    public Object backupLog(String beginTime, String endTime) {
        if (!StringUtils.isValid(beginTime) || !StringUtils.isValid(endTime)) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("begin_time", beginTime);
        params.put("end_time", endTime);
        params.put("status", 1);

        List<SystemLogPo> sysLogList = systemLogsMapper.getLogList(params);
        ExcelDataDto excelData = new ExcelDataDto();
        String fileName = DateFormat.getCurrentDateStr(DateFormat.TIME_STAMP_FORMAT) + "-logs.xlsx";
        String filePath = "./system_log/" + fileName;

        List<String> titles = new ArrayList<>();
        titles.add("uuid");
        titles.add("日志类型");
        titles.add("调用者");
        titles.add("账户信息");
        titles.add("日志标题");
        titles.add("日志内容");
        titles.add("扩展信息");
        titles.add("创建时间");

        excelData.setTitles(titles);

        List<List<Object>> rows = new ArrayList<>();
        for (SystemLogPo sysLogPo : sysLogList) {
            List<Object> row = new ArrayList<>();
            row.add(sysLogPo.getUuid());
            row.add(sysLogPo.getType());
            row.add(sysLogPo.getCaller());
            row.add(sysLogPo.getAccount_info());
            row.add(sysLogPo.getTitle());
            row.add(sysLogPo.getContents());
            row.add(sysLogPo.getExtra_info());
            row.add(sysLogPo.getCreate_time());

            rows.add(row);
        }
        excelData.setRows(rows);
        excelData.setName("日志记录");

        try {
            ExcelUtil.generateExcel(excelData, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SystemLogBackupPo sysLogBackupPo = new SystemLogBackupPo();
        sysLogBackupPo.setUuid(StringUtils.generateUuid());
        sysLogBackupPo.setUser_uuid("");
        sysLogBackupPo.setUser_name("");
        sysLogBackupPo.setFile_url(filePath);
        sysLogBackupPo.setFile_name(fileName);
        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        sysLogBackupPo.setBegin_time(TimeUtils.parseTimeFromString(beginTime +" 00:00:00", timeFormat));
        sysLogBackupPo.setEnd_time(TimeUtils.parseTimeFromString(endTime +" 23:59:59", timeFormat));
        sysLogBackupPo.setCreate_time(TimeUtils.getCurrentSystemTimestamp());
        systemLogsMapper.addLogBackup(sysLogBackupPo);

        return ResponseHelper.success();

    }

    /**
     * 备份列表查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Object backupListLog(Integer pageNum, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();

        if (pageNum != null && pageSize != null) {

            int totalCount = systemLogsMapper.getBackupCount(params);
            Page<SystemLogBackupPo> page = null;
            if (pageNum == null || pageSize == null ){
                pageSize = (pageSize == null) ? 10 : pageSize;
                page = new Page<>(1, pageSize);
            } else {
                params.put("start", Page.getStartPosition(pageNum, pageSize));
                params.put("count", pageSize);
                page = new Page<>(pageNum, pageSize);
            }
            List<SystemLogBackupPo> assetList = systemLogsMapper.getBackupList(params);

            if (assetList == null || assetList.size() < 1) {
                return ResponseHelper.success();
            }

            page.setData(assetList);
            page.setTotalResults(totalCount);
            return ResponseHelper.success(page);
        }

        List<SystemLogBackupPo> assetList = systemLogsMapper.getBackupList(params);

        if (assetList == null || assetList.size() < 1) {
            return ResponseHelper.success();
        }

        return ResponseHelper.success(assetList);
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

    public Object uptLogInfoConfig(String logFields) {

        if (!StringUtils.isValid(logFields))
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        SystemLogInfoConfigPo delPo = new SystemLogInfoConfigPo();
        delPo.setIs_default("1");
        delPo.setIs_display("0");
        delPo.setUpdate_time(TimeUtils.getCurrentSystemTimestamp());
        systemLogsMapper.delLogInfoConfig(delPo);

        String[] log_fields = logFields.split(",");
        for (String logField : log_fields) {
            SystemLogInfoConfigPo slicPo = systemLogsMapper.getLogInfoConfigInfo(null, logField);
            if (slicPo == null)
                continue;

            if ("1".equals(slicPo.getIs_default())) {
                slicPo.setIs_display("1");
                slicPo.setUpdate_time(TimeUtils.getCurrentSystemTimestamp());
                systemLogsMapper.uptLogInfoConfig(slicPo);
            }
        }

        return ResponseHelper.success();
    }



}

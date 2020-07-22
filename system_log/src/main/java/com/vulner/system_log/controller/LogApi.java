package com.vulner.system_log.controller;

import com.vulner.common.response.ResponseHelper;
import com.vulner.system_log.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/sys_log")
public class LogApi {

    @Autowired
    SystemLogService systemLogService;

    @PostMapping(value = "/add")
    @ResponseBody
    public Object recordLog(@RequestParam("caller")String caller,
                            @RequestParam("account_info")String account_info,
                            @RequestParam("type")int type,
                            @RequestParam("title")String title,
                            @RequestParam("contents")String contents,
                            @RequestParam("extra_info")String extra_info) {

        systemLogService.writeLogFile(caller, account_info, type, title, contents, extra_info);

        return systemLogService.addLog(caller, account_info, type, title, contents, extra_info);
    }

    @GetMapping(value = "/get")
    @ResponseBody
    public Object getLogs(@RequestParam("caller")String caller,
                          @RequestParam("title")String title,
                          @RequestParam("offset")int offset,
                          @RequestParam("count")int count) {
        if (caller.length() == 0 || title.length() == 0) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        return systemLogService.getLogs(caller, title, offset, count);
    }

    @GetMapping(value = "/get_in_period")
    @ResponseBody
    public Object getLogs(@RequestParam("caller")String caller,
                          @RequestParam("title")String title,
                          @RequestParam("begin_time")Timestamp beginTime,
                          @RequestParam("end_time")Timestamp endTime,
                          @RequestParam("offset")int offset,
                          @RequestParam("count")int count) {
        if (caller.length() == 0 || title.length() == 0) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        return systemLogService.getLogs(caller, title, beginTime, endTime, offset, count);
    }

    @GetMapping(value = "/search_by_filter")
    @ResponseBody
    public Object searchByFilter(@RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                 @RequestParam(value = "caller", required = false) String caller,
                                 @RequestParam(value = "account_name", required = false) String accountName,
                                 @RequestParam(value = "account_alias", required = false) String accountAlias,
                                 @RequestParam(value = "title", required = false) String title,
                                 @RequestParam(value = "begin_time", required = false) String beginTime,
                                 @RequestParam(value = "end_time", required = false) String endTime,
                                 @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                 @RequestParam(value = "count", required = false, defaultValue = "0") int count) {
        return systemLogService.searchLogsByFilters(type, caller, accountName, accountAlias,
                title, beginTime, endTime, offset, count);
    }

    /**
     * 日志删除
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/delete")
    @ResponseBody
    public Object delLog(@RequestParam("uuid")String uuid) {
        return systemLogService.delLog(uuid);
    }

    /**
     * 日志导入
     * @param file
     * @return
     */
    @PostMapping(value = "/import-logs")
    @ResponseBody
    public Object importLogs(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseHelper.error("ERROR_INVALID_PARAMETER");
        }

        return systemLogService.importLogs(file);
    }

    /**
     * 日志备份
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping(value = "/backup")
    @ResponseBody
    public Object backupLog(@RequestParam("begin_time")String beginTime, @RequestParam("end_time")String endTime) {
        return systemLogService.backupLog(beginTime, endTime);
    }

    /**
     * 日志下载
     * @param response
     * @param uuid
     */
    @GetMapping(value = "/download-log")
    @ResponseBody
    public void downloadLog(HttpServletResponse response, @RequestParam("uuid")String uuid) {
        systemLogService.downloadLog(response, uuid);
    }

    /**
     * 备份列表查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/get-backups")
    @ResponseBody
    public Object getBackups(@RequestParam(value = "page_num", required = false)Integer pageNum,
                                @RequestParam(value = "page_size", required = false)Integer pageSize) {
        return systemLogService.backupListLog(pageNum, pageSize);
    }




    /**
     * 查看日志配置
     * @return
     */
    @GetMapping(value = "/get-log-info-config")
    @ResponseBody
    public Object getLogInfoConfig() {
        return systemLogService.getLogInfoConfig();
    }

    /**
     * 添加日志配置
     * @param logField
     * @param logFieldDesc
     * @param isDisplay
     * @return
     */
    @PostMapping(value = "/add-log-info-config")
    @ResponseBody
    public Object addLogInfoConfig(@RequestParam("log_field")String logField,
                                   @RequestParam("log_field_desc")String logFieldDesc,
                                   @RequestParam("is_display")String isDisplay) {
        return systemLogService.addLogInfoConfig(logField, logFieldDesc, isDisplay);
    }

    /**
     * 修改日志配置
     * @param logFields
     * @return
     */
    @PostMapping(value = "/upt-log-info-config")
    @ResponseBody
    public Object uptLogInfoConfig(@RequestParam("log_fields")String logFields) {
        return systemLogService.uptLogInfoConfig(logFields);
    }
}

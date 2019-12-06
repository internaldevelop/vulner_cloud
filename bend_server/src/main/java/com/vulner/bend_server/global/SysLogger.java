package com.vulner.bend_server.global;

import com.vulner.bend_server.service.SysLogService;
import com.vulner.common.enumeration.LogTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysLogger {
    public final String caller = "fw analyze back-end server";

    @Autowired
    SysLogService sysLogService;

    private void _log(int type,
                    String title,
                    String contents,
                    String extra_info) {
        // Todo: set the account uuid
        String account_uuid = "b3fa2914-a4c4-4dc3-978f-fe7ca3125abb";
        sysLogService.addLog(caller, account_uuid, type, title, contents, extra_info);
    }

    // -----------------------------------------------------------------------
    // SUCCESS = 1
    public void success(String title, String contents) {
        _log(LogTypeEnum.SUCCESS.getType(), title, contents, "");
    }

    public void success(String title, String contents, String extra_info) {
        _log(LogTypeEnum.SUCCESS.getType(), title, contents, extra_info);
    }

    // -----------------------------------------------------------------------
    // FAIL = 2
    public void fail(String title, String contents) {
        _log(LogTypeEnum.FAIL.getType(), title, contents, "");
    }

    public void fail(String title, String contents, String extra_info) {
        _log(LogTypeEnum.FAIL.getType(), title, contents, extra_info);
    }

    // -----------------------------------------------------------------------
    // SYS_ERROR = 3
    public void sysError(String title, String contents) {
        _log(LogTypeEnum.SYS_ERROR.getType(), title, contents, "");
    }

    public void sysError(String title, String contents, String extra_info) {
        _log(LogTypeEnum.SYS_ERROR.getType(), title, contents, extra_info);
    }

    // -----------------------------------------------------------------------
    // INFO = 4
    public void info(String title, String contents) {
        _log(LogTypeEnum.INFO.getType(), title, contents, "");
    }

    public void info(String title, String contents, String extra_info) {
        _log(LogTypeEnum.INFO.getType(), title, contents, extra_info);
    }

    // -----------------------------------------------------------------------
    // EXCEPT = 5
    public void except(String title, String contents) {
        _log(LogTypeEnum.EXCEPT.getType(), title, contents, "");
    }

    public void except(String title, String contents, String extra_info) {
        _log(LogTypeEnum.EXCEPT.getType(), title, contents, extra_info);
    }

    // -----------------------------------------------------------------------
    // WARNING = 6
    public void warning(String title, String contents) {
        _log(LogTypeEnum.WARNING.getType(), title, contents, "");
    }

    public void warning(String title, String contents, String extra_info) {
        _log(LogTypeEnum.WARNING.getType(), title, contents, extra_info);
    }
}

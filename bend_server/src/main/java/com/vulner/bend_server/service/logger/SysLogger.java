package com.vulner.bend_server.service.logger;

import com.alibaba.fastjson.JSONObject;
import com.vulner.bend_server.service.helper.SessionHelper;
import com.vulner.common.enumeration.LogTypeEnum;
import com.vulner.common.global.MyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysLogger {
    public final String caller = "uni-auth service";

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SessionHelper sessionHelper;

    private void _log(int type,
                    String title,
                    String contents,
                    String extra_info) {
        // 组装账户信息 （JSON）
        JSONObject jsonObject = new JSONObject();
        String accessToken = sessionHelper.getSessionAttribute(jsonObject, MyConst.ACCESS_TOKEN);
        sessionHelper.getSessionAttribute(jsonObject, MyConst.ACCOUNT_UUID);
        sessionHelper.getSessionAttribute(jsonObject, MyConst.ACCOUNT_NAME);
        sessionHelper.getSessionAttribute(jsonObject, MyConst.ACCOUNT_ALIAS);
        sysLogService.addLog(accessToken, caller, jsonObject.toJSONString(), type, title, contents, extra_info);
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

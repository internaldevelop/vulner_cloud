package com.vulner.embed_terminal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulner.embed_terminal.bean.po.ExploitInfoTinyPo;
import com.vulner.embed_terminal.global.Page;
import com.vulner.embed_terminal.service.ErrorCodeService;
import com.vulner.embed_terminal.service.LoopholeDBService;
import com.vulner.embed_terminal.service.logger.SysLogService;
import com.vulner.embed_terminal.service.logger.SysLogger;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * 漏洞DB-API
 */
@RestController
@RequestMapping(value = "/vuldb", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoopholeDBApi {

    @Autowired
    SysLogService sysLogService;
    @Autowired
    SysLogger sysLogger;

    @Autowired
    LoopholeDBService loopholeDBService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 漏洞库查询
     * @return
     */
    @GetMapping(value = "/search")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object search(Principal user, @RequestParam(required = false)Integer page_num, @RequestParam(required = false)Integer page_size, @RequestParam(required = false) String name) {
        String accountName = user.getName();
        String msg = String.format("账号（%s）查询", accountName);
        Page<ExploitInfoTinyPo> exploitInfoTinyList = loopholeDBService.search(page_num, page_size, name);
        sysLogger.success("漏洞库查询", msg);
        return ResponseHelper.success(exploitInfoTinyList);
    }

    /**
     * 漏洞库新增
     * @return
     */
    @GetMapping(value = "/add_vul")
    @PreAuthorize("hasAnyAuthority('query')")
    @ResponseBody
    public Object addVul(@RequestBody ExploitInfoTinyPo exploit_info) {
        Boolean addFlag = loopholeDBService.addVul(exploit_info);
        return ResponseHelper.success(addFlag);
    }

    /**
     * 漏洞库数据修改
     * @return
     */
    @GetMapping(value = "/modify_vul")
    @PreAuthorize("hasAnyAuthority('query')")
    @ResponseBody
    public Object modifyVul(@RequestParam String params) throws Exception {
        Map<String, String> map = objectMapper.readValue(params, Map.class);

        Boolean addFlag = loopholeDBService.modifyVul(map);
        return ResponseHelper.success(addFlag);
    }

    /**
     * 漏洞库删除
     * @return
     */
    @GetMapping(value = "/del_vul")
    @PreAuthorize("hasAnyAuthority('query')")
    @ResponseBody
    public Object delVul(@RequestParam("vul_id") String vulId) {
        Boolean addFlag = loopholeDBService.delVul(vulId);
        return ResponseHelper.success(addFlag);
    }




}

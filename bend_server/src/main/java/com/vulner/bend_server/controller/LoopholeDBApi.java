package com.vulner.bend_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulner.bend_server.bean.po.ExploitInfoTinyPo;
import com.vulner.bend_server.global.Page;
import com.vulner.bend_server.global.SysLogger;
import com.vulner.bend_server.service.ErrorCodeService;
import com.vulner.bend_server.service.LoopholeDBService;
import com.vulner.bend_server.service.SysLogService;
import com.vulner.common.response.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 漏洞DB-API
 */
@RestController
@RequestMapping(value = "/vuldb", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoopholeDBApi {

    @Autowired
    ErrorCodeService errorCodeService;
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
    @ResponseBody
    public Object search(@RequestParam(required = false)Integer page_num, @RequestParam(required = false)Integer page_size, @RequestParam(required = false) String name) {
        Page<ExploitInfoTinyPo> exploitInfoTinyList = loopholeDBService.search(page_num, page_size, name);
        return ResponseHelper.success(exploitInfoTinyList);
    }

    /**
     * 漏洞库新增
     * @return
     */
    @GetMapping(value = "/add_vul")
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
    @ResponseBody
    public Object delVul(@RequestParam("vul_id") String vulId) {
        Boolean addFlag = loopholeDBService.delVul(vulId);
        return ResponseHelper.success(addFlag);
    }




}

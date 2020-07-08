package com.vulner.bend_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vulner.bend_server.bean.po.CnvdSharePo;
import com.vulner.bend_server.global.Page;
import com.vulner.bend_server.service.LoopholeDBService;
import com.vulner.bend_server.service.logger.SysLogService;
import com.vulner.bend_server.service.logger.SysLogger;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
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
        Page<CnvdSharePo> exploitInfoTinyList = loopholeDBService.search(page_num, page_size, name);
        sysLogger.success("漏洞库查询", msg);
        return ResponseHelper.success(exploitInfoTinyList);
    }

    /**
     * 漏洞库新增
     * @return
     */
    @PostMapping(value = "/add_vul")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object addVul(@RequestParam String params) {
        try {
            if (!StringUtils.isValid(params))
                return ResponseHelper.error("ERROR_INVALID_PARAMETER");

            Map<String, Object> pMap = objectMapper.readValue(params, Map.class);
            if (pMap.size() < 1) {
                return ResponseHelper.error("ERROR_INVALID_PARAMETER");
            }

            return loopholeDBService.addVul(pMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseHelper.error("ERROR_GENERAL_ERROR");
    }

    /**
     * 漏洞库数据修改
     * @return
     */
    @PostMapping(value = "/modify_vul")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object modifyVul(@RequestParam String params) {

        try {
            if (!StringUtils.isValid(params))
                return ResponseHelper.error("ERROR_INVALID_PARAMETER");

            Map<String, Object> pMap = objectMapper.readValue(params, Map.class);
            if (pMap.size() < 1) {
                return ResponseHelper.error("ERROR_INVALID_PARAMETER");
            }
            return loopholeDBService.modifyVul(pMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHelper.error("ERROR_GENERAL_ERROR");
    }

    /**
     * 漏洞库删除
     * @return
     */
    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object delVul(@RequestParam("vul_id") String vulId) {
        Boolean addFlag = loopholeDBService.delVul(vulId);
        return ResponseHelper.success(addFlag);
    }

    /**
     * 按照年份统计
     * @param user
     * @return
     */
    @GetMapping(value = "/statistics-year")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object statisticsYear(Principal user) {
        String accountName = user.getName();
        String msg = String.format("账号（%s）年份统计查询", accountName);
        sysLogger.success("漏洞库年份统计查询", msg);
        return loopholeDBService.statisticsYear();
    }

    /**
     * 按照厂商统计
     * @param user
     * @return
     */
    @GetMapping(value = "/statistics-discoverer")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object statisticsDiscoverer(Principal user) {
        String accountName = user.getName();
        String msg = String.format("账号（%s）年份统计查询", accountName);
        sysLogger.success("漏洞库年份统计查询", msg);
        return loopholeDBService.statisticsDiscoverer();
    }

    /**
     * 按照级别统计
     * @param user
     * @return
     */
    @GetMapping(value = "/statistics-level")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object statisticsLevel(Principal user) {
        String accountName = user.getName();
        String msg = String.format("账号（%s）年份统计查询", accountName);
        sysLogger.success("漏洞库年份统计查询", msg);
        return loopholeDBService.statisticsLevel();
    }

    /**
     * 主要厂商公开漏洞趋势
     * @param user
     * @return
     */
    @GetMapping(value = "/statistics-trend")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object statisticsTrend(Principal user) {
        String accountName = user.getName();
        String msg = String.format("账号（%s）年份统计查询", accountName);
        sysLogger.success("漏洞库年份统计查询", msg);
        return loopholeDBService.statisticsTrend();
    }

}

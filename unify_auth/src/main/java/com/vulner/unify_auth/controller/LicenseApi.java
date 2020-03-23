package com.vulner.unify_auth.controller;

import com.vulner.unify_auth.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping(value = "/license", produces = MediaType.APPLICATION_JSON_VALUE)
public class LicenseApi {

    @Autowired
    private LicenseService licenseService;

    /**
     * 生成License
     * @param response
     * @param user
     * @param accountUuid
     * @param expireTime 到期时间
     * @param sign 标识 0:续期; 1:角色授权
     * @param roleUuids 角色
     */
    @GetMapping(value = "/create")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public void createLicense(HttpServletResponse response, Principal user, @RequestParam(required = false, value = "account_uuid") String accountUuid, @RequestParam("expire_time") String expireTime, @RequestParam("sign") String sign, @RequestParam(required = false, value = "role_uuids") String roleUuids) {
        licenseService.createLicense(response, user, accountUuid, expireTime, sign, roleUuids);
    }

    /**
     * 导入License
     * @return
     */
    @PostMapping(value = "/import")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object importLicense(Principal user, @RequestParam("file") MultipartFile file){
        return licenseService.importLicense(user, file);
    }

}

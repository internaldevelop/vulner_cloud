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
     * @return
     */
    @GetMapping(value = "/create")
    @PreAuthorize("hasAnyAuthority('statistics')")
    public void createLicense(HttpServletResponse response, Principal user, @RequestParam("user_uuid") String userUuid, @RequestParam("expire_time") String expireTime) {
        licenseService.createLicense(response, user, userUuid, expireTime);
    }

    /**
     * 导入License
     * @return
     */
    @GetMapping(value = "/import")
    @PreAuthorize("hasAnyAuthority('statistics')")
    @ResponseBody
    public Object importLicense(Principal user, @RequestParam("file") MultipartFile file){
        licenseService.importLicense(user, file);
        return null;
    }

}

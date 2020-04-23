package com.vulner.bend_server.service;

import com.vulner.bend_server.bean.dto.AssetAuthenticateDto;
import com.vulner.bend_server.bean.po.ExploitInfoTinyPo;
import com.vulner.bend_server.dao.AssetsMapper;
import com.vulner.bend_server.global.Page;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AssetsService {

    @Autowired
    AssetsMapper assetsMapper;

    /**
     * 获取设备列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param ip
     * @param osType
     * @param empowerFlag
     * @param authenticateFlag
     * @return
     */
    public Object getAssets(Integer pageNum, Integer pageSize, String name, String ip, String osType, String empowerFlag, String authenticateFlag) {

        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isValid(name))
            params.put("name", name);
        if (StringUtils.isValid(ip))
            params.put("ip", ip);
        if (StringUtils.isValid(osType))
            params.put("os_type", osType);
        if (StringUtils.isValid(empowerFlag))
            params.put("empower_flag", empowerFlag);
        if (StringUtils.isValid(authenticateFlag))
            params.put("authenticate_flag", authenticateFlag);

        int totalCount = assetsMapper.getAssetAuthCount(params);

        Page<AssetAuthenticateDto> page = null;
        if (pageNum == null || pageSize == null) {
            int pageSizeNum = (pageSize == null) ? 10 : pageSize;
            page = new Page<>(1, pageSizeNum);
        } else {
            params.put("start", Page.getStartPosition(pageNum, pageSize));
            params.put("count", pageSize);
            page = new Page<>(pageNum, pageSize);
        }

        List<AssetAuthenticateDto> assetList = assetsMapper.getAssetAuth(params);

        page.setData(assetList);
        page.setTotalResults(totalCount);

        return ResponseHelper.success(page);
    }
}

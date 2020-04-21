package com.vulner.bend_server.service;

import com.vulner.bend_server.bean.po.AssetsPo;
import com.vulner.bend_server.dao.AssetsMapper;
import com.vulner.common.response.ResponseHelper;
import com.vulner.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetsService {

    @Autowired
    AssetsMapper assetsMapper;

    /**
     * 获取设备列表
     * @param assetUuid
     * @return
     */
    public Object getAssets(String assetUuid) {
        if (StringUtils.isValid(assetUuid)) {
            AssetsPo AssetsPo = assetsMapper.getAssetsByUuid(assetUuid);
            return ResponseHelper.success(AssetsPo);
        }
        List<AssetsPo> assetList = assetsMapper.getAssets();

        return ResponseHelper.success(assetList);
    }
}

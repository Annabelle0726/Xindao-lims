package com.ruoyi.require.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.excel.FeLightningProtectionExcel;
import com.ruoyi.require.pojo.FeLightningProtection;

import java.util.List;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-防雷检测 服务类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:36
 */
public interface FeLightningProtectionService extends IService<FeLightningProtection> {

    List<FeLightningProtectionExcel> exportOfLightningProtectionDetection();

}

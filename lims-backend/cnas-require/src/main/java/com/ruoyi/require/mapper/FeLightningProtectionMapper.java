package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.require.excel.FeLightningProtectionExcel;
import com.ruoyi.require.pojo.FeLightningProtection;

import java.util.List;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-防雷检测 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:36
 */
public interface FeLightningProtectionMapper extends BaseMapper<FeLightningProtection> {

    List<FeLightningProtectionExcel> exportOfLightningProtectionDetection();

}

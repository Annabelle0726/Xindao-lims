package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.require.excel.FeLightningProtectionExcel;
import com.ruoyi.require.mapper.FeLightningProtectionMapper;
import com.ruoyi.require.pojo.FeLightningProtection;
import com.ruoyi.require.service.FeLightningProtectionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-防雷检测 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:36
 */
@Service
public class FeLightningProtectionServiceImpl extends ServiceImpl<FeLightningProtectionMapper, FeLightningProtection> implements FeLightningProtectionService {

    @Override
    public List<FeLightningProtectionExcel> exportOfLightningProtectionDetection() {
        return baseMapper.exportOfLightningProtectionDetection();
    }
}

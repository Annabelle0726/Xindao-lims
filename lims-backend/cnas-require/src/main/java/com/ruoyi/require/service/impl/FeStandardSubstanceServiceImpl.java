package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.require.mapper.FeStandardSubstanceMapper;
import com.ruoyi.require.pojo.FeStandardSubstance;
import com.ruoyi.require.service.FeStandardSubstanceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标准物质清单 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-13 03:58:59
 */
@Service
public class FeStandardSubstanceServiceImpl extends ServiceImpl<FeStandardSubstanceMapper, FeStandardSubstance> implements FeStandardSubstanceService {

    @Override
    public IPage<FeStandardSubstance> page(Page page, FeStandardSubstance feStandardSubstance) {
        return this.baseMapper.getPage(page,feStandardSubstance);
    }
}

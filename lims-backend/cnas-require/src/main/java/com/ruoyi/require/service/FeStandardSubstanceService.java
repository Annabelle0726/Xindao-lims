package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.pojo.FeStandardSubstance;

/**
 * <p>
 * 标准物质清单 服务类
 * </p>
 *
 * @author
 * @since 2024-11-13 03:58:59
 */
public interface FeStandardSubstanceService extends IService<FeStandardSubstance> {

    IPage<FeStandardSubstance> page(Page page, FeStandardSubstance feStandardSubstance);
}

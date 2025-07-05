package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.dto.SampleOrderDto;
import com.ruoyi.inspect.pojo.InsOrderRates;

/**
 * 订单费用表
 *
 * @author zhuo
 * @since 2025-02-28
 */
public interface InsOrderRatesService extends IService<InsOrderRates> {

    /**
     *
     * @param page
     * @param sampleOrderDto
     * @return
     */
    IPage<SampleOrderDto> selectInsOrderRates(Page page, SampleOrderDto sampleOrderDto);
}


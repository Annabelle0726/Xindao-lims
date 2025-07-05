package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.dto.SampleOrderDto;
import com.ruoyi.inspect.mapper.InsOrderRatesMapper;
import com.ruoyi.inspect.pojo.InsOrderRates;
import com.ruoyi.inspect.service.InsOrderRatesService;
import org.springframework.stereotype.Service;

/**
 * 订单费用表
 *
 * @author zhuo
 * @since 2025-02-28
 */
@Service
public class InsOrderRatesServiceImpl extends ServiceImpl<InsOrderRatesMapper, InsOrderRates> implements InsOrderRatesService {

    /**
     * 查询订单费用列表
     * @param page
     * @param sampleOrderDto
     * @return
     */
    @Override
    public IPage<SampleOrderDto> selectInsOrderRates(Page page, SampleOrderDto sampleOrderDto) {
        return baseMapper.selectInsOrderRates(page, QueryWrappers.queryWrappers(sampleOrderDto));
    }
}


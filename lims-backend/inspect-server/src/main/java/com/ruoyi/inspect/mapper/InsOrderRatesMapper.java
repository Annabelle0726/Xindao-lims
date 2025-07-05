package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.SampleOrderDto;
import com.ruoyi.inspect.pojo.InsOrderRates;
import org.apache.ibatis.annotations.Param;

/**
 * 订单费用表
 *
 * @author zhuo
 * @since 2025-02-28
 */
public interface InsOrderRatesMapper extends BaseMapper<InsOrderRates> {

    /**
     * 查询订单费用列表
     * @return
     */
    IPage<SampleOrderDto> selectInsOrderRates(Page page, @Param("ew") QueryWrapper<SampleOrderDto> ew);
}


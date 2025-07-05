package com.ruoyi.inspect.controller;



import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.SampleOrderDto;
import com.ruoyi.inspect.pojo.InsOrderRates;
import com.ruoyi.inspect.service.InsOrderRatesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 订单费用表
 *
 * @author zhuo
 * @since 2025-02-28
 */
@Api(tags = "订单费用信息")
@RestController
@RequestMapping("/insOrderRates")
public class InsOrderRatesController  {

    @Resource
    private InsOrderRatesService insOrderRatesService;


    /**
     * 查询订单列表
     * @param page
     * @param sampleOrderDto
     * @return
     */
    @ApiOperation(value = "查询订单列表")
    @GetMapping("/selectRatesPage")
    public Result selectInsOrderRates(Page page, SampleOrderDto sampleOrderDto) {
        return Result.success(insOrderRatesService.selectInsOrderRates(page, sampleOrderDto));
    }


    /**
     * 查询订单列表
     * @param insOrderId
     * @return
     */
    @ApiOperation(value = "查询订单费用详情")
    @GetMapping("/selectRatesDetail")
    public Result selectRatesDetail(Page page, Integer insOrderId) {
        return Result.success(insOrderRatesService.page(page, Wrappers.<InsOrderRates>lambdaQuery()
                .eq(InsOrderRates::getInsOrderId, insOrderId)));
    }

}


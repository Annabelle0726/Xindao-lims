package com.ruoyi.inspect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.mapper.IfsInventoryQuantityMapper;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.*;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsOrderTemplate;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.service.InsOrderService;
import com.ruoyi.inspect.service.InsOrderTemplateService;
import com.ruoyi.inspect.service.InsProductService;
import com.ruoyi.inspect.service.InsSampleService;
import com.ruoyi.inspect.vo.InsOrderPrintingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/insOrder")
@Api(tags="检验单模块")
public class InsOrderController {

    private InsOrderService insOrderService;

    private InsSampleService insSampleService;

    private InsProductService insProductService;

    private IfsInventoryQuantityMapper ifsInventoryQuantityMapper;

    private InsOrderTemplateService insOrderTemplateService;

    //获取检验下单数据
    @ApiOperation(value = "查询单位检验单")
    @GetMapping("/selectInsOrderParameter")
    public Result selectInsOrderParameter(Page page, SampleOrderDto sampleOrderDto) throws Exception {
        return Result.success(insOrderService.selectInsOrderParameter(page, sampleOrderDto));
    }

    //用于检验下单区别查看所有订单和只查看同一个委托单位的订单
    @ApiOperation(value = "查询所有检验单")
    @GetMapping("/selectAllInsOrderParameter")
    public Result selectAllInsOrderParameter() {
        return Result.success();
    }


    @ApiOperation(value = "检验分配")
    @PostMapping("/upInsOrder")
    public Result<?> upInsOrder(Integer orderId, Integer sampleId, String appointed, Integer userId,String sonLaboratory) {
        return Result.success(insOrderService.upInsOrder(orderId, sampleId, appointed, userId,sonLaboratory));
    }

    @ApiOperation(value = "添加检验下单数据")
    @PreAuthorize("@ss.hasPermi('add:insOrder')")
    @PostMapping("/addInsOrder")
    public Result<?> addInsOrder(@RequestBody InsPlaceOrderDto insPlaceOrderDto) {
        return Result.success(insOrderService.addInsOrder(insPlaceOrderDto.getSampleList(), insPlaceOrderDto.getInsOrder()));
    }

    @ApiOperation(value = "查询订单最长预计时间")
    @GetMapping("/selectOrderManDay")
    public Result<?> selectOrderManDay(Integer id) {
        int day = insProductService.selectOrderManDay(id);
        return Result.success(LocalDateTime.now().plusHours(day).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @ApiOperation(value = "查询检验下单内容详情")
    @GetMapping("/getInsOrder")
    public Result<?> getInsOrder(Integer orderId) {
        return Result.success(insOrderService.getInsOrder(orderId));
    }

    @ApiOperation(value = "审核检验单进行状态修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "检验单id", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "state", value = "审核结果 1:通过 2：不通过", dataTypeClass = Integer.class)
    })
    @PostMapping("/upInsOrderOfState")
    public Result<?> upInsOrderOfState(@RequestBody InsOrder insOrder) {
        return Result.success(insOrderService.upInsOrderOfState(insOrder));
    }

    @ApiOperation(value = "添加检验单模板")
    @PostMapping("/addInsOrderTemplate")
    public Result<?> addInsOrderTemplate(@RequestBody InsOrderTemplate insOrderTemplate) {
        return Result.success(insOrderTemplateService.addInsOrderTemplate(insOrderTemplate));
    }

    @ApiOperation(value = "查询检验单模板")
    @GetMapping("/selectInsOrderTemplate")
    public Result<?> selectInsOrderTemplate(String company) {
        return Result.success(insOrderTemplateService.selectInsOrderTemplate(company));
    }

    @ApiOperation(value = "通过检验单模板id获取检验单模板内容")
    @GetMapping("/selectInsOrderTemplateById")
    public Result<?> selectInsOrderTemplateById(Integer id) {
        return Result.success(insOrderTemplateService.selectInsOrderTemplateById(id));
    }

    @ApiOperation(value = "删除检验单模板")
    @DeleteMapping("/delInsOrderTemplate")
    public Result<?> delInsOrderTemplate(Integer id) {
        return Result.success(insOrderTemplateService.delInsOrderTemplate(id));
    }

    @ApiOperation(value = "通过检验单查询检验数据（数据查看）")
    @GetMapping("/selectSampleAndProductByOrderId")
    public Result<?> selectSampleAndProductByOrderId(Page page, SampleProductDto2 sampleProductDto){
        return Result.success(insOrderService.selectSampleAndProductByOrderId(page, sampleProductDto));
    }


    @ApiOperation(value = "撤销")
    @PostMapping("/updateStatus")
    public Result<?> updateStatus(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        insOrderService.updateStatus(id);
        return Result.success();
    }

    //待检的撤销的查询待检项目
    @GetMapping("/selectNoProducts")
    public Result<?> selectNoProducts(Page page, InsProduct insProduct,Integer orderId ,String ids){
        return Result.success(insProductService.selectNoProducts(page, insProduct,orderId,ids));
    }


    @ApiOperation(value = "待检的撤销")
    @PostMapping("/updateInspected")
    public Result<?> updateInspected(@RequestBody Map<String, Object> param){
        Integer orderId = (Integer) param.get("orderId");
        String ids = (String) param.get("ids");
        Integer typeSource = (Integer) param.get("typeSource");
        Integer ifsInventoryId = (Integer) param.get("ifsInventoryId");
        if(Objects.nonNull(typeSource) && typeSource == 1){
            ifsInventoryQuantityMapper.update(null,new LambdaUpdateWrapper<IfsInventoryQuantity>()
                    .set(IfsInventoryQuantity::getState,0)
                    .eq(IfsInventoryQuantity::getId,ifsInventoryId));
            insOrderService.update(null,new LambdaUpdateWrapper<InsOrder>().set(InsOrder::getState,-1).eq(InsOrder::getId,orderId)); // 撤销
        }
        insProductService.updateInspected(orderId,ids);
        return Result.success();
    }

    @ApiOperation(value = "审核待检撤销")
    @PostMapping("/checkUpdate")
    public Result<?> checkUpdate(@RequestBody Map<String, Object> param){
        Integer orderId = (Integer) param.get("orderId");
        Integer state = (Integer) param.get("state");
        insProductService.checkUpdate(orderId,state);
        return Result.success();
    }


    @ApiOperation(value = "获取ifs订单")
    @GetMapping("/getIfsOrder")
    public Result<?> getIfsOrder() {
        insOrderService.getIfsOrder();
        return Result.success();
    }


    @ApiOperation(value = "修改订单单号")
    @PostMapping("/updateEntrustCode")
    public Result<?> updateEntrustCode(@RequestBody InsOrder insOrder) {
        insOrderService.updateEntrustCode(insOrder);
        return Result.success();
    }

    @ApiOperation("查询不合格复测信息")
    @GetMapping("/getRetestResult")
    public Result<?> getRetestResult(Integer insProductId) {
        return Result.success(insOrderService.getRetestResult(insProductId));
    }

    @ApiOperation(value = "修改委托编号")
    @PostMapping("/updateOrderEntrustCode")
    public Result<?> updateOrderEntrustCode(@RequestBody InsOrder insOrder) {
        insOrderService.updateOrderEntrustCode(insOrder);
        return Result.success();
    }

    @ApiOperation(value = "修改检验下单内容")
    @PostMapping("/updateInsOrder")
    public Result<?> updateInsOrder(@RequestBody InsOrderUpdateDto insOrderUpdateDto) {
        return Result.success(insOrderService.updateInsOrder(insOrderUpdateDto));
    }

    @ApiOperation(value = "删除退回订单")
    @DeleteMapping("/delInsOrder")
    public Result<?> delInsOrder(Integer insOrderId) {
        // 查询订单
        InsOrder order = insOrderService.getById(insOrderId);
        if (!(order.getState().equals(2) || order.getState().equals(3))) {
            throw new ErrorException("只有退回订单才能删除");
        }

        return Result.success(insOrderService.removeById(insOrderId));
    }

    @ApiOperation(value = "成品标签打印")
    @PostMapping("/labelOrderPrinting")
    public Result<List<InsOrderPrintingVo>> labelOrderPrinting(@RequestBody Map<String, Object> param) {
        List<Integer> ids = (List<Integer>) param.get("ids");
        return Result.success(insOrderService.labelOrderPrinting(ids));
    }

    @ApiOperation(value = "根据订单id查询样品")
    @GetMapping("/getSampleByOrderId")
    public Result<List<InsSample>> getSampleByOrderId(Integer insOrderId) {
        return Result.success(insSampleService.getSampleByOrderId(insOrderId));
    }

    @ApiOperation(value = "根据样品id查询检验项树")
    @GetMapping("/getProductTreeBySampleId")
    public Result<List<StandardProductList>> getProductTreeBySampleId(Integer insSampleId) {
        return Result.success(insOrderService.getProductTreeBySampleId(insSampleId));
    }


    @ApiOperation(value = "添加遗漏的检验项")
    @PostMapping("/addOmitOrderProduct")
    public Result addOmitOrderProduct(@RequestBody OmitOrderProductDto omitOrderProductDto) {
        return Result.success(insOrderService.addOmitOrderProduct(omitOrderProductDto));
    }

    @ApiOperation(value = "成品检验单全部信息导出")
    @GetMapping("/rawAllInsOrderExport")
    public void rawAllInsOrderExport(SampleOrderDto sampleOrderDto, HttpServletResponse response){
        insOrderService.rawAllInsOrderExport(sampleOrderDto,response);
    }


    @ApiOperation(value = "修改样品型号")
    @PostMapping("/updateSampleModel")
    public Result<?> updateSampleModel(@RequestBody List<InsSample> insSampleList) {
        for (InsSample insSample : insSampleList) {
            insOrderService.updateSampleModel(insSample);
        }
        return Result.success();
    }

    @ApiOperation(value = "查询当前时间是否没有该检测项的抽样计划")
    @PostMapping("/judgeNotSpotCheckOrder")
    public Result<?> judgeNotSpotCheckOrder(@RequestBody InsPlaceOrderDto insPlaceOrderDto) {
        return insOrderService.judgeNotSpotCheckOrder(insPlaceOrderDto.getSampleList(), insPlaceOrderDto.getInsOrder());
    }

}

package com.ruoyi.inspect.controller;

import com.ruoyi.basic.dto.IfsInventoryQuantitySupplierDto;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.DataAnalysisDto;
import com.ruoyi.inspect.service.DataAnalysisService;
import com.ruoyi.inspect.vo.DeviationAnalyzeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/10/16
 */
@RequestMapping("/dataAnalysis")
@RestController
@AllArgsConstructor
@Api(tags = "数据分析")
public class DataAnalysisController {

    private DataAnalysisService dataAnalysisService;

    /**
     * 查询原材料柱状统计
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料柱状统计")
    @GetMapping("/getRawPassRateByBarChart")
    public Result getRawPassRateByBarChart(DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawPassRateByBarChart(dataAnalysisDto));
    }

    /**
     * 查询原材料柱状统计
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料合格率饼状态")
    @GetMapping("/getRawPassRateByCake")
    public Result getRawPassRateByCake(DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawPassRateByCake(dataAnalysisDto));
    }


    /**
     * 查询原材料项
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料项")
    @GetMapping("/getRawItemNames")
    public Result getRawItemNames(DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawItemNames(dataAnalysisDto));
    }

    /**
     * 查询原材料项检分析
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料项检分析")
    @PostMapping("/getRawProductAnalysis")
    public Result getRawProductAnalysis(@RequestBody DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawProductAnalysis(dataAnalysisDto));
    }

    /**
     * 查询原材料项检分析列表
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料项检分析列表")
    @PostMapping("/getRawProductAnalysisAllList")
    public Result<List<IfsInventoryQuantitySupplierDto>> getRawProductAnalysisAllList(@RequestBody DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawProductAnalysisAllList(dataAnalysisDto));
    }

    /**
     * 查询原材料项检分析列表
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料项检分析合格率")
    @PostMapping("/getRawProductAnalysisRawPass")
    public Result getRawProductAnalysisRawPass(@RequestBody DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawProductAnalysisRawPass(dataAnalysisDto));
    }

    /**
     * 查询原材料项检和厂家数据对比
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询原材料项检和厂家数据对比")
    @PostMapping("/getRawSupplierCompare")
    public Result<DeviationAnalyzeVo> getRawSupplierCompare(@RequestBody DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawSupplierCompare(dataAnalysisDto));
    }

    /**
     * 查询本月与上个月合格率对比
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询本月与上个月合格率对比")
    @GetMapping("/getRawUpMonth")
    public Result getRawUpMonth(DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getRawUpMonth(dataAnalysisDto));
    }

    /**
     * 查询检验项类型饼图
     * @param dataAnalysisDto
     * @return
     */
    @ApiOperation(value = "查询检验项类型饼图")
    @GetMapping("/getOrderTypeCookie")
    public Result getOrderTypeCookie(DataAnalysisDto dataAnalysisDto) {
        return Result.success(dataAnalysisService.getOrderTypeCookie(dataAnalysisDto));
    }

}

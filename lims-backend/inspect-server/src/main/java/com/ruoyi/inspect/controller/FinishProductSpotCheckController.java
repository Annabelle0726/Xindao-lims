package com.ruoyi.inspect.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.IfsStockQueryDTO;
import com.ruoyi.inspect.dto.SpotCheckQuarterDto;
import com.ruoyi.inspect.dto.SpotCheckYearDto;
import com.ruoyi.inspect.service.FinishProductSpotCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author zhuo
 * @Date 2024/9/29
 */
@RequestMapping("/finishProductSpotCheck")
@RestController
@AllArgsConstructor
@Api(tags = "成品抽样")
public class FinishProductSpotCheckController {

    private FinishProductSpotCheckService finishProductSpotCheckService;

    /**
     * 查询ifs库存数量
     *
     * @param ifsStockQueryDTO
     * @return
     */
    @ApiOperation(value = "查询ifs成品库存")
    @PostMapping("/getIfsStock")
    public Result getIfsStock(@RequestBody IfsStockQueryDTO ifsStockQueryDTO) {
        return finishProductSpotCheckService.getIfsStockReport(ifsStockQueryDTO);
    }

    /********************************************************  季度抽样  ********************************************************/

    /**
     * 新增季度抽检
     *
     * @param spotCheckQuarterDto
     * @return
     */
    @ApiOperation(value = "新增季度抽检")
    @PostMapping("/addQuarter")
    public Result addQuarter(@RequestBody SpotCheckQuarterDto spotCheckQuarterDto) {
        return Result.success(finishProductSpotCheckService.addQuarter(spotCheckQuarterDto));
    }

    /**
     * 查询季度抽样详情
     *
     * @param quarterId
     * @return
     */
    @ApiOperation(value = "查询季度抽样详情")
    @GetMapping("/getQuarter")
    public Result getQuarter(Integer quarterId) {
        return Result.success(finishProductSpotCheckService.getQuarter(quarterId));
    }

    /**
     * 季度抽样列表
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "季度抽样列表")
    @GetMapping("/getQuarterPage")
    public Result getQuarterPage(Page page, SpotCheckQuarterDto spotCheckQuarter) throws Exception {
        return Result.success(finishProductSpotCheckService.getQuarterPage(page, spotCheckQuarter));
    }

    /**
     * 新增季度抽检
     *
     * @param quarterId
     * @return
     */
    @ApiOperation(value = "删除季度检验")
    @DeleteMapping("/deleteQuarter")
    public Result deleteQuarter(Integer quarterId) {
        return Result.success(finishProductSpotCheckService.deleteQuarter(quarterId));
    }

    /**
     * 成品下单界面查询季度信息
     *
     * @return
     */
    @ApiOperation(value = "成品下单界面查询季度信息")
    @GetMapping("/getQuarterOnOrder")
    public Result getQuarterOnOrder() {
        return Result.success(finishProductSpotCheckService.getQuarterOnOrder());
    }

    /**
     * 成品下单界面查询季度信息
     *
     * @return
     */
    @ApiOperation(value = "修改季度检验")
    @PostMapping("/updateQuarterOnOrder")
    public Result updateQuarterOnOrder(@RequestBody SpotCheckQuarterDto spotCheckQuarterDto) {
        return Result.success(finishProductSpotCheckService.updateQuarterOnOrder(spotCheckQuarterDto));
    }

    /**
     * 成品下单界面查询季度信息
     *
     * @return
     */
    @ApiOperation(value = "提交最终季度报告")
    @GetMapping("/finalReportQuarter")
    public void finalReportQuarter(Integer quarterId, HttpServletResponse response) {
        finishProductSpotCheckService.finalReportQuarter(quarterId, response);
    }





    /********************************************************  年度抽样  ********************************************************/

    /**
     * 新增年度抽样
     *
     * @param spotCheckYearDto
     * @return
     */
    @ApiOperation(value = "新增年度抽样")
    @PostMapping("/addSpotCheckYear")
    public Result addSpotCheckYear(@RequestBody SpotCheckYearDto spotCheckYearDto) {
        return Result.success(finishProductSpotCheckService.addSpotCheckYear(spotCheckYearDto));
    }

    /**
     * 查询季度抽样详情
     *
     * @param yearId
     * @return
     */
    @ApiOperation(value = "查询年度抽样详情")
    @GetMapping("/getSpotCheckYear")
    public Result getSpotCheckYear(Integer yearId) {
        return Result.success(finishProductSpotCheckService.getSpotCheckYear(yearId));
    }

    /**
     * 季度抽样列表
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "年度抽样列表列表")
    @GetMapping("/getSpotCheckYearPage")
    public Result getSpotCheckYearPage(Page page, SpotCheckYearDto spotCheckYear) throws Exception {
        return Result.success(finishProductSpotCheckService.getSpotCheckYearPage(page, spotCheckYear));
    }

    /**
     * 删除年度抽样
     *
     * @param yearId
     * @return
     */
    @ApiOperation(value = "删除年度抽样")
    @DeleteMapping("/deleteSpotCheckYear")
    public Result deleteSpotCheckYear(Integer yearId) {
        return Result.success(finishProductSpotCheckService.deleteSpotCheckYear(yearId));
    }

    /**
     * 成品下单界面查询季度信息
     *
     * @return
     */
    @ApiOperation(value = "修改年度检验")
    @PostMapping("/updateSpotCheckYear")
    public Result updateSpotCheckYear(@RequestBody SpotCheckYearDto spotCheckYearDto) {
        return Result.success(finishProductSpotCheckService.updateSpotCheckYear(spotCheckYearDto));
    }

    /**
     * 成品下单界面查询季度信息
     *
     * @return
     */
    @ApiOperation(value = "生成最终年度报告")
    @GetMapping("/finalReportSpotCheckYear")
    public Result finalReportSpotCheckYear(Integer yearId, HttpServletResponse response) {
        return Result.success(finishProductSpotCheckService.finalReportSpotCheckYear(yearId, response));
    }

}

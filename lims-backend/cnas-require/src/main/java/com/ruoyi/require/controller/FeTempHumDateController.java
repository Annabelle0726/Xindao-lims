package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.dto.FeTempHumDateDto;
import com.ruoyi.require.dto.FeTempHumRecordDto;
import com.ruoyi.require.pojo.FeTempHumDate;
import com.ruoyi.require.pojo.FeTempHumRecord;
import com.ruoyi.require.service.FeTempHumDateService;
import com.ruoyi.require.service.FeTempHumRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度 区域 -父 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-09 11:02:18
 */
@Api(tags = "温湿度记录")
@RestController
@RequestMapping("/feTempHumDate")
public class FeTempHumDateController {

    @Autowired
    private FeTempHumDateService feTempHumDateService;

    @Autowired
    private FeTempHumRecordService feTempHumRecordService;

    @PostMapping("addFeTempHumDate")
    @ApiOperation("试验区域-新增/修改")
    public Result<?> addFeTempHumDate(@RequestBody FeTempHumDate feTempHumDate) {
        feTempHumDateService.saveOrUpdate(feTempHumDate);
        return Result.success();
    }

    @DeleteMapping("deleteFeTempHumDate")
    @ApiOperation("试验区域-删除")
    public Result<?> deleteFeTempHumDate(
            @RequestParam("dateId") Integer dateId) {
        feTempHumDateService.removeById(dateId);
        return Result.success();
    }

    @GetMapping("getFeTempHumDate")
    @ApiOperation("试验区域-查询")
    public Result<IPage<FeTempHumDateDto>> getFeTempHumDate(Page page, FeTempHumDateDto feTempHumDateDto) {
        IPage<FeTempHumDateDto> page1 = feTempHumDateService.getFeTempHumDate(page, feTempHumDateDto);
        return Result.success(page1);
    }

    @PostMapping("addFeTempHumRecord")
    @ApiOperation("温湿度记录新增/修改")
    public Result<FeTempHumRecord> addFeTempHumRecord(@RequestBody FeTempHumRecord feTempHumRecord) {
        if (feTempHumRecord.getTempHumId() == null) {
            feTempHumRecordService.save(feTempHumRecord);
        } else {
            feTempHumRecordService.update(Wrappers.<FeTempHumRecord>lambdaUpdate()
                    .eq(FeTempHumRecord::getTempHumId, feTempHumRecord.getTempHumId())
                    .set(FeTempHumRecord::getMorningTestTime, feTempHumRecord.getMorningTestTime())
                    .set(FeTempHumRecord::getMorningTemp, feTempHumRecord.getMorningTemp())
                    .set(FeTempHumRecord::getMorningHum, feTempHumRecord.getMorningHum())
                    .set(FeTempHumRecord::getMorningRecorderId, feTempHumRecord.getMorningRecorderId())
                    .set(FeTempHumRecord::getAfternoonTime, feTempHumRecord.getAfternoonTime())
                    .set(FeTempHumRecord::getAfternoonTemp, feTempHumRecord.getAfternoonTemp())
                    .set(FeTempHumRecord::getAfternoonHum, feTempHumRecord.getAfternoonHum())
                    .set(FeTempHumRecord::getAfternoonRecorderId, feTempHumRecord.getAfternoonRecorderId())
            );
        }
        return Result.success();
    }

    @DeleteMapping("deleteFeTempHumRecord")
    @ApiOperation("温湿度记录删除")
    public Result<?> deleteFeTempHumRecord(
            @RequestParam("tempHumId") Integer tempHumId) {
        feTempHumRecordService.removeById(tempHumId);
        return Result.success();
    }

    @GetMapping("getFeTempHumRecordPage")
    @ApiOperation("温湿度记录查询")
    public Result<IPage<FeTempHumRecordDto>> getFeTempHumRecordPage(Page page, Integer dateId) {
        IPage<FeTempHumRecordDto> page1 = feTempHumRecordService.getFeTempHumRecordPage(page, dateId);
        return Result.success(page1);
    }

    /**
     * 温湿度记录导出
     * @return
     */
    @ApiOperation(value = "导出")
    @GetMapping("/exportTemperatureAndHumidityRecords")
    public void exportTemperatureAndHumidityRecords(Integer dateId, HttpServletResponse response){
        feTempHumDateService.exportTemperatureAndHumidityRecords(dateId, response);
    }

    /**
     * 温湿度确认
     * @param feTempHumDate
     * @return
     */
    @ApiOperation("温湿度确认")
    @PostMapping("affirmFeTempHumDate")
    public Result<?> affirmFeTempHumDate(@RequestBody FeTempHumDate feTempHumDate) {
        feTempHumDateService.affirmFeTempHumDate(feTempHumDate);
        return Result.success();
    }
}

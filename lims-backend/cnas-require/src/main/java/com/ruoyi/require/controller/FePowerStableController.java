package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.dto.FePowerStableAddDto;
import com.ruoyi.require.dto.FePowerStableDto;
import com.ruoyi.require.pojo.FeMeasuredQuantity;
import com.ruoyi.require.pojo.FePowerStable;
import com.ruoyi.require.service.FeMeasuredQuantityService;
import com.ruoyi.require.service.FePowerStableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-电源稳定性 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:52
 */
@Api(tags = "设施和环境条件-设施和环境条件要求-电源稳定性")
@RestController
@RequestMapping("/fePowerStable")
public class FePowerStableController {

    @Autowired
    private FeMeasuredQuantityService feMeasuredQuantityService;

    @Autowired
    private FePowerStableService fePowerStableService;

    @PostMapping("addLaboratoryFacilityPowerStable")
    @ApiOperation("电源稳定性新增/修改")
    public Result<?> addLaboratoryFacilityPowerStable(@RequestBody FePowerStableAddDto fePowerStableAddDto) {
        FePowerStable fePowerStable = new FePowerStable();
        BeanUtils.copyProperties(fePowerStableAddDto, fePowerStable);
        fePowerStableService.saveOrUpdate(fePowerStable);
        if (CollectionUtils.isNotEmpty(fePowerStableAddDto.getFeMeasuredQuantityList())) {
            fePowerStableAddDto.getFeMeasuredQuantityList().forEach(i -> i.setPowerStableId(fePowerStable.getPowerStableId()));
            feMeasuredQuantityService.saveOrUpdateBatch(fePowerStableAddDto.getFeMeasuredQuantityList());
        }
        return Result.success();
    }

    @DeleteMapping("deleteLaboratoryFacilityPowerStable")
    @ApiOperation("电源稳定性删除")
    public Result<FePowerStable> deleteLaboratoryFacilityPowerStable(@RequestParam("powerStableId") Integer powerStableId) {
        fePowerStableService.removeById(powerStableId);
        feMeasuredQuantityService.remove(Wrappers.<FeMeasuredQuantity>lambdaQuery()
                .eq(FeMeasuredQuantity::getPowerStableId, powerStableId));
        return Result.success();
    }

    @GetMapping("getLaboratoryFacilityPowerStablePage")
    @ApiOperation("电源稳定性查询")
    public Result<IPage<FePowerStableDto>> getLaboratoryFacilityPowerStablePage(
            Page page) {
        IPage<FePowerStableDto> page1 = fePowerStableService.getLaboratoryFacilityPowerStablePage(page);
        return Result.success(page1);
    }

    @ApiOperation("选择设备后查询最新设备编号，校准日期")
    @GetMapping("getCalibrationDate")
    public Result<?> getCalibrationDate(@RequestParam("deviceId") Integer deviceId) {
        return Result.success(fePowerStableService.getCalibrationDate(deviceId));
    }

    @DeleteMapping("deleteFeMeasuredQuantity")
    @ApiOperation("电源稳定性-测定量 删除")
    public Result<?> deleteFeMeasuredQuantity(@RequestParam("measuredQuantityId") Integer measuredQuantityId) {
        return Result.success(feMeasuredQuantityService.removeById(measuredQuantityId));
    }

    @GetMapping("getFeMeasuredQuantityService")
    @ApiOperation("电源稳定性-测定量 根据电源稳定性查询")
    public Result<?> getFeMeasuredQuantityService(@RequestParam("powerStableId") Integer powerStableId) {
        return Result.success(feMeasuredQuantityService.list(Wrappers.<FeMeasuredQuantity>lambdaQuery()
                .eq(FeMeasuredQuantity::getPowerStableId, powerStableId)));
    }

    /**
     * 导出电源稳定性
     * @return
     */
    @ApiOperation(value = "导出电源稳定性")
    @GetMapping("/exportFePowerStable")
    public void exportFePowerStable(Integer powerStableId, HttpServletResponse response){
        fePowerStableService.exportFePowerStable(powerStableId, response);
    }
}

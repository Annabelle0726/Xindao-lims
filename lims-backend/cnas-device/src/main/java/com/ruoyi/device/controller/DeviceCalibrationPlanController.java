package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceCalibrationPlanDto;
import com.ruoyi.device.pojo.DeviceCalibrationPlan;
import com.ruoyi.device.pojo.DeviceCalibrationPlanDetail;
import com.ruoyi.device.service.DeviceCalibrationPlanDetailService;
import com.ruoyi.device.service.DeviceCalibrationPlanService;
import com.ruoyi.framework.exception.ErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 设备校准计划主表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 03:58:17
 */
@Api(tags = "设备校准计划")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceCalibrationPlan")
public class DeviceCalibrationPlanController {

    private DeviceCalibrationPlanService deviceCalibrationPlanService;

    private DeviceCalibrationPlanDetailService deviceCalibrationPlanDetailService;

    /**
     * 新增设备校准计划
     * @return
     */
    @ApiOperation(value = "新增设备校准计划")
    @PostMapping("/addDeviceCalibrationPlan")
    public Result addDeviceCalibrationPlan(@RequestBody DeviceCalibrationPlanDto calibrationPlanDto){
        return Result.success(deviceCalibrationPlanService.addDeviceCalibrationPlan(calibrationPlanDto));
    }

    /**
     * 修改设备校准计划
     * @param calibrationPlanDto 设备校准计划
     */
    @ApiOperation("批量修改设备校准计划")
    @PostMapping("/updateDeviceCalibrationPlan")
    public Result updateDeviceCalibrationPlan(@RequestBody DeviceCalibrationPlanDto calibrationPlanDto) {
        return Result.success(deviceCalibrationPlanService.updateDeviceCalibrationPlan(calibrationPlanDto));
    }

    /**
     * 查询设备校准计划详情
     */
    @ApiOperation("查询设备校准计划详情")
    @GetMapping("/getDeviceCalibrationPlan")
    public Result<DeviceCalibrationPlanDto> getDeviceCalibrationPlan(Integer planId) {
        return Result.success(deviceCalibrationPlanService.getDeviceCalibrationPlan(planId));
    }

    /**
     * 导入设备校准计划
     * @return
     */
    @ApiOperation(value = "导入设备校准计划")
    @PostMapping("/importDeviceCalibrationPlan")
    public Result importDeviceCalibrationPlan(MultipartFile file, String planYear){
        return Result.success(deviceCalibrationPlanService.importDeviceCalibrationPlan(file, planYear));
    }


    /**
     * 设备校准计划删除
     * @return
     */
    @ApiOperation(value = "设备校准计划删除")
    @DeleteMapping("/delDeviceCalibrationPlan")
    public Result delDeviceCalibrationPlan(Integer planId){
        return Result.success(deviceCalibrationPlanService.removeById(planId));
    }

    /**
     * 提交批准
     * @return
     */
    @ApiOperation(value = "提交批准")
    @PostMapping("/submiatRatifyDeviceCalibrationPlan")
    public Result submiatRatifyDeviceCalibrationPlan(@RequestBody DeviceCalibrationPlan DeviceCalibrationPlan){
        return Result.success(deviceCalibrationPlanService.submiatRatifyDeviceCalibrationPlan(DeviceCalibrationPlan));
    }

    /**
     * 设备校准计划批准
     * @return
     */
    @ApiOperation(value = "设备校准计划批准")
    @PostMapping("/ratifyDeviceCalibrationPlan")
    public Result ratifyDeviceCalibrationPlan(@RequestBody DeviceCalibrationPlan DeviceCalibrationPlan){
        return Result.success(deviceCalibrationPlanService.ratifyDeviceCalibrationPlan(DeviceCalibrationPlan));
    }


    /**
     * 设备校准计划列表
     * @return
     */
    @ApiOperation(value = "设备校准计划列表")
    @GetMapping("/pageDeviceCalibrationPlan")
    public Result<IPage<DeviceCalibrationPlanDto>> pageDeviceCalibrationPlan(Page page, DeviceCalibrationPlan DeviceCalibrationPlan) throws Exception {
        return Result.success(deviceCalibrationPlanService.pageDeviceCalibrationPlan(page, DeviceCalibrationPlan));
    }

    /**
     * 设备校准计划详情列表
     * @return
     */
    @ApiOperation(value = "设备校准计划详情列表")
    @GetMapping("/pageDeviceCalibrationPlanDetail")
    public Result<IPage<DeviceCalibrationPlanDetail>> pageDeviceCalibrationPlanDetail(Page page, DeviceCalibrationPlanDetail deviceCalibrationPlanDetails) {
        return Result.success(deviceCalibrationPlanService.pageDeviceCalibrationPlanDetail(page, deviceCalibrationPlanDetails));
    }

    /**
     * 新增设备校准计划详情
     * @return
     */
    @ApiOperation(value = "新增设备校准计划详情")
    @PostMapping("/addDeviceCalibrationPlanDetail")
    public Result addDeviceCalibrationPlanDetail(@RequestBody DeviceCalibrationPlanDetail deviceCalibrationPlanDetail){
        if (deviceCalibrationPlanDetail.getPlanId() == null) {
            throw new ErrorException("缺少设备校准计划主表id");
        }
        return Result.success(deviceCalibrationPlanDetailService.save(deviceCalibrationPlanDetail));
    }

    /**
     * 修改设备校准计划详情
     * @return
     */
    @ApiOperation(value = "修改设备校准计划详情")
    @PostMapping("/updateDeviceCalibrationPlanDetail")
    public Result updateDeviceCalibrationPlanDetail(@RequestBody DeviceCalibrationPlanDetail deviceCalibrationPlanDetail){
        return Result.success(deviceCalibrationPlanDetailService.updateById(deviceCalibrationPlanDetail));
    }

    /**
     * 删除设备校准计划详情
     * @return
     */
    @ApiOperation(value = "删除设备校准计划详情")
    @DeleteMapping("/delDeviceCalibrationPlanDetail")
    public Result delDeviceCalibrationPlanDetail(Integer planDetailsId){
        return Result.success(deviceCalibrationPlanDetailService.removeById(planDetailsId));
    }

    /**
     * 导出设备校准计划
     * @return
     */
    @ApiOperation(value = "导出设备校准计划")
    @GetMapping("/exportDeviceCalibrationPlanDetail")
    public void exportDeviceCalibrationPlanDetail(Integer planId, HttpServletResponse response){
        deviceCalibrationPlanService.exportDeviceCalibrationPlanDetail(planId, response);
    }

}

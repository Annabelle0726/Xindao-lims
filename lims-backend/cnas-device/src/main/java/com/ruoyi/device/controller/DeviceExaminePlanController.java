package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceExaminePlanDetailsDto;
import com.ruoyi.device.dto.DeviceExaminePlanDto;
import com.ruoyi.device.dto.DeviceExamineRecordContrastDto;
import com.ruoyi.device.dto.DeviceExamineRecordDto;
import com.ruoyi.device.pojo.DeviceExaminePlan;
import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import com.ruoyi.device.service.DeviceExaminePlanDetailsService;
import com.ruoyi.device.service.DeviceExaminePlanService;
import com.ruoyi.device.service.DeviceExamineRecordContrastService;
import com.ruoyi.device.service.DeviceExamineRecordService;
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
 * 设备核查计划主表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:04
 */
@Api(tags = "设备核查计划")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceExaminePlan")
public class DeviceExaminePlanController {

    private DeviceExaminePlanService deviceExaminePlanService;

    private DeviceExaminePlanDetailsService deviceExaminePlanDetailsService;

    private DeviceExamineRecordService deviceExamineRecordService;

    private DeviceExamineRecordContrastService deviceExamineRecordContrastService;


    /**
     * 新增设备核查计划
     * @return
     */
    @ApiOperation(value = "新增设备核查计划")
    @PostMapping("/addDeviceExaminePlan")
    public Result addDeviceExaminePlan(@RequestBody DeviceExaminePlanDto examinePlanDto){
        return Result.success(deviceExaminePlanService.addDeviceExaminePlan(examinePlanDto));
    }

    /**
     * 修改设备核查计划
     * @param examinePlanDto 设备核查计划
     */
    @ApiOperation("批量修改设备核查计划")
    @PostMapping("/updateDeviceExaminePlan")
    public Result updateDeviceExaminePlan(@RequestBody DeviceExaminePlanDto examinePlanDto) {
        return Result.success(deviceExaminePlanService.updateDeviceExaminePlan(examinePlanDto));
    }

    /**
     * 查询设备核查计划详情
     */
    @ApiOperation("查询设备核查计划详情")
    @GetMapping("/getDeviceExaminePlan")
    public Result<DeviceExaminePlanDto> getDeviceExaminePlan(Integer planId) {
        return Result.success(deviceExaminePlanService.getDeviceExaminePlan(planId));
    }

    /**
     * 导入设备核查计划
     * @return
     */
    @ApiOperation(value = "导入设备核查计划")
    @PostMapping("/importDeviceExaminePlan")
    public Result importDeviceExaminePlan(MultipartFile file){
        return Result.success(deviceExaminePlanService.importDeviceExaminePlan(file));
    }


    /**
     * 设备核查计划删除
     * @return
     */
    @ApiOperation(value = "设备核查计划删除")
    @DeleteMapping("/delDeviceExaminePlan")
    public Result delDeviceExaminePlan(Integer planId){
        return Result.success(deviceExaminePlanService.removeById(planId));
    }


    /**
     * 设备核查计划批准
     * @return
     */
    @ApiOperation(value = "提交批准")
    @PostMapping("/submitRatifyDeviceExaminePlan")
    public Result submitRatifyDeviceExaminePlan(@RequestBody DeviceExaminePlan DeviceExaminePlan){
        return Result.success(deviceExaminePlanService.submitRatifyDeviceExaminePlan(DeviceExaminePlan));
    }

    /**
     * 设备核查计划批准
     * @return
     */
    @ApiOperation(value = "设备核查计划批准")
    @PostMapping("/ratifyDeviceExaminePlan")
    public Result ratifyDeviceExaminePlan(@RequestBody DeviceExaminePlan DeviceExaminePlan){
        return Result.success(deviceExaminePlanService.ratifyDeviceExaminePlan(DeviceExaminePlan));
    }


    /**
     * 设备核查计划列表
     * @return
     */
    @ApiOperation(value = "设备核查计划列表")
    @GetMapping("/pageDeviceExaminePlan")
    public Result<IPage<DeviceExaminePlanDto>> pageDeviceExaminePlan(Page page, DeviceExaminePlan DeviceExaminePlan) {
        return Result.success(deviceExaminePlanService.pageDeviceExaminePlan(page, DeviceExaminePlan));
    }

    /**
     * 设备核查计划详情列表
     * @return
     */
    @ApiOperation(value = "设备核查计划详情列表")
    @GetMapping("/pageDeviceExaminePlanDetail")
    public Result<IPage<DeviceExaminePlanDetailsDto>> pageDeviceExaminePlanDetail(Page page, DeviceExaminePlanDetails deviceExaminePlanDetails) {
        return Result.success(deviceExaminePlanService.pageDeviceExaminePlanDetail(page, deviceExaminePlanDetails));
    }

    /**
     * 新增设备核查计划详情
     * @return
     */
    @ApiOperation(value = "新增设备核查计划详情")
    @PostMapping("/addDeviceExaminePlanDetail")
    public Result addDeviceExaminePlanDetail(@RequestBody DeviceExaminePlanDetails deviceExaminePlanDetail){
        if (deviceExaminePlanDetail.getPlanId() == null) {
            throw new ErrorException("缺少设备核查计划主表id");
        }
        return Result.success(deviceExaminePlanDetailsService.save(deviceExaminePlanDetail));
    }

    /**
     * 修改设备核查计划详情
     * @return
     */
    @ApiOperation(value = "修改设备核查计划详情")
    @PostMapping("/updateDeviceExaminePlanDetail")
    public Result updateDeviceExaminePlanDetail(@RequestBody DeviceExaminePlanDetails deviceExaminePlanDetail){
        return Result.success(deviceExaminePlanDetailsService.updateById(deviceExaminePlanDetail));
    }

    /**
     * 删除设备核查计划详情
     * @return
     */
    @ApiOperation(value = "删除设备核查计划详情")
    @DeleteMapping("/delDeviceExaminePlanDetail")
    public Result delDeviceExaminePlanDetail(Integer planDetailsId){
        return Result.success(deviceExaminePlanDetailsService.removeById(planDetailsId));
    }

    /**
     * 导出设备核查计划
     * @param planId 设备核查计划id
     * @return
     */
    @ApiOperation(value = "导出设备核查计划")
    @GetMapping("/exportDeviceExaminePlanDetail")
    public void exportDeviceExaminePlanDetail(Integer planId, HttpServletResponse response){
        deviceExaminePlanService.exportDeviceExaminePlanDetail(planId, response);
    }

    /*********************************************** 报告 **************************************************/

    /**
     * 查询核查记录
     * @return
     */
    @ApiOperation(value = "查询核查记录")
    @GetMapping("/getExamineRecord")
    public Result<DeviceExamineRecordDto> getExamineRecord(Integer planDetailsId){
        return Result.success(deviceExamineRecordService.getExamineRecord(planDetailsId));
    }

    /**
     * 新增核查记录
     * @return
     */
    @ApiOperation(value = "新增核查记录")
    @PostMapping("/addExamineRecord")
    public Result addExamineRecord(@RequestBody DeviceExamineRecordDto deviceExamineRecordDto){
        return Result.success(deviceExamineRecordService.addExamineRecord(deviceExamineRecordDto));
    }


    /**
     * 复核核查记录
     * @return
     */
    @ApiOperation(value = "复核核查记录")
    @PostMapping("/reviewExamineRecord")
    public Result reviewExamineRecord(@RequestBody DeviceExamineRecordDto deviceExamineRecordDto){
        return Result.success(deviceExamineRecordService.reviewExamineRecord(deviceExamineRecordDto));
    }

    /**
     * 导出复核核查记录
     * @param planDetailsId
     * @return
     */
    @ApiOperation(value = "导出复核核查记录")
    @GetMapping("/exportReviewExamineRecordDetail")
    public void exportReviewExamineRecordDetail(Integer planDetailsId, HttpServletResponse response){
        deviceExamineRecordService.exportReviewExamineRecordDetail(planDetailsId, response);
    }

    /*********************************************** 报告对比 **************************************************/


    /**
     * 查询核查对比记录
     * @return
     */
    @ApiOperation(value = "查询核查对比记录")
    @GetMapping("/getExamineRecordContrast")
    public Result<DeviceExamineRecordContrastDto> getExamineRecordContrast(Integer planDetailsId){
        return Result.success(deviceExamineRecordContrastService.getExamineRecordContrast(planDetailsId));
    }

    /**
     * 新增核查对比记录
     * @return
     */
    @ApiOperation(value = "新增核查对比记录")
    @PostMapping("/addExamineRecordContrast")
    public Result addExamineRecordContrast(@RequestBody DeviceExamineRecordContrastDto deviceExamineRecordContrastDto){
        return Result.success(deviceExamineRecordContrastService.addExamineRecordContrast(deviceExamineRecordContrastDto));
    }


    /**
     * 审核核查对比记录
     * @return
     */
    @ApiOperation(value = "审核核查对比记录")
    @PostMapping("/reviewExamineRecordContrast")
    public Result reviewExamineRecordContrast(@RequestBody DeviceExamineRecordContrastDto deviceExamineRecordContrastDto){
        return Result.success(deviceExamineRecordContrastService.reviewExamineRecordContrast(deviceExamineRecordContrastDto));
    }

    /**
     * 导出审核核查对比记录
     * @param planDetailsId
     * @return
     */
    @ApiOperation(value = "导出审核核查对比记录")
    @GetMapping("/exportReviewExamineRecordContrast")
    public Result exportReviewExamineRecordContrast(Integer planDetailsId, HttpServletResponse response){
        deviceExamineRecordContrastService.exportReviewExamineRecordContrast(planDetailsId, response);
        return Result.success();
    }
}

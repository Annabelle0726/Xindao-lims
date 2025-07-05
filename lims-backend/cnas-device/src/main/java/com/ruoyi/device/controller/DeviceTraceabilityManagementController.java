package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceTraceabilityManagementDto;
import com.ruoyi.device.pojo.DeviceTraceabilityManagement;
import com.ruoyi.device.service.DeviceTraceabilityManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备量值溯源计划表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:44
 */
@Api(tags = "设备量值溯源计划")
@RestController
@RequestMapping("/deviceTraceabilityManagement")
public class DeviceTraceabilityManagementController {
    @Resource
    private DeviceTraceabilityManagementService deviceTraceabilityManagementService;

    /**
     * 分页查询量值溯源计划
     * @return
     */
    @ApiOperation("分页查询量值溯源计划")
    @GetMapping("selectDeviceTraceabilityManagementByPage")
    public Result<IPage<DeviceTraceabilityManagement>> selectDeviceTraceabilityManagementByPage(Page page, DeviceTraceabilityManagementDto itemParameter){
        return deviceTraceabilityManagementService.selectDeviceTraceabilityManagementByPage(page, itemParameter);
    }

    /**
     * 新增量值溯源计划
     * @param deviceTraceabilityManagementDto 量值溯源计划
     */
    @ApiOperation("新增量值溯源计划")
    @PostMapping("/addTraceabilityManagement")
    public Result addTraceabilityManagement(@RequestBody DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        return deviceTraceabilityManagementService.addTraceabilityManagement(deviceTraceabilityManagementDto);
    }

    /**
     * 修改量值溯源计划
     * @param deviceTraceabilityManagementDto 量值溯源计划
     */
    @ApiOperation("修改量值溯源计划")
    @PostMapping("/updateTraceabilityManagement")
    public Result updateTraceabilityManagement(@RequestBody DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        return deviceTraceabilityManagementService.updateTraceabilityManagement(deviceTraceabilityManagementDto);
    }

    /**
     * 删除量值溯源计划
     * @param deviceTraceabilityManagementDto 量值溯源计划
     */
    @ApiOperation("删除量值溯源计划")
    @DeleteMapping("/deleteTraceabilityManagement")
    public Result deleteTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        return deviceTraceabilityManagementService.deleteTraceabilityManagement(deviceTraceabilityManagementDto);
    }

    /**
     * 查询量值溯源计划详情
     */
    @ApiOperation("查询量值溯源计划详情")
    @GetMapping("/getTraceabilityManagementDetail")
    public Result<DeviceTraceabilityManagementDto> getTraceabilityManagementDetail(Integer traceabilityManagementId) {
        return deviceTraceabilityManagementService.getTraceabilityManagementDetail(traceabilityManagementId);
    }

    /**
     * 提交批准
     */
    @ApiOperation("提交批准")
    @PostMapping("/submitReviewTraceabilityManagementStatus")
    public Result submitReviewTraceabilityManagementStatus(@RequestBody DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        return deviceTraceabilityManagementService.submitReviewTraceabilityManagementStatus(deviceTraceabilityManagementDto);
    }

    /**
     * 量值溯源计划批准
     */
    @ApiOperation("量值溯源计划批准")
    @PostMapping("/reviewTraceabilityManagementStatus")
    public Result reviewTraceabilityManagementStatus(@RequestBody DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        return deviceTraceabilityManagementService.reviewTraceabilityManagementStatus(deviceTraceabilityManagementDto);
    }

    /**
     * 导出量值溯源计划
     */
    @ApiOperation("导出量值溯源计划")
    @GetMapping("/exportDeviceTraceabilityManagement")
    public Result exportDeviceTraceabilityManagement(@RequestParam("traceabilityManagementId") Integer traceabilityManagementId, HttpServletResponse response) {
        return deviceTraceabilityManagementService.exportDeviceTraceabilityManagementDto(traceabilityManagementId, response);
    }
}

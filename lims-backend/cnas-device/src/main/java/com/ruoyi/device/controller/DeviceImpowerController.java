package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceImpowerDto;
import com.ruoyi.device.pojo.DeviceImpower;
import com.ruoyi.device.service.DeviceImpowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备使用授权表 前端控制器
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:23
 */
@Api(tags = "设备使用授权表")
@RestController
@RequestMapping("/deviceImpower")
public class DeviceImpowerController {

    @Resource
    private DeviceImpowerService deviceImpowerService;

    /**
     * 分页查询使用授权
     * @return
     */
    @ApiOperation("分页查询使用授权")
    @GetMapping("selectDeviceImpowerByPage")
    public Result<IPage<DeviceImpower>> selectDeviceImpowerByPage(Page page, DeviceImpowerDto itemParameter){
        return deviceImpowerService.selectDeviceImpowerByPage(page, itemParameter);
    }

    /**
     * 新增使用授权
     * @param deviceImpowerDto 使用授权
     */
    @ApiOperation("新增使用授权")
    @PostMapping("/addImpower")
    public Result addImpower(@RequestBody DeviceImpowerDto deviceImpowerDto) {
        return deviceImpowerService.addImpower(deviceImpowerDto);
    }

    /**
     * 修改使用授权
     * @param deviceImpowerDto 使用授权
     */
    @ApiOperation("修改使用授权")
    @PostMapping("/updateImpower")
    public Result updateImpower(@RequestBody DeviceImpowerDto deviceImpowerDto) {
        return deviceImpowerService.updateImpower(deviceImpowerDto);
    }

    /**
     * 删除使用授权
     * @param deviceImpowerDto 使用授权
     */
    @ApiOperation("删除使用授权")
    @DeleteMapping("/deleteImpower")
    public Result deleteImpower(DeviceImpowerDto deviceImpowerDto) {
        return deviceImpowerService.deleteImpower(deviceImpowerDto);
    }

    /**
     * 查询使用授权详情
     */
    @ApiOperation("查询使用授权详情")
    @GetMapping("/getImpowerDetail")
    public Result<DeviceImpowerDto> getImpowerDetail(Integer impowerId) {
        return deviceImpowerService.getImpowerDetail(impowerId);
    }

    /**
     * 提交批准
     */
    @ApiOperation("提交批准通知")
    @PostMapping("/submitReviewImpowerStatus")
    public Result submitReviewImpowerStatus(@RequestBody DeviceImpowerDto deviceImpowerDto) {
        return deviceImpowerService.submitReviewImpowerStatus(deviceImpowerDto);
    }

    /**
     * 使用授权批准
     */
    @ApiOperation("授权批准")
    @PostMapping("/reviewImpowerStatus")
    public Result reviewImpowerStatus(@RequestBody DeviceImpowerDto deviceImpowerDto) {
        return deviceImpowerService.reviewImpowerStatus(deviceImpowerDto);
    }

    /**
     * 导出使用授权
     */
    @ApiOperation("导出使用授权")
    @GetMapping("/exportDeviceImpower")
    public Result exportDeviceImpower(@RequestParam("impowerId") Integer impowerId, HttpServletResponse response) {
        return deviceImpowerService.exportDeviceImpowerDto(impowerId, response);
    }

}

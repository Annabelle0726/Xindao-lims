package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceExternalApply;
import com.ruoyi.device.service.DeviceExternalApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 利用外部设备申请表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 10:28:43
 */
@Api(tags = "利用外部设备申请表")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceExternalApply")
public class DeviceExternalApplyController {

    private DeviceExternalApplyService deviceExternalApplyService;


    /**
     * 利用外部设备申请列表
     * @return
     */
    @ApiOperation(value = "利用外部设备申请列表")
    @GetMapping("/pageDeviceExternalApply")
    public Result<IPage<DeviceExternalApply>> pageDeviceExternalApply(Page page, DeviceExternalApply deviceExternalApply) throws Exception {
        return Result.success(deviceExternalApplyService.pageDeviceExternalApply(page, deviceExternalApply));
    }

    /**
     * 查询利用外部设备申请
     * @return
     */
    @ApiOperation(value = "查询利用外部设备申请")
    @GetMapping("/getDeviceExternalApply")
    public Result getDeviceExternalApply(Integer externalApplyId){
        return Result.success(deviceExternalApplyService.getById(externalApplyId));
    }

    /**
     * 删除利用外部设备申请
     * @return
     */
    @ApiOperation(value = "删除利用外部设备申请")
    @DeleteMapping("/delDeviceExternalApply")
    public Result delDeviceExternalApply(Integer externalApplyId){
        return Result.success(deviceExternalApplyService.removeById(externalApplyId));
    }

    /**
     * 新增利用外部设备申请
     * @return
     */
    @ApiOperation(value = "新增利用外部设备申请")
    @PostMapping("/addDeviceExternalApply")
    public Result addDeviceExternalApply(@RequestBody DeviceExternalApply deviceExternalApply){
        return Result.success(deviceExternalApplyService.addDeviceExternalApply(deviceExternalApply));
    }

    /**
     * 导出利用外部设备申请
     * @param externalApplyId 外部设备申请id
     * @return
     */
    @ApiOperation(value = "导出利用外部设备申请")
    @GetMapping("/exportDeviceExternalApply")
    public Result exportDeviceExternalApply(Integer externalApplyId, HttpServletResponse response){
        deviceExternalApplyService.exportDeviceExternalApply(externalApplyId, response);
        return Result.success();
    }
}

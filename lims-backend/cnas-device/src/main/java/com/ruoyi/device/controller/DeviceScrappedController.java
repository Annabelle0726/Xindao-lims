package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceScrapped;
import com.ruoyi.device.service.DeviceScrappedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 设备报废申请表 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 01:53:47
 */
@Api(tags = "设备报废申请表")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceScrapped")
public class DeviceScrappedController {

    private DeviceScrappedService deviceScrappedService;


    /**
     * 设备报废申请列表
     * @return
     */
    @ApiOperation(value = "设备报废申请列表")
    @GetMapping("/pageDeviceScrapped")
    public Result<IPage<DeviceScrapped>> pageDeviceScrapped(Page page, DeviceScrapped deviceScrapped) {
        return Result.success(deviceScrappedService.pageDeviceScrapped(page, deviceScrapped));
    }

    /**
     * 查询设备报废申请
     * @return
     */
    @ApiOperation(value = "查询设备报废申请")
    @GetMapping("/getDeviceScrapped")
    public Result getDeviceScrapped(Integer scrappedId){
        return Result.success(deviceScrappedService.getById(scrappedId));
    }

    /**
     * 删除设备核查计划详情
     * @return
     */
    @ApiOperation(value = "删除设备报废申请")
    @DeleteMapping("/delDeviceScrapped")
    public Result delDeviceScrapped(Integer scrappedId){
        return Result.success(deviceScrappedService.removeById(scrappedId));
    }

    /**
     * 新增设备报废申请
     * @return
     */
    @ApiOperation(value = "新增设备报废申请")
    @PostMapping("/addDeviceScrapped")
    public Result addDeviceScrapped(@RequestBody DeviceScrapped deviceScrapped){
        return Result.success(deviceScrappedService.addDeviceScrapped(deviceScrapped));
    }

    /**
     * 导出设备报废申请
     */
    @ApiOperation("导出设备报废申请")
    @GetMapping("/exportDeviceScrapped")
    public Result exportDeviceScrapped(Integer scrappedId, HttpServletResponse response) {
        return deviceScrappedService.exportDeviceScrapped(scrappedId, response);
    }

}

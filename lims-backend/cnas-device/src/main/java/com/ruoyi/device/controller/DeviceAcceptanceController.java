package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceAcceptance;
import com.ruoyi.device.pojo.DeviceAcceptanceFile;
import com.ruoyi.device.service.DeviceAcceptanceFileService;
import com.ruoyi.device.service.DeviceAcceptanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备验收(装备) 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 01:45:14
 */
@Api(tags = "设备验收(装备)")
@AllArgsConstructor
@RestController
@RequestMapping("/deviceAcceptance")
public class DeviceAcceptanceController {

    private DeviceAcceptanceService deviceAcceptanceService;
    private DeviceAcceptanceFileService deviceAcceptanceFileService;

    /**
     * 设备验收列表
     * @return
     */
    @ApiOperation(value = "设备验收列表")
    @GetMapping("/pageDeviceAcceptance")
    public Result<IPage<DeviceAcceptance>> pageDeviceAcceptance(Page page, DeviceAcceptance deviceAcceptance) {
        return Result.success(deviceAcceptanceService.pageDeviceAcceptance(page, deviceAcceptance));
    }

    /**
     * 查询设备验收
     * @return
     */
    @ApiOperation(value = "查询设备验收")
    @GetMapping("/getDeviceAcceptance")
    public Result getDeviceAcceptance(Integer acceptanceId){
        return Result.success(deviceAcceptanceService.getById(acceptanceId));
    }

    /**
     * 删除设备验收
     * @return
     */
    @ApiOperation(value = "删除设备验收")
    @DeleteMapping("/delDeviceAcceptance")
    public Result delDeviceAcceptance(Integer acceptanceId){
        return Result.success(deviceAcceptanceService.removeById(acceptanceId));
    }

    /**
     * 新增设备验收
     * @return
     */
    @ApiOperation(value = "新增设备验收")
    @PostMapping("/addDeviceAcceptance")
    public Result addDeviceAcceptance(@RequestBody DeviceAcceptance deviceAcceptance){
        return Result.success(deviceAcceptanceService.save(deviceAcceptance));
    }

    /**
     * 新增设备验收
     * @return
     */
    @ApiOperation(value = "编辑设备验收")
    @PostMapping("/updateDeviceAcceptance")
    public Result updateDeviceAcceptance(@RequestBody DeviceAcceptance deviceAcceptance){
        return Result.success(deviceAcceptanceService.updateById(deviceAcceptance));
    }

    /**
     * 设备验收导出
     * @param acceptanceId  设备验收id
     * @param response   响应体
     * @return
     */
    @ApiOperation(value = "设备验收导出")
    @GetMapping("/exportDeviceAcceptance")
    public void exportDeviceAcceptance(Integer acceptanceId, HttpServletResponse response){
        deviceAcceptanceService.exportDeviceAcceptance(acceptanceId, response);
    }

    /**
     * 设备验收附件新增
     * @param acceptanceId
     * @param file
     * @return
     */
    @ApiOperation(value = "设备验收附件新增")
    @PostMapping("/uploadDeviceAcceptanceFile")
    public Result<?> uploadDeviceAcceptanceFile(Integer acceptanceId, MultipartFile file) {
        return Result.success(deviceAcceptanceService.uploadDeviceAcceptanceFile(acceptanceId, file));
    }


    /**
     * 设备验收附件列表
     * @return
     */
    @ApiOperation(value = "设备验收附件列表")
    @GetMapping("/getDeviceAcceptanceFileList")
    public Result<List<DeviceAcceptanceFile>> getVerifyMethodFileList(Integer acceptanceId){
        return Result.success(deviceAcceptanceFileService.list(Wrappers.<DeviceAcceptanceFile>lambdaQuery()
                .eq(DeviceAcceptanceFile::getAcceptanceId, acceptanceId)));
    }

    /**
     * 设备验收附件删除
     * @return
     */
    @ApiOperation(value = "设备验收附件删除")
    @DeleteMapping("/delDeviceAcceptanceFileList")
    public Result delDeviceAcceptanceFileList(Integer acceptanceFileId){
        return Result.success(deviceAcceptanceFileService.removeById(acceptanceFileId));
    }
}

package com.ruoyi.device.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DataConfigDto;
import com.ruoyi.device.dto.DeviceCollectionDto;
import com.ruoyi.device.dto.DeviceDto;
import com.ruoyi.device.pojo.DataConfig;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.service.DataConfigService;
import com.ruoyi.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备(DeviceController)表控制层
 */
@Api(tags = "设备")
@RestController
@RequestMapping("/deviceScope")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @Value("${file.path}")
    private String filePath;

    @Value("${wordUrl}")
    private String wordUrl;

    @Autowired
    private DataConfigService dataConfigService;

    @ApiOperation(value = "查询设备详情")
    @GetMapping("/getDeviceById")
    public Result getDeviceById(Integer deviceId){
        return Result.success(deviceService.getById(deviceId));
    }


    //设备工具明细
    @ApiOperation(value = "查询设备详情列表")
    @GetMapping("/selectDeviceParameter")
    public Result selectDeviceParameter(Page page, DeviceDto itemParameter, Boolean laboratoryNameIsNull){
        return Result.success(deviceService.selectDeviceParameter(page, itemParameter, laboratoryNameIsNull));
    }

    @ApiOperation(value = "添加设备详情参数")
    @PostMapping("/addDeviceParameter")
    public Result addDeviceParameter(@RequestBody Device itemParameter) {
        return Result.success(deviceService.addDeviceParameter(itemParameter));
    }

    @ApiOperation(value = "删除设备详情参数")
    @DeleteMapping("/delDeviceParameter")
    public Result<?> delDeviceParameter(Integer id) {
        return Result.success(deviceService.delDeviceParameter(id));
    }

    @ApiOperation(value = "修改设备详情参数")
    @PostMapping("/upDeviceParameter")
    public Result<?> upDeviceParameter(@RequestBody Device itemParameter) {
        return Result.success(deviceService.upDeviceParameter(itemParameter));
    }

    @ApiOperation(value = "获取设备总览")
    @GetMapping("/selectEquipmentOverview")
    public Result selectEquipmentOverview() {
        return Result.success(deviceService.selectEquipmentOverview());
    }

    @ApiOperation(value = "获取被授权人")
    @GetMapping("/authorizedPerson")
    public Result authorizedPerson() {
        return Result.success(deviceService.authorizedPerson());
    }

    @ApiOperation(value = "搜索")
    @GetMapping("/search")
    public Result search(Integer status, String deviceName, String specificationModel, String largeCategory) {
        return Result.success(deviceService.search(status, deviceName, specificationModel, largeCategory));
    }

    //图片上传
    @ApiOperation(value = "设备文件上传")
    @PostMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        String urlString;
        String pathName;
        String filename = file.getOriginalFilename();
        String path;
        try {
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                // 是图片
                path = filePath;
            } else {
                // 是文件
                path = wordUrl;
            }

            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "-" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            HashMap<String, String> map = new HashMap<>();
            map.put("name", filename);
            map.put("url", pathName);
            return Result.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("文件上传错误");
            return null;
        }
    }

    @ApiOperation(value = "通过项目获取设备列表")
    @GetMapping("/selectDeviceByCategory")
    public Result selectDeviceByCategory(String inspectionItem, String inspectionItemSubclass,String laboratory) {
        return Result.success(deviceService.selectDeviceByCategory(inspectionItem, inspectionItemSubclass,laboratory));
    }

    @ApiOperation(value = "通过设备编号获取设备列表")
    @GetMapping("/selectDeviceByCode")
    public Result<DeviceDto> selectDeviceByCode(Integer id) {
        return Result.success(deviceService.selectDeviceByCode(id));
    }


    @ApiOperation("/数采-数据采集")
    @PostMapping("/dataCollection")
    public Result<?> dataAcquisition(HttpServletRequest request,@RequestBody DeviceCollectionDto dto) {
        return deviceService.dataAcquisition(request, dto);
    }

    @ApiOperation(value = "维护设备文件配置")
    @PostMapping("/saveDeviceFileConfiguration")
    public Result<?> saveDeviceFileConfiguration(@RequestBody DataConfigDto dataConfigList) {
        dataConfigService.saveDeviceFileConfiguration(dataConfigList);
        return Result.success();
    }

    @ApiOperation(value = "查询数采配置")
    @GetMapping("/queryDataAcquisitionConfiguration")
    public Result<?> queryDataAcquisitionConfiguration(DataConfig dataConfig) {
        return dataConfigService.queryDataAcquisitionConfiguration(dataConfig);
    }

    @ApiOperation(value = "查询检验项数采配置")
    @GetMapping("/queryProductConfiguration")
    public Result<?> queryProductConfiguration(DataConfig dataConfig) {
        return dataConfigService.queryProductConfiguration(dataConfig);
    }

    @ApiOperation(value = "维护数采配置")
    @PostMapping("/saveDataAcquisitionConfiguration")
    public Result<?> saveDataAcquisitionConfiguration(@RequestBody DataConfigDto dataConfigList) {
        dataConfigService.saveDataAcquisitionConfiguration(dataConfigList);
        return Result.success();
    }


    @ApiOperation(value = "查询绑定了但是没有配置的检验项")
    @GetMapping("/getNoConfigProduct")
    public Result<?> getNoConfigProduct(Page page, Integer deviceId) {
        return Result.success(dataConfigService.getNoConfigProduct(page, deviceId));
    }

    @ApiOperation(value = "删除数采配置")
    @DeleteMapping("/deleteDataAcquisitionConfiguration")
    public Result<?> deleteDataAcquisitionConfiguration(@RequestParam("ids") String ids) {
        List<String> split = Arrays.asList(ids.split(","));
        List<String> collect = split.stream().distinct().collect(Collectors.toList());
        dataConfigService.removeBatchByIds(collect);
        return Result.success();
    }

    @ApiOperation(value = "左侧设备树形栏")
    @GetMapping("/treeDevice")
    public Result treeDevice(String deviceName) {
        return Result.success(deviceService.treeDevice(deviceName));
    }


    @ApiOperation(value = "设备档案导出")
    @GetMapping("/exportDeviceFile")
    public void exportDeviceFile(@RequestParam Integer deviceId, HttpServletResponse response){
        deviceService.exportDeviceFile(deviceId,response);
    }

    @ApiOperation(value = "仪器设备一览表导出")
    @GetMapping("/exportEquipmentDetails")
    public void exportEquipmentDetails(HttpServletResponse response){
        deviceService.exportEquipmentDetails(response);
    }
}

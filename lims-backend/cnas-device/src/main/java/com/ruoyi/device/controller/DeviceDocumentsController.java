package com.ruoyi.device.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceDocuments;
import com.ruoyi.device.service.DeviceDocumentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 设备档案文档
 *
 * @author zhuo
 * @since 2025-02-28
 */
@RestController
@Api(tags = "设备档案文档")
@RequestMapping("/deviceDocuments")
public class DeviceDocumentsController {

    @Resource
    private DeviceDocumentsService deviceDocumentsService;


    /**
     * 新增设备档案
     * @param document
     * @return
     */
    @ApiOperation(value = "新增设备档案")
    @PostMapping("/addDocument")
    public Result addDocument(@RequestBody DeviceDocuments document) {
        if (document.getDeviceId() == null) {
            throw new RuntimeException("设备id为空");
        }
        deviceDocumentsService.save(document);
        return Result.success();
    }

    /**
     * 查询设备档案信息
     * @param id
     * @return
     */
    @ApiOperation(value = "查询设备档案信息")
    @GetMapping("/getDocumentById")
    public Result getDocumentById(Integer id) {
        return Result.success(deviceDocumentsService.getById(id));
    }

    /**
     * 修改设备档案
     * @param document
     * @return
     */
    @ApiOperation(value = "修改设备档案")
    @PostMapping("/updateDocument")
    public Result updateDocument(@RequestBody DeviceDocuments document) {
        return Result.success(deviceDocumentsService.updateById(document));
    }

    /**
     * 删除设备档案
     * @param id
     * @return
     */
    @ApiOperation(value = "删除设备档案")
    @DeleteMapping("/deleteDocumentById")
    public Result deleteDocumentById(Integer id) {
        return Result.success(deviceDocumentsService.removeById(id));
    }

    /**
     * 查询设备档案列表
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "查询设备档案列表")
    @GetMapping("/getAllDocuments")
    public Result getAllDocuments(Integer deviceId) {
        LambdaQueryWrapper<DeviceDocuments> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeviceDocuments::getDeviceId, deviceId);
        return Result.success(deviceDocumentsService.list(lambdaQueryWrapper));
    }

}


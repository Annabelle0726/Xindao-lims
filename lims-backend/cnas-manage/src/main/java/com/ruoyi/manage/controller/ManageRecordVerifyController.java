package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordVerify;
import com.ruoyi.manage.service.ManageRecordVerifyService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 外来文件确认记录 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-12 10:29:44
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordVerify")
public class ManageRecordVerifyController {

    @Resource
    private ManageRecordVerifyService manageRecordVerifyService;


    @ApiOperation(value = "查询外来文件确认记录详情")
    @GetMapping("/pageManageRecordVerify")
    public Result pageManageRecordVerify(Page page,ManageRecordVerify manageRecordVerify) throws Exception {
        return Result.success(manageRecordVerifyService.pageManageRecordVerify(page, manageRecordVerify));
    }

    @ApiOperation(value = "新增外来文件确认记录")
    @PostMapping("/addManageRecordVerify")
    public Result addManageRecordVerify(@RequestBody ManageRecordVerify manageRecordVerify) {
        return Result.success(manageRecordVerifyService.addManageRecordVerify(manageRecordVerify));
    }

    @ApiOperation(value = "删除外来文件确认记录")
    @DeleteMapping("/delManageRecordVerify")
    public Result delManageRecordVerify(Integer id) {
        return Result.success(manageRecordVerifyService.delManageRecordVerify(id));
    }


    @ApiOperation(value = "修改外来文件确认记录")
    @PostMapping("/doManageRecordVerify")
    public Result doManageRecordVerify(@RequestBody ManageRecordVerify manageRecordVerify) {
        return Result.success(manageRecordVerifyService.updateById(manageRecordVerify));
    }

    @ApiOperation(value = "导入外来文件确认记录")
    @PostMapping("/exportManageRecordVerify")
    public Result exportManageRecordVerify(MultipartFile file) {
        return Result.success(manageRecordVerifyService.exportManageRecordVerify(file));
    }

}

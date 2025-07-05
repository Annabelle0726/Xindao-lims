package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordCheck;
import com.ruoyi.manage.service.ManageRecordCheckService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件审批记录(含修订后再次审批记录) 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-12 02:31:36
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordCheck")
public class ManageRecordCheckController {

    @Resource
    private ManageRecordCheckService manageRecordCheckService;

    @ApiOperation(value = "分页查询文件审批记录")
    @GetMapping("/pageManageRecordCheck")
    public Result pageManageRecordCheck(Page page,ManageRecordCheck manageRecordCheck) throws Exception {
        return Result.success(manageRecordCheckService.pageManageRecordCheck(page, manageRecordCheck));
    }

    @ApiOperation(value = "新增文件审批记录")
    @PostMapping("/addManageRecordCheck")
    public Result addManageRecordCheck(@RequestBody ManageRecordCheck manageRecordCheck) {
        return Result.success(manageRecordCheckService.save(manageRecordCheck));
    }


    @ApiOperation(value = "编辑文件审批记录")
    @PostMapping("/doManageRecordCheck")
    public Result doManageRecordCheck(@RequestBody ManageRecordCheck manageRecordCheck) {
        return Result.success(manageRecordCheckService.updateById(manageRecordCheck));
    }

    @ApiOperation(value = "删除文件审批记录")
    @DeleteMapping("/delManageRecordCheck")
    public Result delManageRecordCheck(Integer id) {
        return Result.success(manageRecordCheckService.removeById(id));
    }

    @ApiOperation(value = "审核文件审批记录")
    @PostMapping("/checkManageRecordCheck")
    public Result checkManageRecordCheck(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String checkState = (String) param.get("checkState");
        return Result.success(manageRecordCheckService.checkManageRecordCheck(id,checkState));
    }

    @ApiOperation(value = "批准文件审批记录")
    @PostMapping("/ratifyManageRecordCheck")
    public Result ratifyManageRecordCheck(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String ratifyState = (String) param.get("ratifyState");
        return Result.success(manageRecordCheckService.ratifyManageRecordCheck(id,ratifyState));
    }

    @ApiOperation(value = "导出文件审批记录")
    @GetMapping("/exportOutManageRecordCheck")
    public Result exportOutManageRecordCheck(ManageRecordCheck manageRecordCheck, HttpServletResponse response) throws Exception {
        return Result.success(manageRecordCheckService.exportOutManageRecordCheck(manageRecordCheck,response));
    }

    @ApiOperation(value = "导入文件审批记录")
    @PostMapping("/exportInManageRecordCheck")
    public Result exportInManageRecordCheck(MultipartFile file){
        return Result.success(manageRecordCheckService.exportInManageRecordCheck(file));
    }

}

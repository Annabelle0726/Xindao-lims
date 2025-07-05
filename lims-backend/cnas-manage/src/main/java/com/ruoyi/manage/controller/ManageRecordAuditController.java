package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordAudit;
import com.ruoyi.manage.service.ManageRecordAuditService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件修订申请审批记录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-14 10:29:18
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordAudit")
public class ManageRecordAuditController {

    @Resource
    private ManageRecordAuditService manageRecordAuditService;

    @ApiOperation(value = "分页查询文件修订申请审批记录")
    @GetMapping("/pageManageRecordAudit")
    public Result pageManageRecordAudit(Page page,ManageRecordAudit manageRecordAudit) throws Exception {
        return Result.success(manageRecordAuditService.pageManageRecordAudit(page, manageRecordAudit));
    }

    @ApiOperation(value = "新增文件修订申请审批记录")
    @PostMapping("/addManageRecordAudit")
    public Result addManageRecordAudit(ManageRecordAudit manageRecordAudit){
        return Result.success(manageRecordAuditService.addManageRecordAudit(manageRecordAudit));
    }


    @ApiOperation(value = "编辑文件修订申请审批记录")
    @PostMapping("/doManageRecordAudit")
    public Result doManageRecordAudit(ManageRecordAudit manageRecordAudit){
        return Result.success(manageRecordAuditService.doManageRecordAudit(manageRecordAudit));
    }

    @ApiOperation(value = "删除文件修订申请审批记录")
    @DeleteMapping("/delManageRecordAudit")
    public Result delManageRecordAudit(Integer id){
        return Result.success(manageRecordAuditService.removeById(id));
    }

    @ApiOperation(value = "批准文件修订申请审批记录")
    @PostMapping("/ratifyManageRecordAudit")
    public Result ratifyManageRecordAudit(@RequestBody Map<String, Integer> param){
        Integer id = param.get("id");
        return Result.success(manageRecordAuditService.ratifyManageRecordAudit(id));
    }

    @ApiOperation(value = "导出文件修订申请审批记录")
    @GetMapping("/exportOutManageRecordAudit")
    public Result exportOutManageRecordAudit(ManageRecordAudit manageRecordAudit, HttpServletResponse response) {
        return Result.success(manageRecordAuditService.exportOutManageRecordAudit(manageRecordAudit,response));
    }


}

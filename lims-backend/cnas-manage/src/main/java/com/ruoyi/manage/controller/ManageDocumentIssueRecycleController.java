package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.dto.ManageDocumentIssueRecycleDto;
import com.ruoyi.manage.pojo.ManageDocumentIssueRecycle;
import com.ruoyi.manage.service.ManageDocumentIssueRecycleService;
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
 * 文件发放回收 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-09 09:18:24
 */
@Api(tags = "管理体系文件的控制")
@RestController
@RequestMapping("/manageDocumentIssueRecycle")
public class ManageDocumentIssueRecycleController {

    @Resource
    private ManageDocumentIssueRecycleService manageDocumentIssueRecycleService;

    @ApiOperation(value = "分页查询文件发放回收")
    @GetMapping("/pageManageDocumentIssueRecycle")
    public Result pageManageDocumentIssueRecycle(Page page,ManageDocumentIssueRecycleDto  manageDocumentIssueRecycleDto) throws Exception {
        return Result.success(manageDocumentIssueRecycleService.pageManageDocumentIssueRecycle(page, manageDocumentIssueRecycleDto));
    }

    @ApiOperation(value = "新增文件发放回收")
    @PostMapping("/addManageDocumentIssueRecycle")
    public Result addManageDocumentIssueRecycle(ManageDocumentIssueRecycle manageDocumentIssueRecycle){
        return Result.success(manageDocumentIssueRecycleService.addManageDocumentIssueRecycle(manageDocumentIssueRecycle));
    }

    @ApiOperation(value = "删除文件发放回收")
    @DeleteMapping("/delManageDocumentIssueRecycle")
    public Result delManageDocumentIssueRecycle(Long id){
        return Result.success(manageDocumentIssueRecycleService.delManageDocumentIssueRecycle(id));
    }


    @ApiOperation(value = "查看文件发放回收")
    @GetMapping("/getManageDocumentIssueRecycle")
    public Result getManageDocumentIssueRecycle(Long id){
        return Result.success(manageDocumentIssueRecycleService.getManageDocumentIssueRecycle(id));
    }


    @ApiOperation(value = "编辑文件发放回收")
    @PostMapping("/doManageDocumentIssueRecycle")
    public Result doManageDocumentIssueRecycle(@RequestBody ManageDocumentIssueRecycle manageDocumentIssueRecycle){
        return Result.success(manageDocumentIssueRecycleService.doManageDocumentIssueRecycle(manageDocumentIssueRecycle));
    }


    @ApiOperation(value = "审核文件发放回收")
    @PostMapping("/checkManageDocumentIssueRecycle")
    public Result checkManageDocumentIssueRecycle(@RequestBody ManageDocumentIssueRecycle manageDocumentIssueRecycle){
        return Result.success(manageDocumentIssueRecycleService.checkManageDocumentIssueRecycle(manageDocumentIssueRecycle.getId(),manageDocumentIssueRecycle.getDocumentState()));
    }

    @ApiOperation(value = "导出文件发放回收")
    @GetMapping("/exportManageDocumentIssueRecycle")
    public void exportManageDocumentIssueRecycle(ManageDocumentIssueRecycleDto manageDocumentIssueRecycleDto, HttpServletResponse response) throws Exception {
        manageDocumentIssueRecycleService.exportManageDocumentIssueRecycle(manageDocumentIssueRecycleDto,response);
    }



}

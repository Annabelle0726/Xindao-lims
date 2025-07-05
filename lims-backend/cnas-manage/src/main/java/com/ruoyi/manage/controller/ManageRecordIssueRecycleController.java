package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordIssueRecycle;
import com.ruoyi.manage.service.ManageRecordIssueRecycleService;
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
 * 所有文件(内、外部文件)的发放与回收记录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-13 09:11:05
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordIssueRecycle")
public class ManageRecordIssueRecycleController {

    @Resource
    private ManageRecordIssueRecycleService manageRecordIssueRecycleService;

    @ApiOperation(value = "分页查询所有文件的发放与回收记录")
    @GetMapping("/pageManageRecordIssueRecycle")
    public Result pageManageRecordIssueRecycle(Page page,ManageRecordIssueRecycle manageRecordIssueRecycle) throws Exception {
        return Result.success(manageRecordIssueRecycleService.pageManageRecordIssueRecycle(page, manageRecordIssueRecycle));
    }

    @ApiOperation(value = "删除所有文件的发放与回收记录")
    @DeleteMapping("/delManageRecordIssueRecycle")
    public Result delManageRecordIssueRecycle(Integer id){
        return Result.success(manageRecordIssueRecycleService.removeById(id));
    }

    @ApiOperation(value = "新增所有文件的发放与回收记录")
    @PostMapping("/addManageRecordIssueRecycle")
    public Result addManageRecordIssueRecycle(@RequestBody ManageRecordIssueRecycle manageRecordIssueRecycle){
        return Result.success(manageRecordIssueRecycleService.addManageRecordIssueRecycle(manageRecordIssueRecycle));
    }


    @ApiOperation(value = "编辑所有文件的发放与回收记录")
    @PostMapping("/doManageRecordIssueRecycle")
    public Result doManageRecordIssueRecycle(@RequestBody ManageRecordIssueRecycle manageRecordIssueRecycle){
        return Result.success(manageRecordIssueRecycleService.doManageRecordIssueRecycle(manageRecordIssueRecycle));
    }

    @ApiOperation(value = "导出所有文件的发放与回收记录")
    @GetMapping("/exportOutManageRecordIssueRecycle")
    public Result exportOutManageRecordIssueRecycle(ManageRecordIssueRecycle manageRecordIssueRecycle, HttpServletResponse response) {
        return Result.success(manageRecordIssueRecycleService.exportOutManageRecordIssueRecycle(manageRecordIssueRecycle,response));
    }

    @ApiOperation(value = "导入所有文件的发放与回收记录")
    @PostMapping("/exportInManageRecordIssueRecycle")
    public Result exportInManageRecordIssueRecycle(MultipartFile file){
        return Result.success(manageRecordIssueRecycleService.exportInManageRecordIssueRecycle(file));
    }

}

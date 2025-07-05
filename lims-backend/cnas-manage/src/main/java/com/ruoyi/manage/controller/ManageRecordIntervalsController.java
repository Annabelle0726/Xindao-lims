package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordIntervals;
import com.ruoyi.manage.service.ManageRecordIntervalsService;
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
 * 文件定期审查记录 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-13 10:54:31
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordIntervals")
public class ManageRecordIntervalsController {

    @Resource
    private ManageRecordIntervalsService manageRecordIntervalsService;

    @ApiOperation(value = "分页查询文件定期审查记录")
    @GetMapping("/pageManageRecordIntervals")
    public Result pageManageRecordIntervals(Page page,ManageRecordIntervals manageRecordIntervals) throws Exception {
        return Result.success(manageRecordIntervalsService.pageManageRecordIntervals(page, manageRecordIntervals));
    }

    @ApiOperation(value = "新增文件定期审查记录")
    @PostMapping("/addManageRecordIntervals")
    public Result addManageRecordIntervals(@RequestBody ManageRecordIntervals manageRecordIntervals) {
        return Result.success(manageRecordIntervalsService.addManageRecordIntervals(manageRecordIntervals));
    }


    @ApiOperation(value = "编辑文件定期审查记录")
    @PostMapping("/doManageRecordIntervals")
    public Result doManageRecordIntervals(@RequestBody ManageRecordIntervals manageRecordIntervals) {
        return Result.success(manageRecordIntervalsService.updateById(manageRecordIntervals));
    }

    @ApiOperation(value = "删除文件定期审查记录")
    @DeleteMapping("/delManageRecordIntervals")
    public Result delManageRecordIntervals(Integer id) {
        return Result.success(manageRecordIntervalsService.delManageRecordIntervals(id));
    }

    @ApiOperation(value = "导出文件定期审查记录")
    @GetMapping("/exportOutManageRecordIntervals")
    public Result exportOutManageRecordIntervals(ManageRecordIntervals manageRecordIntervals, HttpServletResponse response) throws Exception {
        return Result.success(manageRecordIntervalsService.exportOutManageRecordIntervals(manageRecordIntervals,response));
    }

    @ApiOperation(value = "导入文件定期审查记录")
    @PostMapping("/exportInManageRecordIntervals")
    public Result exportInManageRecordIntervals(MultipartFile file){
        return Result.success(manageRecordIntervalsService.exportInManageRecordIntervals(file));
    }

}

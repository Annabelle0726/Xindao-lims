package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.pojo.ProcessEvaluate;
import com.ruoyi.process.service.ProcessEvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 测量不确定度的评价 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-02 01:10:43
 */
@RestController
@RequestMapping("/processEvaluate")
@Api(tags = "测量不确定度的评价")
public class ProcessEvaluateController {

    @Resource
    private ProcessEvaluateService processEvaluateService;

    @ApiOperation(value = "查询测量不确定度的评定列表")
    @GetMapping("/pageProcessEvaluate")
    public Result pageProcessEvaluate(ProcessEvaluate processEvaluate,Page page) throws Exception {
        return Result.success(processEvaluateService.pageProcessEvaluate(page, processEvaluate));
    }

    @ApiOperation(value = "新增测量不确定度的评定")
    @PostMapping("/addProcessEvaluate")
    public Result addProcessEvaluate(MultipartFile file){
        return Result.success(processEvaluateService.addProcessEvaluate(file));
    }

    @ApiOperation(value = "评价")
    @PostMapping("/doProcessEvaluate")
    public Result doProcessEvaluate(@RequestBody ProcessEvaluate processEvaluate){
        return Result.success(processEvaluateService.doProcessEvaluate(processEvaluate));
    }

    @ApiOperation(value = "删除测量不确定度的评定")
    @DeleteMapping("/delProcessEvaluate")
    public Result delProcessEvaluate(Long id){
        return Result.success(processEvaluateService.removeById(id));
    }

    @ApiOperation(value = "导出测量不确定度的评定")
    @GetMapping("/exportProcessEvaluate")
    public void exportProcessEvaluate(ProcessEvaluate processEvaluate, HttpServletResponse response) throws Exception{
        processEvaluateService.exportProcessEvaluate(processEvaluate,response);
    }

}

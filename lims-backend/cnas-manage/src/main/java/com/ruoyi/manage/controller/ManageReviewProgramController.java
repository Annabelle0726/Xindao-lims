package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.manage.pojo.ManageReviewProgram;
import com.ruoyi.manage.service.ManageReviewProgramService;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-09 03:05:42
 */
@Api(tags = "管理评审计划")
@RestController
@RequestMapping("/manageReviewProgram")
public class ManageReviewProgramController {
    @Resource
    private ManageReviewProgramService manageReviewProgramService;


    @ApiOperation(value = "查询评审计划")
    @GetMapping("/getPageReviewProgram")
    public Result<IPage<ManageReviewProgram>> getPageReviewProgram(Page page, String startTime,String endTime, String judgingLocation) {
        IPage<ManageReviewProgram> ipage = manageReviewProgramService.page(page,startTime,endTime,judgingLocation);
        return Result.success(ipage);
    }


    @ApiOperation(value = "新增评审计划")
    @PostMapping("/addReviewProgram")
    public Result addReviewProgram(@RequestBody ManageReviewProgram manageReviewProgram){
        return Result.success(manageReviewProgramService.addReviewProgram(manageReviewProgram));
    }

    @ApiOperation(value = "编辑评审计划")
    @PostMapping("/modifyReviewProgram")
    public Result modifyReviewProgram(@RequestBody ManageReviewProgram manageReviewProgram){
        return Result.success(manageReviewProgramService.updateById(manageReviewProgram));
    }

    @ApiOperation(value = "删除评审计划")
    @DeleteMapping("/deleteReviewProgram")
    public Result deleteReviewProgram( Integer id){
        return Result.success(manageReviewProgramService.removeById(id));
    }


    @ApiOperation(value = "下载评审计划")
    @GetMapping("/exportReviewProgram")
    public void exportReviewProgram(Integer id, HttpServletResponse response){
        manageReviewProgramService.exportReviewProgram(id,response);
    }



}

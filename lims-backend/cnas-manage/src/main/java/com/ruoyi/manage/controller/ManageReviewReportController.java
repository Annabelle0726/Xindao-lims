package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.manage.pojo.ManageReviewReport;
import com.ruoyi.manage.service.ManageReviewReportService;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 管理评审报告 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-12 04:44:39
 */
@Api(tags = "管理评审报告")
@RestController
@RequestMapping("/manageReviewReport")
public class ManageReviewReportController {

    @Resource
    private ManageReviewReportService manageReviewReportService;

    @ApiOperation(value = "查询管理评审报告")
    @GetMapping("/getPageReviewReport")
    public Result<IPage<ManageReviewReport>> getPageReviewReport(Page page, String startTime, String endTime, String place) {
        IPage<ManageReviewReport> ipage = manageReviewReportService.page(page,startTime,endTime,place);
        return Result.success(ipage);
    }

    @ApiOperation(value = "新增管理评审报告")
    @PostMapping("/addReviewReport")
    public Result addReviewReport(@RequestBody ManageReviewReport manageReviewReport){
        return Result.success(manageReviewReportService.save(manageReviewReport));
    }

    @ApiOperation(value = "编辑管理评审报告")
    @PostMapping("/modifyReviewReport")
    public Result modifyReviewReport(@RequestBody ManageReviewReport manageReviewReport){
        return Result.success(manageReviewReportService.updateById(manageReviewReport));
    }

    @ApiOperation(value = "删除管理评审报告")
    @DeleteMapping("/deleteReviewReport")
    public Result deleteReviewReport(Integer id){
        return Result.success(manageReviewReportService.removeById(id));
    }

    @ApiOperation(value = "下载管理评审报告")
    @GetMapping("/exportReviewReport")
    public void exportReviewReport(Integer id , HttpServletResponse response){
        manageReviewReportService.exportReviewReport(id,response);
    }
}

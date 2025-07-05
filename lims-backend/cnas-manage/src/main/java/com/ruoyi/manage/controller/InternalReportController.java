package com.ruoyi.manage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.InternalReport;
import com.ruoyi.manage.service.InternalReportService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 内审报告表
 *
 * @author makejava
 * @since 2024-11-11
 */
@Api(tags = "内审报告")
@AllArgsConstructor
@RestController
@RequestMapping("/internalReport")
public class InternalReportController {

    private InternalReportService internalReportService;

    /**
     * 内审报告分页查询
     * @param
     * @return
     */

    @ApiOperation(value = "内审报告分页查询")
    @GetMapping("/pageInternalReport")
    public Result<IPage<InternalReport>> pageInternalReport(Page page,InternalReport internalReport) throws Exception {
        return Result.success(internalReportService.pageInternalReport(page, internalReport));
    }

    /**
     * 内审报告新增
     * @return
     */

    @ApiOperation(value = "内审报告新增")
    @PostMapping("/addInternalReport")
    public Result addInternalReport(@RequestBody InternalReport internalReport){
        return Result.success(internalReportService.save(internalReport));
    }

    /**
     * 内审报告修改
     * @return
     */

    @ApiOperation(value = "内审报告修改")
    @PostMapping("/updateInternalReport")
    public Result updateInternalReport(@RequestBody InternalReport internalReport){
        return Result.success(internalReportService.updateById(internalReport));
    }

    /**
     * 内审报告删除
     * @return
     */

    @ApiOperation(value = "内审报告删除")
    @DeleteMapping("/delInternalReport")
    public Result delInternalReport(Integer reportId){
        return Result.success(internalReportService.removeById(reportId));
    }

    /**
     * 内审报告查看详情
     * @return
     */

    @ApiOperation(value = "内审报告查看详情")
    @GetMapping("/getInternalReportOne")
    public Result<InternalReport> getInternalReportOne(Integer reportId){
        return Result.success(internalReportService.getById(reportId));
    }

    /**
     * 内审检查审核
     * @return
     */
    @ApiOperation(value = "内审检查审核")
    @PostMapping("/examineInternalReport")
    public Result examineInternalReport(@RequestBody InternalReport internalReport){
        return Result.success(internalReportService.ratifyInternalCheck(internalReport));
    }

    /**
     * 内审报告质量负责人填写
     * @return
     */
    @ApiOperation(value = "内审报告质量负责人填写")
    @PostMapping("/qualityInternalReport")
    public Result qualityInternalReport(@RequestBody InternalReport internalReport){
        return Result.success(internalReportService.qualityInternalReport(internalReport));
    }

    /**
     * 导出内审报告
     * @return
     */

    @ApiOperation(value = "导出内审报告")
    @GetMapping("/exportInternalReport")
    public void exportInternalReport(Integer reportId, HttpServletResponse response){
        internalReportService.exportInternalReport(reportId, response);
    }

}


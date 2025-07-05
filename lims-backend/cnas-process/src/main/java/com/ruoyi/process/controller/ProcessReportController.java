package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.dto.ProcessReportDto;
import com.ruoyi.process.pojo.ProcessReport;
import com.ruoyi.process.service.ProcessReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 检验报告发放登记表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-05 08:58:39
 */
@RestController
@RequestMapping("/processReport")
@Api(tags = "检验报告发放登记表")
public class ProcessReportController {

    @Resource
    private ProcessReportService processReportService;

    @ApiOperation(value = "查询检验报告发放登记列表")
    @GetMapping("/pageProcessReport")
    public Result pageProcessReport(ProcessReportDto processReport , Page page) {
        return Result.success(processReportService.pageProcessReport(page, processReport));
    }

    @ApiOperation(value = "新增检验报告发放登记")
    @PostMapping("/addProcessReport")
    public Result  addProcessReport(@RequestBody ProcessReport processReport) {
        return Result.success(processReportService.save(processReport));
    }

    @ApiOperation(value = "删除检验报告发放登记")
    @DeleteMapping("/delProcessReport")
    public Result delProcessReport(Long id) {
        return Result.success(processReportService.removeById(id));
    }

    @ApiOperation(value = "查看检验报告发放登记详情")
    @GetMapping("/getProcessReport")
    public Result getProcessReport(Long id)  {
        return Result.success(processReportService.getById(id));
    }

    @ApiOperation(value = "修改检验报告发放登记")
    @PostMapping("/doProcessReport")
    public Result doProcessReport(@RequestBody ProcessReport processReport) {
        return Result.success(processReportService.updateById(processReport));
    }

    @ApiOperation(value = "导出检验报告发放登记表")
    @GetMapping("/exportProcessReport")
    public void exportProcessReport(ProcessReportDto dto, HttpServletResponse response) {
        processReportService.exportProcessReport(dto, response);
    }

}

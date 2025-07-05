package com.ruoyi.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.process.dto.QualityMonitorDetailsDto;
import com.ruoyi.process.dto.QualityMonitorDto;
import com.ruoyi.process.mapper.QualityMonitorDetailsEvaluateFileMapper;
import com.ruoyi.process.pojo.*;
import com.ruoyi.process.service.QualityMonitorDetailsService;
import com.ruoyi.process.service.QualityMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 质量监控计划主表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Api(tags = "质量监控计划")
@AllArgsConstructor
@RestController
@RequestMapping("/qualityMonitor")
public class QualityMonitorController {

    @Resource
    private QualityMonitorService qualityMonitorService;

    @Resource
    private QualityMonitorDetailsService qualityMonitorDetailsService;

    @Resource
    private QualityMonitorDetailsEvaluateFileMapper qualityMonitorDetailsEvaluateFileMapper;

    /**
     * 导入监控计划
     * @return
     */
    @ApiOperation(value = "导入监控计划")
    @PostMapping("/importQualityMonitor")
    public Result importQualityMonitor(MultipartFile file, QualityMonitor qualityMonitor) {
        return Result.success(qualityMonitorService.importQualityMonitor(file, qualityMonitor));
    }

    /**
     * 监控计划审核
     * @return
     */
    @ApiOperation(value = "监控计划审核")
    @PostMapping("/examineQualityMonitor")
    public Result examineQualityMonitor(@RequestBody QualityMonitor qualityMonitor) {
        return Result.success(qualityMonitorService.examineQualityMonitor(qualityMonitor));
    }

    /**
     * 监控计划删除
     * @return
     */
    @ApiOperation(value = "监控计划删除")
    @DeleteMapping("/delQualitySupervise")
    public Result delQualitySupervise(Integer qualityMonitorId) {
        return Result.success(qualityMonitorService.removeById(qualityMonitorId));
    }

    /**
     * 监控计划批准
     * @return
     */
    @ApiOperation(value = "监控计划批准")
    @PostMapping("/ratifyQualityMonitor")
    public Result ratifyQualityMonitor(@RequestBody QualityMonitor qualityMonitor) {
        return Result.success(qualityMonitorService.ratifyQualityMonitor(qualityMonitor));
    }


    /**
     * 监控计划列表
     * @return
     */
    @ApiOperation(value = "监控计划列表")
    @GetMapping("/pageQualityMonitor")
    public Result<IPage<QualityMonitorDto>> pageQualityMonitor(QualityMonitor qualityMonitor,Page page) throws Exception {
        return Result.success(qualityMonitorService.pageQualityMonitor(page, qualityMonitor));
    }

    /**
     * 监控计划详情列表
     * @param data
     * @return
     */
    @ApiOperation(value = "监控计划详情列表")
    @GetMapping("/pageQualityMonitorDetail")
    public Result<IPage<QualityMonitorDetailsDto>> pageQualityMonitorDetail(QualityMonitorDetails qualityMonitorDetails, Page page) throws Exception {
        return Result.success(qualityMonitorService.pageQualityMonitorDetail(page, qualityMonitorDetails));
    }

    /**
     * 新增监控计划详情
     * @return
     */
    @ApiOperation(value = "新增监控计划详情")
    @PostMapping("/addQualityMonitorDetail")
    public Result addQualityMonitorDetail(@RequestBody QualityMonitorDetails qualityMonitorDetails) {
        if (qualityMonitorDetails.getQualityMonitorId() == null) {
            throw new ErrorException("缺少监控计划主表id");
        }
        return Result.success(qualityMonitorDetailsService.save(qualityMonitorDetails));
    }

    /**
     * 修改监控计划详情
     * @return
     */
    @ApiOperation(value = "修改监控计划详情")
    @PostMapping("/updateQualityMonitorDetail")
    public Result updateQualityMonitorDetail(@RequestBody QualityMonitorDetails qualityMonitorDetails) {
        return Result.success(qualityMonitorDetailsService.updateById(qualityMonitorDetails));
    }

    /**
     * 删除监控计划详情
     * @return
     */
    @ApiOperation(value = "删除监控计划详情")
    @DeleteMapping("/delQualityMonitorDetail")
    public Result delQualityMonitorDetail(Integer qualityMonitorDetailsId) {
        return Result.success(qualityMonitorDetailsService.removeById(qualityMonitorDetailsId));
    }

    /**
     * 导出监控计划
     * @return
     */
    @ApiOperation(value = "导出监控计划")
    @GetMapping("/exportQualityMonitorDetail")
    public void exportQualityMonitorDetail(Integer qualityMonitorId, HttpServletResponse response) {
        qualityMonitorService.exportQualityMonitorDetail(qualityMonitorId, response);
    }


    /************************************************************  实施  *******************************************************************/

    /**
     * 查询监控计划详情实施信息
     * @return
     */
    @ApiOperation(value = "查询监控计划详情实施信息")
    @GetMapping("/getQualityMonitorRatify")
    public Result<QualityMonitorDetailsRatify> getQualityMonitorRatify(Integer qualityMonitorDetailsId) {
        return Result.success(qualityMonitorService.getQualityMonitorRatify(qualityMonitorDetailsId));
    }

    /**
     * 新增监控计划详情实施信息
     * @return
     */
    @ApiOperation(value = "新增监控计划详情实施信息")
    @PostMapping("/addQualityMonitorRatify")
    public Result addQualityMonitorRatify(@RequestBody QualityMonitorDetailsRatify qualityMonitorDetailsRatify) {
        return Result.success(qualityMonitorService.addQualityMonitorRatify(qualityMonitorDetailsRatify));
    }

    /**
     * 监控计划详情提交批准意见
     * @return
     */
    @ApiOperation(value = "监控计划详情提交批准意见")
    @PostMapping("/addQualityMonitorRatifyOpinion")
    public Result addQualityMonitorRatifyOpinion(@RequestBody QualityMonitorDetailsRatify qualityMonitorDetailsRatify) {
        return Result.success(qualityMonitorService.addQualityMonitorRatifyOpinion(qualityMonitorDetailsRatify));
    }

    /**
     * 导出监控计划详情实施信息
     * @param qualityMonitorDetailsId 监控计划详情实施id
     */
    @ApiOperation(value = "导出监控计划详情实施信息")
    @GetMapping("/exportQualityMonitorRatify")
    public void exportQualityMonitorRatify(Integer qualityMonitorDetailsId, HttpServletResponse response){
        qualityMonitorService.exportQualityMonitorRatify(qualityMonitorDetailsId, response);
    }

    /************************************************************  评价  *******************************************************************/

    /**
     * 查询监控评价
     * @return
     */
    @ApiOperation(value = "查询监控评价")
    @GetMapping("/getQualityMonitorEvaluate")
    public Result<QualityMonitorDetailsEvaluate> getQualityMonitorEvaluate(Integer qualityMonitorDetailsId) {
        return Result.success(qualityMonitorService.getQualityMonitorEvaluate(qualityMonitorDetailsId));
    }

    /**
     * 新增监控评价
     * @return
     */
    @ApiOperation(value = "新增监控评价")
    @PostMapping("/addQualityMonitorEvaluate")
    public Result addQualityMonitorEvaluate(@RequestBody QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate) {
        return Result.success(qualityMonitorService.addQualityMonitorEvaluate(qualityMonitorDetailsEvaluate));
    }

    /**
     * 导出监控评价
     * @param qualityMonitorDetailsId 监控评价id
     */
    @ApiOperation(value = "导出监控评价")
    @GetMapping("/exportQualityMonitorEvaluate")
    public void exportQualityMonitorEvaluate(Integer qualityMonitorDetailsId, HttpServletResponse response){
        qualityMonitorService.exportQualityMonitorEvaluate(qualityMonitorDetailsId, response);
    }

    /**
     * 监控评价批准意见
     * @return
     */
    @ApiOperation(value = "监控评价批准意见")
    @PostMapping("/addMonitorEvaluateOpinion")
    public Result addMonitorEvaluateOpinion(@RequestBody QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate) {
        return Result.success(qualityMonitorService.addMonitorEvaluateOpinion(qualityMonitorDetailsEvaluate));
    }


    /**
     * 新增监控评价附件表
     * @param qualityMonitorDetailsId
     * @param file
     * @return
     */
    @ApiOperation(value = "新增监控评价附件表")
    @PostMapping("/uploadEvaluateFile")
    public Result<?> uploadEvaluateFile(Integer qualityMonitorDetailsId, MultipartFile file) {
        return Result.success(qualityMonitorService.uploadEvaluateFile(qualityMonitorDetailsId, file));
    }


    /**
     * 查询监控评价附件列表
     * @return
     */
    @ApiOperation(value = "查询监控评价附件列表")
    @GetMapping("/getEvaluateFileList")
    public Result<List<QualityMonitorDetailsEvaluateFile>> getEvaluateFileList(Integer qualityMonitorDetailsId) {
        return Result.success(qualityMonitorService.getEvaluateFileList(qualityMonitorDetailsId));
    }

    /**
     * 删除监控评价附件表
     * @return
     */
    @ApiOperation(value = "删除监控评价附件表")
    @DeleteMapping("/delVerifyEvaluateFileList")
    public Result delVerifyMethodFileList(Integer evaluateFileId) {
        return Result.success(qualityMonitorDetailsEvaluateFileMapper.deleteById(evaluateFileId));
    }

    /******************************************************** 完成报告 ***************************************************************/

    /**
     * 上传监控完成报告
     * @param file
     * @param qualityMonitorDetailsId
     * @return
     */
    @ApiOperation(value = "上传监控完成报告")
    @PostMapping("/uploadFinishReport")
    public Result uploadFinishReport(MultipartFile file, Integer qualityMonitorDetailsId) {
        return Result.success(qualityMonitorService.uploadFinishReport(file, qualityMonitorDetailsId));
    }


    /**
     * 监控计划批准
     * @return
     */
    @ApiOperation(value = "批准完成报告")
    @PostMapping("/ratifyFinishReport")
    public Result ratifyFinishReport(@RequestBody QualityMonitorDetails qualityMonitorDetails) {
        return Result.success(qualityMonitorService.ratifyFinishReport(qualityMonitorDetails));
    }
}


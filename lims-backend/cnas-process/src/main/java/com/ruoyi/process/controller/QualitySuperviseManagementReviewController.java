package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.pojo.QualitySupervise;
import com.ruoyi.process.pojo.QualitySuperviseManagementReview;
import com.ruoyi.process.service.QualitySuperviseManagementReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 质量监督管理评审输入材料 前端控制器
 * </p>
 *
 * @author
 * @since 2025-05-07 10:46:11
 */
@RestController
@RequestMapping("/managementReview")
@Api(tags = "质量监督管理评审输入材料")
@AllArgsConstructor
public class QualitySuperviseManagementReviewController {

    private QualitySuperviseManagementReviewService qualitySuperviseManagementReviewService;

    /**
     * 分页查询
     * @return
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/pageManagementReview")
    public Result<IPage<QualitySuperviseManagementReview>> pageManagementReview(QualitySuperviseManagementReview managementReview, Page page) {
        return Result.success(qualitySuperviseManagementReviewService.pageManagementReview(page, managementReview));
    }

    /**
     * 新增或修改
     * @return
     */
    @ApiOperation(value = "新增或修改")
    @PostMapping("/saveOrUpdateManagementReview")
    public Result saveOrUpdateManagementReview(@RequestBody QualitySuperviseManagementReview managementReview) {
        return Result.success(qualitySuperviseManagementReviewService.saveOrUpdate(managementReview));
    }

    /**
     * 删除
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/deleteManagementReview")
    public Result deleteManagementReview(Integer managementReviewId) {
        return Result.success(qualitySuperviseManagementReviewService.removeById(managementReviewId));
    }

    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出")
    @GetMapping("/exportManagementReview")
    public void exportManagementReview(Integer managementReviewId,HttpServletResponse response) {
        qualitySuperviseManagementReviewService.exportManagementReview(managementReviewId, response);
    }
}

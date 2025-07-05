package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.QualitySuperviseManagementReview;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 质量监督管理评审输入材料 服务类
 * </p>
 *
 * @author
 * @since 2025-05-07 10:46:11
 */
public interface QualitySuperviseManagementReviewService extends IService<QualitySuperviseManagementReview> {

    /**
     * 分页查询
     * @param page
     * @param managementReview
     * @return
     */
    IPage<QualitySuperviseManagementReview> pageManagementReview(Page page, QualitySuperviseManagementReview managementReview);

    /**
     * 导出
     * @param managementReviewId
     * @param response
     */
    void exportManagementReview(Integer managementReviewId, HttpServletResponse response);
}

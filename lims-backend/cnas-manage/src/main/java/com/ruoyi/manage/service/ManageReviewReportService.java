package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageReviewReport;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 管理评审报告 服务类
 * </p>
 *
 * @author
 * @since 2024-11-12 04:44:39
 */
public interface ManageReviewReportService extends IService<ManageReviewReport> {


    IPage<ManageReviewReport> page(Page page, String startTime, String endTime, String place);

    void exportReviewReport(Integer id, HttpServletResponse response);
}

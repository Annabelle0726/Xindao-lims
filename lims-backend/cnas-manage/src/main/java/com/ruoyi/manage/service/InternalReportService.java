package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.InternalReport;

import javax.servlet.http.HttpServletResponse;

/**
 * 内审报告表
 *
 * @author zhuo
 * @since 2024-11-11
 */
public interface InternalReportService extends IService<InternalReport> {

    /**
     * 内审报告分页查询
     * @param page
     * @param internalReport
     * @return
     */
    IPage<InternalReport> pageInternalReport(Page page, InternalReport internalReport);

    /**
     * 内审报告审核
     * @param internalReport
     * @return
     */
    boolean ratifyInternalCheck(InternalReport internalReport);

    /**
     * 质量部经理填写
     * @param internalReport
     * @return
     */
    boolean qualityInternalReport(InternalReport internalReport);

    /**
     * 导出内审报告
     * @param reportId
     * @param response
     */
    void exportInternalReport(Integer reportId, HttpServletResponse response);
}


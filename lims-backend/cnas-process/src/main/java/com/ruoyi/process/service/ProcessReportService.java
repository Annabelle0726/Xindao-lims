package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.ProcessReportDto;
import com.ruoyi.process.pojo.ProcessReport;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验报告发放登记表 服务类
 * </p>
 *
 * @author
 * @since 2024-11-05 08:58:39
 */
public interface ProcessReportService extends IService<ProcessReport> {

    IPage<ProcessReportDto> pageProcessReport(Page page, ProcessReportDto processReport);

    void exportProcessReport(ProcessReportDto dto, HttpServletResponse response);
}

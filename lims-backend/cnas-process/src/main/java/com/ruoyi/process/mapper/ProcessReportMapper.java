package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.ProcessReportDto;
import com.ruoyi.process.pojo.ProcessReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 检验报告发放登记表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-05 08:58:39
 */
public interface ProcessReportMapper extends BaseMapper<ProcessReport> {

    IPage<ProcessReportDto> pageProcessReport(Page page, @Param("ew") QueryWrapper<ProcessReportDto> queryWrappers);

    List<ProcessReportDto> exportProcessReport();
}

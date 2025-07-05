package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.InternalReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 内审报告表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Mapper
public interface InternalReportMapper extends BaseMapper<InternalReport> {

    /**
     * 内审报告分页查询
     * @param page
     * @param internalReportQueryWrapper
     * @return
     */
    IPage<InternalReport> pageInternalReport(Page page, @Param("ew") QueryWrapper<InternalReport> internalReportQueryWrapper);
}


package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.QualityMonitorDto;
import com.ruoyi.process.pojo.QualityMonitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 质量监控计划主表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Mapper
public interface QualityMonitorMapper extends BaseMapper<QualityMonitor> {

    /**
     * 监控计划列表
     * @return
     */
    IPage<QualityMonitorDto> pageQualityMonitor(Page page, @Param("ew") QueryWrapper<QualityMonitor> qualityMonitorQueryWrapper);

}


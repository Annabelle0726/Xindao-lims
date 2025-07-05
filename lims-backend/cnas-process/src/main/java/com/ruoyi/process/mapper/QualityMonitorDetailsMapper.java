package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.QualityMonitorDetailsDto;
import com.ruoyi.process.pojo.QualityMonitorDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 质量监控计划详情表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Mapper
public interface QualityMonitorDetailsMapper extends BaseMapper<QualityMonitorDetails> {

    /**
     * 质量监控计划列表
     * @param page
     * @param qualityMonitorDetailsQueryWrapper
     * @return
     */
    IPage<QualityMonitorDetailsDto> pageQualityMonitorDetail(Page page, @Param("ew") QueryWrapper<QualityMonitorDetails> qualityMonitorDetailsQueryWrapper);
}


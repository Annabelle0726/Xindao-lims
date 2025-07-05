package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.QualitySuperviseDetailsDto;
import com.ruoyi.process.pojo.QualitySuperviseDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 质量监督详情表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Mapper
public interface QualitySuperviseDetailsMapper extends BaseMapper<QualitySuperviseDetails> {

    /**
     * 监督计划详情列表
     * @param page
     * @param qualitySuperviseDetailsQueryWrapper
     * @return
     */
    IPage<QualitySuperviseDetailsDto> pageQualitySuperviseDetail(Page page, @Param("ew") QueryWrapper<QualitySuperviseDetailsDto> qualitySuperviseDetailsQueryWrapper, @Param("causeType") Integer causeType);
}


package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.QualitySupervise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 质量监督主表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Mapper
public interface QualitySuperviseMapper extends BaseMapper<QualitySupervise> {

    /**
     * 监督计划列表
     * @param page
     * @param qualitySuperviseQueryWrapper
     * @return
     */
    IPage<QualitySupervise> pageQualitySupervise(@Param("page") Page page, @Param("ew") QueryWrapper<QualitySupervise> qualitySuperviseQueryWrapper);

    /**
     * 查询该计划监督员
     * @param superviseDetailsId
     * @return
     */
    List<Map<String, String>> getRecordUser(Integer superviseDetailsId);
}


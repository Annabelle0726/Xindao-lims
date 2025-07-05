package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.QualitySuperviseDetailsCorrect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 质量监督详情纠正处理表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Mapper
public interface QualitySuperviseDetailsCorrectMapper extends BaseMapper<QualitySuperviseDetailsCorrect> {

    /**
     * 查询监督纠正措施列表
     * @param page
     * @param ew
     * @return
     */
    IPage<QualitySuperviseDetailsCorrect> pageSuperviseDetailAccording(Page page, @Param("ew") QueryWrapper<QualitySuperviseDetailsCorrect> ew);
}


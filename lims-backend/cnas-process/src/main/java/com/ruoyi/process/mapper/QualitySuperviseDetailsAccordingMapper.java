package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.QualitySuperviseDetailsAccordingDto;
import com.ruoyi.process.pojo.QualitySuperviseDetailsAccording;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 质量监督详情不符合项控制表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Mapper
public interface QualitySuperviseDetailsAccordingMapper extends BaseMapper<QualitySuperviseDetailsAccording> {

    /**
     * 查询不符合项控制列表
     * @param page
     * @param ew
     * @return
     */
    IPage<QualitySuperviseDetailsAccording> pageSuperviseDetailAccording(Page page, @Param("ew") QueryWrapper<QualitySuperviseDetailsAccording> ew);

    /**
     * 导出监督记录不符合控制信息
     * @param superviseDetailId
     * @return
     */
    QualitySuperviseDetailsAccordingDto selectSuperviseDetailsAccording(@Param("superviseDetailId") Integer superviseDetailId);
}


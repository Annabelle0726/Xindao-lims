package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.process.pojo.QualityMonitorDetailsEvaluate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 质量监控计划详情评价表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Mapper
public interface QualityMonitorDetailsEvaluateMapper extends BaseMapper<QualityMonitorDetailsEvaluate> {

    /**
     * 查询质量监控评价
     * @param qualityMonitorDetailsId
     * @return
     */
    QualityMonitorDetailsEvaluate getQualityMonitorEvaluate(Integer qualityMonitorDetailsId);
}


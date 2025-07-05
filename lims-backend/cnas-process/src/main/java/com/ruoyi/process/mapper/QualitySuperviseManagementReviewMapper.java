package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.QualitySuperviseManagementReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 质量监督管理评审输入材料 Mapper 接口
 * </p>
 *
 * @author
 * @since 2025-05-07 10:46:11
 */
public interface QualitySuperviseManagementReviewMapper extends BaseMapper<QualitySuperviseManagementReview> {

    /**
     * 分页查询
     * @param page
     * @return
     */
    IPage<QualitySuperviseManagementReview> pageManagementReview(@Param("page") Page page, @Param("ew") QueryWrapper<QualitySuperviseManagementReview> ew);
}

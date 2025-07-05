package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageReviewReport;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 管理评审报告 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-12 04:44:39
 */
public interface ManageReviewReportMapper extends BaseMapper<ManageReviewReport> {

    IPage<ManageReviewReport> page(Page page, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("place") String place);

}

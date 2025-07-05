package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessEvaluate;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 测量不确定度的评价 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-02 01:10:43
 */
public interface ProcessEvaluateMapper extends BaseMapper<ProcessEvaluate> {

    IPage<ProcessEvaluate> pageProcessEvaluate(Page page, @Param("ew") QueryWrapper<ProcessEvaluate> queryWrappers);
}

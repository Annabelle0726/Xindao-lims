package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessSample;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 样品接收 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-12-12 05:02:49
 */
public interface ProcessSampleMapper extends BaseMapper<ProcessSample> {

    IPage<ProcessSample> pageProcessSample(@Param("page") Page page, @Param("ew") QueryWrapper<ProcessSample> ew);
}

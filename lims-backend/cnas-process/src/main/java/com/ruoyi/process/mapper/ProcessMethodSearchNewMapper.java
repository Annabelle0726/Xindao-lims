package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.ProcessMethodSearchNewBackupsDto;
import com.ruoyi.process.pojo.ProcessMethodSearchNew;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 标准查新
 *
 * @author zhuo
 * @since 2024-11-04
 */
@Mapper
public interface ProcessMethodSearchNewMapper extends BaseMapper<ProcessMethodSearchNew> {

    /**
     * 标准查新分页查询
     * @param page
     * @param processMethodSearchNewDtoQueryWrapper
     * @param beginDate
     * @param endDate
     * @return
     */
    IPage<ProcessMethodSearchNew> pageMethodSearchNew(@Param("page") Page page, @Param("ew") QueryWrapper<ProcessMethodSearchNewBackupsDto> processMethodSearchNewDtoQueryWrapper, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

}


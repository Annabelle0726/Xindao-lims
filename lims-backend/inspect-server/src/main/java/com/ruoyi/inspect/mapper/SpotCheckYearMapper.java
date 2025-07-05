package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.SpotCheckYearDto;
import com.ruoyi.inspect.pojo.SpotCheckYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 年度抽样计划
 *
 * @author zhuo
 * @since 2024-10-10
 */
@Mapper
public interface SpotCheckYearMapper extends BaseMapper<SpotCheckYear> {

    /**
     * 查询年度抽样列表
     * @param page
     * @param ew
     * @return
     */
    IPage<SpotCheckYearDto> getSpotCheckYearPage(Page page, @Param("ew") QueryWrapper<SpotCheckYearDto> ew);

}


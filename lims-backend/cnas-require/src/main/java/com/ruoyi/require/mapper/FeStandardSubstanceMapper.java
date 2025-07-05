package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.pojo.FeStandardSubstance;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标准物质清单 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-13 03:58:59
 */
public interface FeStandardSubstanceMapper extends BaseMapper<FeStandardSubstance> {

    IPage<FeStandardSubstance> getPage(Page page,@Param("ew")FeStandardSubstance feStandardSubstance);
}

package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.InternalPlanDto;
import com.ruoyi.manage.pojo.InternalPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内审年度计划 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-13 03:27:47
 */
@Mapper
public interface InternalPlanMapper extends BaseMapper<InternalPlan> {

    /**
     * 内审年度计划分页查询
     * @param page
     * @param ew
     * @return
     */
    IPage<InternalPlanDto> pageInternalPlan(Page page, @Param("ew") QueryWrapper<InternalPlan> ew);
}

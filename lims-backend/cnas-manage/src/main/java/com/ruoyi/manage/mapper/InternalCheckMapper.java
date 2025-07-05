package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.InternalCheckDto;
import com.ruoyi.manage.pojo.InternalCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 内审检查表
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Mapper
public interface InternalCheckMapper extends BaseMapper<InternalCheck> {

    /**
     * 内审检查分页查询
     * @param page
     * @return
     */
    IPage<InternalCheckDto> pageInternalCheck(Page page, @Param("ew") QueryWrapper<InternalCheck> ew);
}


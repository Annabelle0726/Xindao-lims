package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.InternalImplementDto;
import com.ruoyi.manage.pojo.InternalImplement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 内审实施计划
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Mapper
public interface InternalImplementMapper extends BaseMapper<InternalImplement> {

    /**
     * 内审实施计划分页查询
     * @param page
     * @return
     */
    IPage<InternalImplementDto> pageInternalImplement(Page page, @Param("ew") QueryWrapper<InternalImplement> ew);
}


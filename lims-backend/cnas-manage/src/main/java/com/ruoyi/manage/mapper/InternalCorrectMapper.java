package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.InternalCorrect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内审管理纠正处理表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-13 04:00:15
 */
@Mapper
public interface InternalCorrectMapper extends BaseMapper<InternalCorrect> {

    IPage<InternalCorrect> pageInternalAccording(Page page, @Param("ew") QueryWrapper<InternalCorrect> ew);
}

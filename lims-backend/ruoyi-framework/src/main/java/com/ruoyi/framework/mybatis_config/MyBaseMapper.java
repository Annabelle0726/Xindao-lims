package com.ruoyi.framework.mybatis_config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 自定义添加mybatis-plus批量添加
 * @param <T>
 */
public interface MyBaseMapper <T> extends BaseMapper<T> {

    int insertBatchSomeColumn(List<T> entityList);
}


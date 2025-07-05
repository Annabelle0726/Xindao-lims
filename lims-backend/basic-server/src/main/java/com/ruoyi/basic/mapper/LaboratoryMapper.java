package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.Laboratory;
import org.apache.ibatis.annotations.Param;

/**
 * 实验室管理(Laboratory)表数据库访问层
 */
public interface LaboratoryMapper extends BaseMapper<Laboratory> {

    IPage<Laboratory> selectItemParameter(Page page, @Param("ew") QueryWrapper<Laboratory> ew);

    Object obtainItemParameterList(@Param("page") Page page, @Param("ew") QueryWrapper<Laboratory> ew);

}


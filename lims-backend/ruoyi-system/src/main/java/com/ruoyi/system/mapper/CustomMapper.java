package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.entity.Custom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface CustomMapper extends BaseMapper<Custom> {

    IPage<Custom> selectCustomPageList(IPage<Custom> page, @Param("ew") QueryWrapper<Custom> ew);

    int delCustomById(Integer id);

}

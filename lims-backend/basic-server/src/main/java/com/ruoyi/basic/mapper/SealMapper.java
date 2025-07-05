package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.basic.pojo.Seal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SealMapper extends BaseMapper<Seal> {
    IPage<Seal>selectSeal(Page page, @Param("ew") QueryWrapper<Seal> ew);
    List<Laboratory> selectLaboratory (@Param("labId") Integer labId);

}

package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.PageTestObjectDto;
import com.ruoyi.basic.pojo.StructureTestObject;
import org.apache.ibatis.annotations.Param;

/**
 * 检测对象(StructureTestObject)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-26 17:36:41
 */
public interface StructureTestObjectMapper extends BaseMapper<StructureTestObject> {

    IPage<PageTestObjectDto> selectTestObjectList(Page page, @Param("ew") QueryWrapper<PageTestObjectDto> ew, @Param("partNo") String partNo);

}


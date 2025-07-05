package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.StructureTestObjectPart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 检验对象零件表
 *
 * @author zhuo
 * @since 2024-08-07
 */
@Mapper
public interface StructureTestObjectPartMapper extends BaseMapper<StructureTestObjectPart> {
    IPage<StructureTestObjectPart> selectListByTestObjectId(Page page, @Param("ew") QueryWrapper<StructureTestObjectPart> structureTestObjectPartQueryWrapper, @Param("testObjectId") Integer testObjectId);
}


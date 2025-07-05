package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.InternalWastesDto;
import com.ruoyi.require.pojo.InternalWastes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 安全内务三废登记 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-19 06:39:27
 */
@Mapper
public interface InternalWastesMapper extends BaseMapper<InternalWastes> {


    /**
     * 安全内务三废登记分页查询
     * @param page
     * @param ew
     * @return
     */
    IPage<InternalWastesDto> pageInternalWastes(Page page, @Param("ew") QueryWrapper<InternalWastes> ew);
}

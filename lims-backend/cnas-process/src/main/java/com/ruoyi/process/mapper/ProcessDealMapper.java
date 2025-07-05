package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.ProcessDealDto;
import com.ruoyi.process.pojo.ProcessDeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 检测或校准物品的处置 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-02 02:50:19
 */
public interface ProcessDealMapper extends BaseMapper<ProcessDeal> {

    IPage<ProcessDeal> pageProcessDeal(Page page, @Param("ew") QueryWrapper<ProcessDeal> queryWrappers);

    List<ProcessDealDto> selectDeal(@Param("id") Integer id);

}

package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessTotaldeal;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检测或校准物品的处置总表(历史) Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-02 03:59:09
 */
public interface ProcessTotaldealMapper extends BaseMapper<ProcessTotaldeal> {

    IPage<ProcessTotaldeal> pageProcessTotaldeal(Page page, @Param("ew") QueryWrapper<ProcessTotaldeal> queryWrappers);
}

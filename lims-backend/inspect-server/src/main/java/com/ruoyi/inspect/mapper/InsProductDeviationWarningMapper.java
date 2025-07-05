package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.InsProductDeviationWarningDto;
import com.ruoyi.inspect.pojo.InsProductDeviationWarning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检验项偏差预警主表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-03-28 02:18:02
 */
public interface InsProductDeviationWarningMapper extends BaseMapper<InsProductDeviationWarning> {

    /**
     * 查看预警列表
     * @param page
     * @return
     */
    IPage<InsProductDeviationWarningDto> selectDeviationWarningPage(@Param("page") Page page, @Param("ew") QueryWrapper<InsProductDeviationWarningDto> ew);
}

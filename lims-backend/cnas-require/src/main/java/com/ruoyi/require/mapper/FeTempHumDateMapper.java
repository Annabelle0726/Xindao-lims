package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.FeTempHumDateDto;
import com.ruoyi.require.pojo.FeTempHumDate;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度 区域 -父 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-09 11:02:18
 */
public interface FeTempHumDateMapper extends BaseMapper<FeTempHumDate> {

    IPage<FeTempHumDateDto> getFeTempHumDate(Page page, @Param("ew") QueryWrapper<FeTempHumDateDto> ew);

    /**
     * 查询这个月是否有该实验室的任务
     * @param laboratory 实验室
     * @param formattedDate 当前月份
     * @return 返回条数
     */
    int selectFeTempHumDateIncludeFormattedDate(@Param("laboratory") String laboratory, @Param("formattedDate") String formattedDate);
}

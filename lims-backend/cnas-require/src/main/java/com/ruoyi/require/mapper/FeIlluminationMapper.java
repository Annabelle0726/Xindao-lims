package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.FeIlluminationDto;
import com.ruoyi.require.dto.FeIlluminationExportDto;
import com.ruoyi.require.pojo.FeIllumination;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-照度记录表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-07 04:15:57
 */
public interface FeIlluminationMapper extends BaseMapper<FeIllumination> {

    IPage<FeIlluminationDto> getFeLightningProtection(Page page);

    /**
     * 查询照明记录
     * @param intensityIlluminationId
     * @return
     */
    FeIlluminationExportDto selectFeIllumination(Integer intensityIlluminationId);
}

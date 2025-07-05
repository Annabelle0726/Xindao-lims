package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.FePowerStableDto;
import com.ruoyi.require.dto.FePowerStableExportDto;
import com.ruoyi.require.pojo.FePowerStable;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-电源稳定性 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:52
 */
public interface FePowerStableMapper extends BaseMapper<FePowerStable> {

    IPage<FePowerStableDto> getLaboratoryFacilityPowerStablePage(Page page);

    Map<String, Objects> getCalibrationDate(Integer deviceId);

    /**
     * 查询电源稳定性
     * @param powerStableId
     * @return
     */
    FePowerStableExportDto selectPowerStable(@Param("powerStableId") Integer powerStableId);
}

package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.FePowerStableDto;
import com.ruoyi.require.pojo.FePowerStable;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-电源稳定性 服务类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:16:52
 */
public interface FePowerStableService extends IService<FePowerStable> {

    IPage<FePowerStableDto> getLaboratoryFacilityPowerStablePage(Page page);

    Map<String, Objects> getCalibrationDate(Integer deviceId);

    /**
     * 导出电源稳定性
     * @param powerStableId
     * @param response
     */
    void exportFePowerStable(Integer powerStableId, HttpServletResponse response);
}

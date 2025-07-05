package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.pojo.FeCalibrationSchedule;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * </p>
 *
 * @author 
 * @since 2024-11-13 02:53:05
 */
public interface FeCalibrationScheduleService extends IService<FeCalibrationSchedule> {


    IPage<FeCalibrationSchedule> page(Page page,String instrumentName, String managementNumber);

    /**
     * 量值溯源计划导出word
     *
     * @param response 响应添加word
     */
    void exportWordOfValueTraceabilityPlan(HttpServletResponse response);

}

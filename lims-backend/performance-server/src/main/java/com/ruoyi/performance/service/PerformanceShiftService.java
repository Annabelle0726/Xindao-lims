package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.dto.PerformanceShiftAddDto;
import com.ruoyi.performance.pojo.PerformanceShift;

import java.util.Map;

/**
 * <p>
 * 绩效管理-班次 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-08 09:12:04
 */
public interface PerformanceShiftService extends IService<PerformanceShift> {

    void performanceShiftAdd(PerformanceShiftAddDto performanceShiftAddDto);

    Map<String, Object> performanceShiftPage(Page<Object> page, String time, String userName, String laboratory);

    void performanceShiftUpdate(PerformanceShift performanceShift);

    IPage<Map<String, Object>> performanceShiftPageYear(Page<Object> page, String time, String userName, String laboratory);

    Map<Object, Object> exportToYearExcel(String time, String userName, String laboratory) throws Exception;

    Map<Object, Object> exportToMonthExcel(String time, String userName, String laboratory);
}

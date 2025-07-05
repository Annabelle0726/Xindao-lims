package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.pojo.DeviceAccidentReport;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备事故报告单 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 06:31:12
 */
public interface DeviceAccidentReportService extends IService<DeviceAccidentReport> {

    /**
     * 设备事故报告列表
     * @param page
     * @param deviceAccidentReport
     * @return
     */
    IPage<DeviceAccidentReport> pageDeviceAccidentReport(Page page, DeviceAccidentReport deviceAccidentReport);

    /**
     * 新增设备事故报告
     * @param deviceAccidentReport
     * @return
     */
    boolean addDeviceAccidentReport(DeviceAccidentReport deviceAccidentReport);

    /**
     * 导出设备事故报告
     * @param accidentReportId 设备事故报告id
     * @param response 响应
     */
    void exportDeviceAccidentReport(Integer accidentReportId, HttpServletResponse response);
}

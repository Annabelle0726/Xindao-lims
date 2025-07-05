package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceBreakdownMaintenance;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备故障维修表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 04:50:57
 */
public interface DeviceBreakdownMaintenanceService extends IService<DeviceBreakdownMaintenance> {

    /**
     * 设备故障维修列表
     * @param page
     * @param deviceBreakdownMaintenance
     * @return
     */
    IPage<DeviceBreakdownMaintenance> pageDeviceBreakdownMaintenance(Page page, DeviceBreakdownMaintenance deviceBreakdownMaintenance);

    /**
     * 新增设备故障维修
     * @param deviceBreakdownMaintenance
     * @return
     */
    boolean addDeviceBreakdownMaintenance(DeviceBreakdownMaintenance deviceBreakdownMaintenance);

    /**
     * 导出设备故障维修
     * @param maintenanceId 设备故障维修id
     * @param response 响应
     */
    void exportDeviceBreakdownMaintenance(Integer maintenanceId, HttpServletResponse response);
}

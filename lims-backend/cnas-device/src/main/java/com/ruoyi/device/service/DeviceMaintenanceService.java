package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceMaintenanceDto;
import com.ruoyi.device.pojo.DeviceMaintenance;

import javax.servlet.http.HttpServletResponse;

/**
 * 设备维护保养
 */
public interface DeviceMaintenanceService extends IService<DeviceMaintenance> {

    /**
     * 设备维护分页查询
     * @return
     */
    IPage<DeviceMaintenanceDto> selectDeviceMaintenancePage(Page page, DeviceMaintenanceDto deviceMaintenance);

    /**
     * 导出设备维护保养
     * @param deviceId
     * @param response
     * @return
     */
    void exportDeviceMaintenance(Integer deviceId, HttpServletResponse response);
}

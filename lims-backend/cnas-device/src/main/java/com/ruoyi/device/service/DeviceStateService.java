package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceStateDto;
import com.ruoyi.device.pojo.DeviceState;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备停用/启用 服务类
 * </p>
 *
 * @author 
 * @since 2024-09-26 09:51:40
 */
public interface DeviceStateService extends IService<DeviceState> {

    IPage<DeviceStateDto> getDeviceStatePage(Integer deviceId, Page page, String processNumber);

    /**
     * 导出设备状态
     *
     * @param deviceId
     * @param processNumber
     * @param response
     */
    void exportDeviceStatus(Integer deviceId, String processNumber, HttpServletResponse response);
}

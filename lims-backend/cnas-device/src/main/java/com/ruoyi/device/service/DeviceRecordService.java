package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceRecordDto;
import com.ruoyi.device.pojo.DeviceRecord;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * cnas设备使用记录表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 11:06:47
 */
public interface DeviceRecordService extends IService<DeviceRecord> {

    IPage<DeviceRecordDto> deviceRecordPage(Integer deviceId, Page page, String sampleCode, String managementNumber);


    /**
     * 导出设备使用记录
     *
     * @param deviceId
     * @param response
     */
    void exportUseRecord(Integer deviceId, String exportDate, HttpServletResponse response);
}

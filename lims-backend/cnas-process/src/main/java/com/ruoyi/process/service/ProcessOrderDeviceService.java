package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessOrderDevice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * cnas设备使用记录表(7.1检验委托单) 服务类
 * </p>
 *
 * @author
 * @since 2025-04-17 03:51:48
 */
public interface ProcessOrderDeviceService extends IService<ProcessOrderDevice> {

    IPage<ProcessOrderDevice> deviceRecordPage(Integer deviceId, Page page, String sampleCode, String managementNumber);
}

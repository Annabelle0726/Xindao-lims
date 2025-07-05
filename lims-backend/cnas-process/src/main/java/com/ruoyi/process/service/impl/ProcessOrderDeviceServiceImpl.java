package com.ruoyi.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessOrderDevice;
import com.ruoyi.process.mapper.ProcessOrderDeviceMapper;
import com.ruoyi.process.service.ProcessOrderDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * cnas设备使用记录表(7.1检验委托单) 服务实现类
 * </p>
 *
 * @author
 * @since 2025-04-17 03:51:48
 */
@Service
public class ProcessOrderDeviceServiceImpl extends ServiceImpl<ProcessOrderDeviceMapper, ProcessOrderDevice> implements ProcessOrderDeviceService {

    @Override
    public IPage<ProcessOrderDevice> deviceRecordPage(Integer deviceId, Page page, String sampleCode, String managementNumber) {
        return baseMapper.deviceRecordPage(deviceId, page, sampleCode, managementNumber);
    }
}

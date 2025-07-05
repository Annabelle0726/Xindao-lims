package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.pojo.DeviceAcceptance;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备验收(装备) Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 01:45:14
 */
public interface DeviceAcceptanceMapper extends BaseMapper<DeviceAcceptance> {

    /**
     * 设备验收列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceAcceptance> pageDeviceAcceptance(Page page, @Param("ew") QueryWrapper<DeviceAcceptance> ew);
}

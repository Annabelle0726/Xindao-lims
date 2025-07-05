package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceBreakdownMaintenance;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备故障维修表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 04:50:57
 */
public interface DeviceBreakdownMaintenanceMapper extends BaseMapper<DeviceBreakdownMaintenance> {

    /**
     * 设备故障维修列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceBreakdownMaintenance> pageDeviceBreakdownMaintenance(Page page, @Param("ew") QueryWrapper<DeviceBreakdownMaintenance> ew);
}

package com.ruoyi.device.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceMaintenanceDto;
import com.ruoyi.device.pojo.DeviceMaintenance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备维护保养
 */
@Mapper
public interface DeviceMaintenanceMapper extends BaseMapper<DeviceMaintenance> {

    /**
     * 设备维护分页查询
     * @param page
     * @return
     */
    IPage<DeviceMaintenanceDto> selectDeviceMaintenancePage(@Param("page") Page page, @Param("ew") QueryWrapper<DeviceMaintenanceDto> ew);

}

package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceMaintenancePlanDto;
import com.ruoyi.device.pojo.DeviceMaintenancePlan;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备保养计划表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:10:52
 */
public interface DeviceMaintenancePlanMapper extends BaseMapper<DeviceMaintenancePlan> {

    /**
     * 分页查询设备保养计划
     * @param page 当前页
     * @param queryWrappers 查询条件
     * @return
     */
    IPage<DeviceMaintenancePlan> selectDeviceParameterPage(Page page, @Param("ew") QueryWrapper<DeviceMaintenancePlanDto> queryWrappers);

    /**
     * 根据保养计划id查询设备保养计划
     * @param maintenancePlanId 保养计划id
     * @return
     */
    DeviceMaintenancePlanDto selectMaintenancePlanById(@Param("maintenancePlanId") Integer maintenancePlanId);
}

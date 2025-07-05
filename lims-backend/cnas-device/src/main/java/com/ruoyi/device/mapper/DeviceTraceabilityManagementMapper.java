package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.device.dto.DeviceTraceabilityManagementDto;
import com.ruoyi.device.pojo.DeviceTraceabilityManagement;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备量值溯源计划表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:44
 */
public interface DeviceTraceabilityManagementMapper extends BaseMapper<DeviceTraceabilityManagement> {

    /**
     * 分页查询设备量值溯源计划
     * @param page
     * @param queryWrappers
     * @return
     */
    IPage<DeviceTraceabilityManagement> selectDeviceParameterPage(IPage page, @Param("ew") QueryWrapper<DeviceTraceabilityManagementDto> ew);

    /**
     * 根据id查询设备量值溯源计划
     * @param traceabilityManagementId
     * @return
     */
    DeviceTraceabilityManagementDto selectDeviceTraceabilityManagementById(@Param("traceabilityManagementId") Integer traceabilityManagementId);
}

package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceImpowerDto;
import com.ruoyi.device.dto.DeviceTraceabilityManagementDto;
import com.ruoyi.device.pojo.DeviceImpower;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备量值溯源计划表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:23
 */
public interface DeviceImpowerMapper extends BaseMapper<DeviceImpower> {

    /**
     * 分页查询使用授权
     * @param page
     * @param deviceImpowerDtoQueryWrapper
     * @return
     */
    IPage<DeviceImpower> selectDeviceParameterPage(@Param("page") Page page, @Param("ew") QueryWrapper<DeviceImpowerDto> deviceImpowerDtoQueryWrapper);

    /**
     * 根据id查询设备使用授权
     * @param traceabilityManagementId
     * @return
     */
    DeviceTraceabilityManagementDto selectDeviceTraceabilityManagementById(@Param("traceabilityManagementId") Integer traceabilityManagementId);
}

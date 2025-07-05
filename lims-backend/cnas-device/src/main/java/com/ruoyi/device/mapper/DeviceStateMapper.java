package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceStateDto;
import com.ruoyi.device.pojo.DeviceState;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备停用/启用 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-09-26 09:51:40
 */
public interface DeviceStateMapper extends BaseMapper<DeviceState> {

    IPage<DeviceStateDto> getDeviceStatePage(@Param("deviceId") Integer deviceId, @Param("page") Page page, @Param("processNumber") String processNumber);
}

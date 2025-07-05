package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.device.dto.DeviceTraceabilityManagementDetailsDto;
import com.ruoyi.device.pojo.DeviceTraceabilityManagementDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备量值溯源计划详情表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:58
 */
public interface DeviceTraceabilityManagementDetailsMapper extends BaseMapper<DeviceTraceabilityManagementDetails> {

    /**
     * 根据溯源计划id查询溯源计划详情
     * @param traceabilityManagementId 溯源计划id
     * @return 溯源计划详情
     */
    List<DeviceTraceabilityManagementDetailsDto> deviceTraceabilityManagementDetailsList(@Param("traceabilityManagementId") Integer traceabilityManagementId);
}

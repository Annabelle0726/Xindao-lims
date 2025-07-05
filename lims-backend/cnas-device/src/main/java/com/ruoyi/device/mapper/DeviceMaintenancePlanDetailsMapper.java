package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.device.dto.DeviceMaintenancePlanDetailsDto;
import com.ruoyi.device.pojo.DeviceMaintenancePlanDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备保养计划详情表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:11:46
 */
public interface DeviceMaintenancePlanDetailsMapper extends BaseMapper<DeviceMaintenancePlanDetails> {

    /**
     * 查询设备保养详情
     * @param maintenancePlanId 设备保养计划id
     */
    List<DeviceMaintenancePlanDetailsDto> deviceInspectionRecordDetailsList(@Param("maintenancePlanId") Integer maintenancePlanId);
}

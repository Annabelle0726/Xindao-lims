package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceMaintenancePlanDto;
import com.ruoyi.device.pojo.DeviceMaintenancePlan;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备保养计划表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:10:52
 */
public interface DeviceMaintenancePlanService extends IService<DeviceMaintenancePlan> {

    /**
     * 分页查询设备保养计划
     * @param page 当前页
     * @param deviceMaintenancePlanDto 设备保养计划
     * @return
     */
    Result<IPage<DeviceMaintenancePlan>> selectDeviceMaintenancePlanByPage(Page page, DeviceMaintenancePlanDto deviceMaintenancePlanDto);

    /**
     * 新增设备保养计划
     *
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    Result addMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto);

    /**
     * 修改设备保养计划
     *
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    Result updateMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto);

    /**
     * 删除设备保养计划
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    Result deleteMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto);

    /**
     * 导出设备保养计划
     *
     * @param maintenancePlanId 设备保养计划id
     * @param response          响应
     */
    Result exportDeviceMaintenancePlanDto(Integer maintenancePlanId, HttpServletResponse response);

    /**
     * 查看设备保养计划详情
     * @param maintenancePlanId 设备保养计划id
     * @return
     */
    Result<DeviceMaintenancePlanDto> getMaintenancePlanDetail(Integer maintenancePlanId);

    /**
     * 提交审核
     * @param deviceMaintenancePlanDto
     * @return
     */
    Result submitReviewMaintenancePlanStatus(DeviceMaintenancePlanDto deviceMaintenancePlanDto);

    /**
     * 审核设备保养计划状态
     * @param deviceMaintenancePlanDto 设备保养计划
     * @return
     */
    Result reviewMaintenancePlanStatus(DeviceMaintenancePlanDto deviceMaintenancePlanDto);

}

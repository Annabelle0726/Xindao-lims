package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceTraceabilityManagementDto;
import com.ruoyi.device.pojo.DeviceTraceabilityManagement;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备量值溯源计划表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:44
 */
public interface DeviceTraceabilityManagementService extends IService<DeviceTraceabilityManagement> {

    /**
     * 分页查询量值溯源计划
     * @param page 分页参数
     * @param itemParameter 量值溯源计划
     * @return
     */
    Result<IPage<DeviceTraceabilityManagement>> selectDeviceTraceabilityManagementByPage(@Param("page") Page page, @Param("itemParameter") DeviceTraceabilityManagementDto itemParameter);

    /**
     * 新增量值溯源计划
     * @param deviceTraceabilityManagementDto 量值溯源计划
     */
    Result addTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto);

    /**
     * 修改量值溯源计划
     * @param deviceTraceabilityManagementDto 量值溯源计划
     */
    Result updateTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto);

    /**
     * 删除量值溯源计划
     * @param deviceTraceabilityManagementDto 量值溯源计划
     */
    Result deleteTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto);

    /**
     * 查询量值溯源计划详情
     */
    Result<DeviceTraceabilityManagementDto> getTraceabilityManagementDetail(Integer traceabilityManagementId);

    /**
     * 提交批准
     * @param deviceTraceabilityManagementDto
     * @return
     */
    Result submitReviewTraceabilityManagementStatus(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto);

    /**
     * 量值溯源计划审核
     */
    Result reviewTraceabilityManagementStatus(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto);

    /**
     * 量值溯源计划导出
     */
    Result exportDeviceTraceabilityManagementDto(Integer traceabilityManagementId, HttpServletResponse response);

}

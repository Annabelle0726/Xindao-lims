package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceInspectionRecordDto;
import com.ruoyi.device.pojo.DeviceInspectionRecord;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  设备点检记录表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 04:25:14
 */
public interface DeviceInspectionRecordService extends IService<DeviceInspectionRecord> {

    /**
     * 分页查询设备点检记录
     * @param page
     */
    Result<IPage<DeviceInspectionRecord>> getDeviceInspectionRecordByPage(Page page, DeviceInspectionRecordDto deviceInspectionRecord);


    /**
     * 查询点检详情
     * @param inspectionRecordId
     * @return
     */
    Result getDeviceInspectionRecord(Integer inspectionRecordId);

    /**
     * 新增设备点检记录
     *
     * @param deviceInspectionRecord 设备点检记录
     */
    Result addDeviceInspectionRecord(DeviceInspectionRecordDto deviceInspectionRecord);

    /**
     * 修改设备点检记录
     * @param deviceInspectionRecord 设备点检记录
     */
    Result updateInspectionRecordAndDetails(DeviceInspectionRecordDto deviceInspectionRecord);

    /**
     * 删除设备点检记录
     * @param deviceInspectionRecord 设备点检记录
     */
    Result deleteDeviceInspectionRecordOrDetails(DeviceInspectionRecordDto deviceInspectionRecord);

    /**
     * 复核点检记录
     * @param deviceExamineRecordDto
     * @return
     */
    Result reviewDeviceInspectionRecord(DeviceInspectionRecordDto deviceExamineRecordDto);

    /**
     * 导出设备点检记录
     *
     * @param deviceInspectionRecordId 设备点检记录id
     * @param response
     */
    Result exportDeviceInspectionRecord(Integer deviceInspectionRecordId, HttpServletResponse response);

}

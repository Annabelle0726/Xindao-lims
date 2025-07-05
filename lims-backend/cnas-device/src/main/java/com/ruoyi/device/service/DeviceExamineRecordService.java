package com.ruoyi.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceExamineRecordDto;
import com.ruoyi.device.pojo.DeviceExamineRecord;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备核查记录表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:28
 */
public interface DeviceExamineRecordService extends IService<DeviceExamineRecord> {

    /**
     * 查询核查记录
     * @return
     */
    DeviceExamineRecordDto getExamineRecord(Integer planDetailsId);

    /**
     * 新增核查记录
     * @return
     */
    boolean addExamineRecord(DeviceExamineRecordDto deviceExamineRecordDto);

    /**
     * 复核核查记录
     * @return
     */
    boolean reviewExamineRecord(DeviceExamineRecordDto deviceExamineRecordDto);

    /**
     * 导出复核核查记录
     * @param planDetailsId
     * @param response 响应
     */
    void exportReviewExamineRecordDetail(Integer planDetailsId, HttpServletResponse response);
}

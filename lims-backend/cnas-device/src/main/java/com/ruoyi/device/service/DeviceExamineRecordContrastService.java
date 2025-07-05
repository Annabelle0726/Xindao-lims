package com.ruoyi.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceExamineRecordContrastDto;
import com.ruoyi.device.pojo.DeviceExamineRecordContrast;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备核查记录对比表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:43
 */
public interface DeviceExamineRecordContrastService extends IService<DeviceExamineRecordContrast> {

    /**
     * 查询核查对比记录
     * @return
     */
    DeviceExamineRecordContrastDto getExamineRecordContrast(Integer planDetailsId);

    /**
     * 新增核查对比记录
     * @return
     *
     */
    boolean addExamineRecordContrast(DeviceExamineRecordContrastDto deviceExamineRecordContrastDto);

    /**
     * 审核核查对比记录
     * @return
     */
    boolean reviewExamineRecordContrast(DeviceExamineRecordContrastDto deviceExamineRecordContrastDto);

    /**
     * 导出审核核查对比记录
     *
     * @param recordId 审核核查对比记录id
     * @param response
     * @return
     */
    void exportReviewExamineRecordContrast(Integer recordId, HttpServletResponse response);
}

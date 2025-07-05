package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceCalibrationPlanDto;
import com.ruoyi.device.pojo.DeviceCalibrationPlan;
import com.ruoyi.device.pojo.DeviceCalibrationPlanDetail;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备校准计划主表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 03:58:17
 */
public interface DeviceCalibrationPlanService extends IService<DeviceCalibrationPlan> {

    /**
     * 新增设备校准计划
     * @param calibrationPlanDto
     * @return
     */
    boolean addDeviceCalibrationPlan(DeviceCalibrationPlanDto calibrationPlanDto);

    /**
     * 导入设备校准计划
     * @param file
     * @return
     */
    boolean importDeviceCalibrationPlan(MultipartFile file, String planYear);

    /**
     * 提交批准
     * @param deviceCalibrationPlan
     * @return
     */
    boolean submiatRatifyDeviceCalibrationPlan(DeviceCalibrationPlan deviceCalibrationPlan);

    /**
     * 设备校准计划批准
     * @param DeviceCalibrationPlan
     * @return
     */
    boolean ratifyDeviceCalibrationPlan(DeviceCalibrationPlan DeviceCalibrationPlan);

    /**
     * 设备校准计划列表
     * @param page
     * @param DeviceCalibrationPlan
     * @return
     */
    IPage<DeviceCalibrationPlanDto> pageDeviceCalibrationPlan(Page page, DeviceCalibrationPlan deviceCalibrationPlan);

    /**
     * 设备校准计划详情列表
     * @param page
     * @param DeviceCalibrationPlanDetails
     * @return
     */
    IPage<DeviceCalibrationPlanDetail> pageDeviceCalibrationPlanDetail(Page page, DeviceCalibrationPlanDetail deviceCalibrationPlanDetails);


    /**
     * 导出设备校准计划
     *
     * @param DeviceCalibrationPlanId
     * @param response
     */
    void exportDeviceCalibrationPlanDetail(Integer DeviceCalibrationPlanId, HttpServletResponse response);

    /**
     * 批量编辑设备校准详情
     * @param calibrationPlanDto
     * @return
     */
    boolean updateDeviceCalibrationPlan(DeviceCalibrationPlanDto calibrationPlanDto);

    /**
     * 查询设备校准详情
     * @param planId
     * @return
     */
    DeviceCalibrationPlanDto getDeviceCalibrationPlan(Integer planId);
}

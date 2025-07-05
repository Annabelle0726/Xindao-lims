package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceExaminePlanDetailsDto;
import com.ruoyi.device.dto.DeviceExaminePlanDto;
import com.ruoyi.device.pojo.DeviceExaminePlan;
import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备核查计划主表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:04
 */
public interface DeviceExaminePlanService extends IService<DeviceExaminePlan> {

    /**
     * 新增设备核查计划
     * @param examinePlanDto
     * @return
     */
    boolean addDeviceExaminePlan(DeviceExaminePlanDto examinePlanDto);

    /**
     * 导入设备核查计划
     * @param file
     * @return
     */
    boolean importDeviceExaminePlan(MultipartFile file);

    /**
     * 提交批准
     * @param deviceExaminePlan
     * @return
     */
    boolean submitRatifyDeviceExaminePlan(DeviceExaminePlan deviceExaminePlan);

    /**
     * 设备核查计划批准
     * @param DeviceExaminePlan
     * @return
     */
    boolean ratifyDeviceExaminePlan(DeviceExaminePlan DeviceExaminePlan);

    /**
     * 设备核查计划列表
     * @param page
     * @param DeviceExaminePlan
     * @return
     */
    IPage<DeviceExaminePlanDto> pageDeviceExaminePlan(Page page, DeviceExaminePlan DeviceExaminePlan);

    /**
     * 设备核查计划详情列表
     * @param page
     * @param DeviceExaminePlanDetails
     * @return
     */
    IPage<DeviceExaminePlanDetailsDto> pageDeviceExaminePlanDetail(Page page, DeviceExaminePlanDetails DeviceExaminePlanDetails);


    /**
     * 导出设备核查计划
     * @param deviceExaminePlanId 设备核查计划id
     * @param response 响应
     */
    void exportDeviceExaminePlanDetail(Integer deviceExaminePlanId, HttpServletResponse response);

    /**
     * 批量修改设备核查计划
     * @param examinePlanDto
     * @return
     */
    boolean updateDeviceExaminePlan(DeviceExaminePlanDto examinePlanDto);

    /**
     * 查询设备核查计划详情
     * @param planId
     * @return
     */
    DeviceExaminePlanDto getDeviceExaminePlan(Integer planId);
}

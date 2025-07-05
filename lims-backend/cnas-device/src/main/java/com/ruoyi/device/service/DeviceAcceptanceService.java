package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.pojo.DeviceAcceptance;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备验收(装备) 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 01:45:14
 */
public interface DeviceAcceptanceService extends IService<DeviceAcceptance> {

    /**
     * 设备验收列表
     * @param page
     * @param deviceAcceptance
     * @return
     */
    IPage<DeviceAcceptance> pageDeviceAcceptance(Page page, DeviceAcceptance deviceAcceptance);


    boolean uploadDeviceAcceptanceFile(Integer acceptanceId, MultipartFile file);

    /**
     * 设备验收导出
     * @param acceptanceId  设备验收id
     * @param response   响应体
     * @return
     */
    void exportDeviceAcceptance(Integer acceptanceId, HttpServletResponse response);
}

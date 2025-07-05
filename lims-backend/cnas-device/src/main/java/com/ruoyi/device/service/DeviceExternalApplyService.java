package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.pojo.DeviceExternalApply;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 利用外部设备申请表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 10:28:43
 */
public interface DeviceExternalApplyService extends IService<DeviceExternalApply> {

    /**
     * 利用外部设备申请列表
     * @param page
     * @param deviceExternalApply
     * @return
     */
    IPage<DeviceExternalApply> pageDeviceExternalApply(Page page, DeviceExternalApply deviceExternalApply);

    /**
     * 新增利用外部设备申请
     * @param deviceExternalApply
     * @return
     */
    boolean addDeviceExternalApply(DeviceExternalApply deviceExternalApply);

    /**
     * 导出利用外部设备申请
     *
     * @param externalApplyId 外部设备申请id
     * @param response
     */
    void exportDeviceExternalApply(Integer externalApplyId, HttpServletResponse response);
}

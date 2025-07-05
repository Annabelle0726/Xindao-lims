package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceScrapped;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备报废申请表 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 01:53:47
 */
public interface DeviceScrappedService extends IService<DeviceScrapped> {

    /**
     * 设备报废申请列表
     * @param page
     * @param deviceScrapped
     * @return
     */
    IPage<DeviceScrapped> pageDeviceScrapped(Page page, DeviceScrapped deviceScrapped);

    /**
     * 新增设备报废申请
     * @param deviceScrapped
     * @return
     */
    boolean addDeviceScrapped(DeviceScrapped deviceScrapped);

    /**
     * 修改设备报废申请
     * @param scrappedId 设备报废申请id
     * @return
     */
    Result exportDeviceScrapped(Integer scrappedId, HttpServletResponse response);

}

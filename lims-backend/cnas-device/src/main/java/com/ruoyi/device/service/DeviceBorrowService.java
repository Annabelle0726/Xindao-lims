package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.pojo.DeviceBorrow;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 10:53:51
 */
public interface DeviceBorrowService extends IService<DeviceBorrow> {

    IPage<DeviceBorrow> deviceBorrowPage(Page page, DeviceBorrow deviceBorrow);

    int saveDeviceBorrow(DeviceBorrow deviceBorrow);

    DeviceBorrow getDeviceBorrow(Integer id);

    List<DeviceBorrow> getDeviceBorrowBydeviceId(Integer deviceId);
}

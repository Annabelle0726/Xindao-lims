package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.pojo.DeviceCalibrationPlanDetail;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备校准计划详情表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 03:58:29
 */
public interface DeviceCalibrationPlanDetailMapper extends BaseMapper<DeviceCalibrationPlanDetail> {

    /**
     * 设备校准计划详情列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceCalibrationPlanDetail> pageDeviceCalibrationPlanDetail(Page page, @Param("ew") QueryWrapper<DeviceCalibrationPlanDetail> ew);
}

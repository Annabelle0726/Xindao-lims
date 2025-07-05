package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceAccidentReportDto;
import com.ruoyi.device.pojo.DeviceAccidentReport;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备事故报告单 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 06:31:12
 */
public interface DeviceAccidentReportMapper extends BaseMapper<DeviceAccidentReport> {

    /**
     * 设备事故报告列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceAccidentReport> pageDeviceAccidentReport(Page page, @Param("ew") QueryWrapper<DeviceAccidentReport> ew);

    /**
     * 查询设备事故报告详情
     * @param accidentReportId 设备事故报告id
     * @return
     */
    DeviceAccidentReportDto selectDeviceAccidentReportById(Integer accidentReportId);
}

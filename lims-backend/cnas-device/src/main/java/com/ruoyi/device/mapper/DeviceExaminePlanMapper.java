package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceExaminePlanDto;
import com.ruoyi.device.pojo.DeviceExaminePlan;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备核查计划主表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:04
 */
public interface DeviceExaminePlanMapper extends BaseMapper<DeviceExaminePlan> {

    /**
     * 设备核查计划列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceExaminePlanDto> deviceExaminePlanDetailsMapper(Page page, @Param("ew") QueryWrapper<DeviceExaminePlan> ew);

    /**
     * 查询设备核查计划详情
     * @param deviceExaminePlanId 设备核查计划id
     * @return
     */
    DeviceExaminePlanDto selectExamineExaminePlanDto(@Param("deviceExaminePlanId") Integer deviceExaminePlanId);
}

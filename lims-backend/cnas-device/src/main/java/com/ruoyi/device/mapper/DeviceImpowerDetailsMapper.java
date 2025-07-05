package com.ruoyi.device.mapper;

import com.ruoyi.device.dto.DeviceImpowerDetailsDto;
import com.ruoyi.device.pojo.DeviceImpowerDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备量值溯源计划详情表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:39
 */
public interface DeviceImpowerDetailsMapper extends BaseMapper<DeviceImpowerDetails> {

    /**
     * 查询设备使用详情
     * @param impowerId
     * @return
     */
    List<DeviceImpowerDetailsDto> deviceImpowerDetailsList(@Param("impowerId") Integer impowerId);
}

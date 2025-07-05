package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceInspectionRecordDto;
import com.ruoyi.device.pojo.DeviceInspectionRecord;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 04:25:14
 */
public interface DeviceInspectionRecordMapper extends BaseMapper<DeviceInspectionRecord> {

    /**
     * 分页查询设备点检记录
     * @param page
     * @param queryWrappers
     * @return
     */
    IPage<DeviceInspectionRecord> selectDeviceParameterPage(Page page, @Param("ew") QueryWrapper<DeviceInspectionRecordDto> queryWrappers);

}

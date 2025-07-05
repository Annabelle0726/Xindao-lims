package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceRecordDto;
import com.ruoyi.device.pojo.DeviceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * cnas设备使用记录表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 11:06:47
 */
public interface DeviceRecordMapper extends BaseMapper<DeviceRecord> {

    IPage<DeviceRecordDto> deviceRecordPage(@Param("deviceId") Integer deviceId, @Param("page") Page page, @Param("sampleCode") String sampleCode, @Param("managementNumber") String managementNumber, @Param("userId") Integer userId);


    /**
     * 查询未填写的设备
     * @return
     */
    List<DeviceRecordDto> selectNotFilled();

    /**
     * 查询导出设备使用记录
     * @param deviceId
     * @param exportDate
     * @return
     */
    List<DeviceRecord> selectExportList(@Param("deviceId") Integer deviceId, @Param("exportDate") String exportDate);
}

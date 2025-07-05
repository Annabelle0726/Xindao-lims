package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.inspect.dto.InsOrderDeviceRecordDto;
import com.ruoyi.inspect.pojo.InsOrderDeviceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * cnas设备使用记录表(DeviceRecord)$desc
 *
 * @author makejava
 * @since 2024-12-21 11:11:01
 */
public interface InsOrderDeviceRecordMapper extends BaseMapper<InsOrderDeviceRecord> {

    /**
     * 查询设备使用记录
     * @param insOrderId
     * @return
     */
    List<InsOrderDeviceRecordDto> selectDeviceNumber(@Param("insOrderId") Integer insOrderId);

    /**
     * 根据编号查询设备id
     * @param deviceNumbers
     * @return
     */
    List<Integer> selectDeviceIdsByNumbers(@Param("deviceNumbers") Set<String> deviceNumbers);
}


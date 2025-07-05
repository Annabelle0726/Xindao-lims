package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.pojo.ProcessOrderDevice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * cnas设备使用记录表(7.1检验委托单) Mapper 接口
 * </p>
 *
 * @author
 * @since 2025-04-17 03:51:48
 */
public interface ProcessOrderDeviceMapper extends BaseMapper<ProcessOrderDevice> {

    Set<String> selectDeviceNumber(@Param("inspectionOrderId") Integer inspectionOrderId);

    List<Integer> selectDeviceIdsByNumbers(@Param("deviceNumbers") Set<String> deviceNumbers);

    IPage<ProcessOrderDevice> deviceRecordPage(@Param("deviceId") Integer deviceId, @Param("page") Page page, @Param("sampleCode") String sampleCode, @Param("managementNumber") String managementNumber);
}

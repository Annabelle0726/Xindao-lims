package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.device.dto.DeviceExamineRecordContrastDto;
import com.ruoyi.device.pojo.DeviceExamineRecordContrast;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备核查记录对比表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:43
 */
public interface DeviceExamineRecordContrastMapper extends BaseMapper<DeviceExamineRecordContrast> {

    /**
     * 查询核查对比记录
     * @param planDetailsId
     * @return
     */
    DeviceExamineRecordContrastDto getExamineRecordContrast(Integer planDetailsId);

    /**
     * 查询对比记录用于导出
     * @param planDetailsId
     * @return
     */
    DeviceExamineRecordContrastDto selectExamineRecordContrastDto(@Param("planDetailsId") Integer planDetailsId);
}

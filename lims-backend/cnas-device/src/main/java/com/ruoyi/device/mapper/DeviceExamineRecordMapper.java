package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.device.dto.DeviceExamineRecordDto;
import com.ruoyi.device.pojo.DeviceExamineRecord;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备核查记录表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:28
 */
public interface DeviceExamineRecordMapper extends BaseMapper<DeviceExamineRecord> {

    /**
     * 查询设备核查记录
     * @param planDetailsId
     * @return
     */
    DeviceExamineRecordDto getExamineRecord(Integer planDetailsId);

    /**
     * 复核核查记录
     * @param planDetailsId 复核核查记录id
     * @return
     */
    DeviceExamineRecordDto selectReviewExamineRecordDto(@Param("planDetailsId") Integer planDetailsId);
}

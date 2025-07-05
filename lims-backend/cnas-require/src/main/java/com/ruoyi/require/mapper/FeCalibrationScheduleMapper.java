package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.pojo.FeCalibrationSchedule;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *
 * </p>
 *
 * @author 
 * @since 2024-11-13 02:53:05
 */
public interface FeCalibrationScheduleMapper extends BaseMapper<FeCalibrationSchedule> {

    IPage<FeCalibrationSchedule> ipage(Page page, @Param("instrumentName") String instrumentName, @Param("managementNumber") String managementNumber);
}

package com.ruoyi.performance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.performance.dto.AuxiliaryWorkingHoursDayDto;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日工时管理的辅助工时 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 02:22:19
 */
public interface AuxiliaryWorkingHoursDayMapper extends BaseMapper<AuxiliaryWorkingHoursDay> {

    IPage<AuxiliaryWorkingHoursDayDto> selectAuxiliaryWorkingHoursDay(Page page, @Param("ew") QueryWrapper<AuxiliaryWorkingHoursDayDto> ew, @Param("ids") List<Integer> ids);

    //查询辅助工时导出信息
    List<AuxiliaryWorkingHoursDayDto> selectDataByUser(@Param("ids") List<Integer> ids);

    List<AuxiliaryWorkingHoursDay> selectListByIds(@Param("ids") List<Integer> ids);

    //查询该月的辅助工时
    List<Map<String, Object>> totalHours(@Param("month") String month, @Param("ids") List<Integer> ids);

    List<AuxiliaryWorkingHoursDay> selectLists(@Param("ew") QueryWrapper<AuxiliaryWorkingHoursDay> ew, @Param("ids") List<Integer> ids);

    /**
     * 查询辅助工时集合
     * @param ew
     * @param ids
     * @return
     */
    List<AuxiliaryWorkingHoursDayDto> selectAuxiliaryWorkingHoursDayList(@Param("ew") QueryWrapper<AuxiliaryWorkingHoursDayDto> ew, @Param("ids") List<Integer> ids);
}

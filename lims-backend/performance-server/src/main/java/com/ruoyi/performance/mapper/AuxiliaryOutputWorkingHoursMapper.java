package com.ruoyi.performance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.performance.dto.AuxiliaryAllDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursDto;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日工时管理的产量工时 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 03:48:48
 */
public interface AuxiliaryOutputWorkingHoursMapper extends BaseMapper<AuxiliaryOutputWorkingHours> {

    IPage<AuxiliaryOutputWorkingHoursDto> selectAuxiliaryOutputWorkingHours(Page page, @Param("ew") QueryWrapper<AuxiliaryOutputWorkingHoursDto> ew, @Param("ids") List<Long> ids);

    //查询统计工时导出数据
    List<AuxiliaryOutputWorkingHoursDto> selectDataByUser(@Param("ids") List<Integer> ids);

    //查询该月的产量工时
    List<Map<String, Object>> totalHours(@Param("month") String month, @Param("ids") List<Integer> ids, @Param("type") String type);


    List<AuxiliaryOutputWorkingHours> selectListByIds(@Param("ids") List<Integer> ids);

    List<AuxiliaryOutputWorkingHours> selectLists(@Param("ew") QueryWrapper<AuxiliaryOutputWorkingHours> ew, @Param("ids") List<Integer> ids);

    /**
     * 查询
     * @param dto
     * @return
     */
    List<AuxiliaryAllDto> selectAuxiliaryAllByMonth(@Param("dto") AuxiliaryOriginalHoursLookDto dto, @Param("userIds") List<Integer> userIds);

    /**
     * 查询辅助工时
     * @param dto
     * @param userIds
     * @return
     */
    List<AuxiliaryAllDto> selectSubsidiaryAllByMonth(@Param("dto") AuxiliaryOriginalHoursLookDto dto, @Param("userIds") List<Integer> userIds);

    List<AuxiliaryOutputWorkingHoursDto> selectAuxiliaryOutputWorkingHoursList(@Param("ew") QueryWrapper<AuxiliaryOutputWorkingHoursDto> ew, @Param("ids") List<Integer> ids);
}

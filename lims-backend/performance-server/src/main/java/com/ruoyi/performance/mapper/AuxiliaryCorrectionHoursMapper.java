package com.ruoyi.performance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.performance.dto.AuxiliaryCorrectionHoursDto;
import com.ruoyi.performance.pojo.AuxiliaryCorrectionHours;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工时统计的修正工时 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-29 02:38:19
 */
public interface AuxiliaryCorrectionHoursMapper extends BaseMapper<AuxiliaryCorrectionHours> {

    IPage<AuxiliaryCorrectionHoursDto> selectAuxiliaryCorrectionHours(Page page, @Param("ew") QueryWrapper<AuxiliaryCorrectionHoursDto> ew, @Param("ids") List<Integer> ids);

    List<Integer> selDepartLimsByName(String departLims);

}

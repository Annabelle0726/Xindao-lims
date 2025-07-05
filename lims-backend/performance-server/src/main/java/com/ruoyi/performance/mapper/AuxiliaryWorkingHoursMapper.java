package com.ruoyi.performance.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHours;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 辅助工时 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-09 06:58:31
 */
public interface AuxiliaryWorkingHoursMapper extends BaseMapper<AuxiliaryWorkingHours> {
    IPage<AuxiliaryWorkingHours> selectAuxiliaryWorkingHours(@Param("page") Page page, @Param("ew") QueryWrapper<AuxiliaryWorkingHours> ew);

    /**
     * 绑定辅助工时配置的日工时管理数量
     *
     * @param auxiliaryWorkingHoursId
     * @return
     */
    int hourDayBindAuxiliaryCount(@Param("auxiliaryWorkingHoursId") Integer auxiliaryWorkingHoursId);
}

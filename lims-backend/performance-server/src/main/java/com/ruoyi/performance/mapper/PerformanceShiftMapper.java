package com.ruoyi.performance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.framework.mybatis_config.MyBaseMapper;
import com.ruoyi.performance.dto.PerformanceShiftMapDto;
import com.ruoyi.performance.pojo.PerformanceShift;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 绩效管理-班次 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-08 09:12:04
 */
public interface PerformanceShiftMapper extends MyBaseMapper<PerformanceShift> {

    IPage<PerformanceShiftMapDto> performanceShiftPage(
            Page<Object> page,
            @Param("time") String time,
            @Param("userName") String userName,
            @Param("laboratory") String laboratory
    );

    List<Map<String, Object>> performanceShiftYearPage(@Param("time") String time,
                                                       @Param("userName") String userName,
                                                       @Param("laboratory") String laboratory);

    IPage<Map<String, Object>> performanceShiftYear(@Param("page") Page<Object> page, @Param("time") String time, @Param("userName") String userName, @Param("laboratory") String laboratory);

    List<Map<String, Object>> performanceShiftYearList(@Param("time") String time, @Param("userName") String userName, @Param("laboratory") String laboratory);

    List<PerformanceShiftMapDto> performanceShiftList(@Param("time") String time, @Param("userName") String userName, @Param("laboratory") String laboratory);

    String seldepLimsId(@Param("depLimsId") int depLimsId);
}

package com.ruoyi.performance.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuxiliaryOriginalHoursMapper {

    //查询该月的总工时
    List<Map<String, Object>> totalHours(@Param("month") String month, @Param("ids") List<Integer> ids);

}

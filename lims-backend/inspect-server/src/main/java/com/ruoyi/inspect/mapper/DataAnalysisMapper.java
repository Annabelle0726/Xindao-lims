package com.ruoyi.inspect.mapper;

import com.ruoyi.basic.dto.IfsInventoryQuantitySupplierDto;
import com.ruoyi.inspect.dto.DataAnalysisDto;
import com.ruoyi.inspect.vo.RawMaterialSupplierVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据分析
 *
 * @Author zhuo
 * @Date 2024/10/16
 */
@Mapper
public interface DataAnalysisMapper {

    /**
     * 获取本周的原材料信息
     * @return
     */
    List<Map<String, Object>> getRawPassRateByBarChartByWeek(@Param("dto") DataAnalysisDto dataAnalysisDto);

    /**
     * 获取本月的原材料信息
     * @return
     */
    List<Map<String, Object>> getRawPassRateByBarChartByDay(@Param("dto") DataAnalysisDto dataAnalysisDto);

    /**
     * 获取本年的原材料信息
     * @return
     */
    List<Map<String, Object>> getRawPassRateByBarChartByYear(@Param("dto") DataAnalysisDto dataAnalysisDtor);

    /**
     * 查看原材料饼状图
     * @return
     */
    Map<String, Object> getRawPassRateByCake(@Param("dto") DataAnalysisDto dataAnalysisDto);


    /**
     * 查询检测项集合
     * @param dataAnalysisDto
     * @return
     */
    List<IfsInventoryQuantitySupplierDto> getRawProductAnalysisAllSample(@Param("dto") DataAnalysisDto dataAnalysisDto);

    /**
     * 查询所有的
     * @param dataAnalysisDto
     * @return
     */
    List<IfsInventoryQuantitySupplierDto> getRawProductAnalysisList(@Param("dto") DataAnalysisDto dataAnalysisDto);


    List<RawMaterialSupplierVo> getItemValueByOrderIds(@Param("insOrderIds") List<Integer> insOrderIds, @Param("itemName") String itemName);

    /**
     * 查询本月与上月合格率对比
     * @return
     */
    List<Map<String, Object>> getRawUpMonth();

    /**
     * 查询检验项类型饼图
     * @return
     */
    Map<String, Object> getOrderTypeCookie();
}

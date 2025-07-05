package com.ruoyi.inspect.service;


import com.ruoyi.basic.dto.IfsInventoryQuantitySupplierDto;
import com.ruoyi.inspect.dto.DataAnalysisDto;
import com.ruoyi.inspect.vo.DeviationAnalyzeVo;
import com.ruoyi.inspect.vo.RawProductAnalysisVo;

import java.util.List;
import java.util.Map;

/**
 * 数据分析
 *
 * @Author zhuo
 * @Date 2024/10/16
 */
public interface DataAnalysisService {

    /**
     * 查询原材料柱状统计
     * @return
     */
    List<Map<String, Object>> getRawPassRateByBarChart(DataAnalysisDto dataAnalysisDto);

    /**
     * 查询原材料合格率饼状图
     * @param dataAnalysisDto
     * @return
     */
    Map<String, Object> getRawPassRateByCake(DataAnalysisDto dataAnalysisDto);


    /**
     * 查询检验项名称
     * @param dataAnalysisDto
     * @return
     */
    List<String> getRawItemNames(DataAnalysisDto dataAnalysisDto);

    /**
     * 查询原材料项检分析
     * @param dataAnalysisDto
     * @return
     */
    RawProductAnalysisVo getRawProductAnalysis(DataAnalysisDto dataAnalysisDto);

    /**
     * 查询检测项分析列表
     * @param dataAnalysisDto
     * @return
     */
    List<IfsInventoryQuantitySupplierDto> getRawProductAnalysisAllList(DataAnalysisDto dataAnalysisDto);

    /**
     * 查询原材料项检分析合格率
     * @param dataAnalysisDto
     * @return
     */
    RawProductAnalysisVo getRawProductAnalysisRawPass(DataAnalysisDto dataAnalysisDto);

    /**
     * 查询原材料项检和厂家数据对比
     * @param dataAnalysisDto
     * @return
     */
    DeviationAnalyzeVo getRawSupplierCompare(DataAnalysisDto dataAnalysisDto);

    /**
     * 查询本月与上月合格率对比
     * @param dataAnalysisDto
     * @return
     */
    List<Map<String, Object>> getRawUpMonth(DataAnalysisDto dataAnalysisDto);

    Map<String, Object> getOrderTypeCookie(DataAnalysisDto dataAnalysisDto);
}

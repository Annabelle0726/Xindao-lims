package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.IfsStockQueryDTO;
import com.ruoyi.inspect.dto.SpotCheckQuarterDto;
import com.ruoyi.inspect.dto.SpotCheckYearDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成品抽样
 *
 * @Author zhuo
 * @Date 2024/9/29
 */
public interface FinishProductSpotCheckService {

    /**
     * 查询ifs成品库存
     *
     * @return
     */
    Result getIfsStockReport(IfsStockQueryDTO ifsStockQueryDTO);


    /*******************************************************  季度抽样 *******************************************************/

    /**
     * 新增季度抽检
     * @param spotCheckQuarterDto
     * @return
     */
    boolean addQuarter(SpotCheckQuarterDto spotCheckQuarterDto);

    /**
     * 查询季度抽样详情
     * @param quarterId
     * @return
     */
    SpotCheckQuarterDto getQuarter(Integer quarterId);

    /**
     * 季度抽样分页查询
     * @param page
     * @param spotCheckQuarter
     * @return
     */
    IPage<SpotCheckQuarterDto> getQuarterPage(Page page, SpotCheckQuarterDto spotCheckQuarter);

    /**
     * 删除季度抽样
     * @param quarterId
     * @return
     */
    boolean deleteQuarter(Integer quarterId);

    /**
     * 成品下单界面查询季度信息
     * @return
     */
    List<Map<String, Object>> getQuarterOnOrder();

    /**
     * 修改季度检验
     * @param spotCheckQuarterDto
     * @return
     */
    boolean updateQuarterOnOrder(SpotCheckQuarterDto spotCheckQuarterDto);

    /**
     * 生成最终报告
     * @param quarterId
     * @return
     */
    boolean finalReportQuarter(Integer quarterId, HttpServletResponse response);


    /*******************************************************  年度抽样 *******************************************************/

    /**
     * 新增年度抽样
     * @param spotCheckYearDto
     * @return
     */
    boolean addSpotCheckYear(SpotCheckYearDto spotCheckYearDto);

    /**
     * 查询年度抽样详情
     * @param yearId
     * @return
     */
    SpotCheckYearDto getSpotCheckYear(Integer yearId);

    /**
     * 年度抽样列表
     * @param page
     * @param spotCheckYear
     * @return
     */
    IPage<SpotCheckYearDto> getSpotCheckYearPage(Page page, SpotCheckYearDto spotCheckYear);

    /**
     * 删除年度抽样
     * @param yearId
     * @return
     */
    boolean deleteSpotCheckYear(Integer yearId);

    /**
     * 修改年度检验
     * @param spotCheckYearDto
     * @return
     */
    boolean updateSpotCheckYear(SpotCheckYearDto spotCheckYearDto);

    /**
     * 生成最终年度报告
     * @param yearId
     * @return
     */
    boolean finalReportSpotCheckYear(Integer yearId, HttpServletResponse response);
}

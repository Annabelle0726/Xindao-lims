package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.StructureTestObject;
import com.ruoyi.inspect.dto.SampleOrderDto;
import com.ruoyi.inspect.dto.SampleProductDto2;
import com.ruoyi.inspect.dto.SampleProductExportDto;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.vo.InsOrderPrintingVo;
import com.ruoyi.inspect.vo.SampleDefectsFatherVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gaoaoy
 * @description 针对表【ins_order(检验下单)】的数据库操作Mapper
 * @createDate 2024-03-12 16:17:55
 * @Entity com.yuanchu.mom.pojo.InsOrder
 */
public interface InsOrderMapper extends BaseMapper<InsOrder> {

    //获取检验下单数据
    IPage<SampleOrderDto> selectInsOrderPage(@Param("page") IPage<InsOrder> page, @Param("ew") QueryWrapper<SampleOrderDto> ew, @Param("laboratory") String laboratory, @Param("isOrderAll") String isOrderAll);


    String getLaboratoryCode(@Param("name") String name);

    IPage<SampleProductDto2> selectSampleAndProductByOrderId(@Param("page") IPage<SampleProductDto2> page, @Param("ew") QueryWrapper<SampleProductDto2> ew, @Param("id") Integer id);

    List<Map<String, String>> selectDeviceList(@Param("managementNumbers") Set<String> managementNumbers);
    List<SampleDefectsFatherVo> selectSampleDefects(Page page, @Param("inspectionItems") String inspectionItems, @Param("orderNumber") String orderNumber);

    String getStandardMethodCode(@Param("id") Integer id);

    int updateStatus(@Param("id") Integer id);

    String selLaboratoryCode(@Param("laboratory") String laboratory);

    List<Map<Integer, Object>> selectReportModelByOrderId(@Param("id") Integer id, @Param("laboratory") String laboratory);

    String seldepLimsId(@Param("depLimsId") Integer depLimsId);

    /**
     * 查询检验结果
     * @param ids
     * @return
     */
    List<SampleProductExportDto> selectSampleBySampleId(@Param("ids") List<Integer> ids);

    /**
     * 获取批量检验的总数
     * @param id
     * @return
     */
    int selectSampleMax(@Param("id") Integer id);

    /**
     * 获取线芯颜色
     * @param id
     * @return
     */
    List<String> selectSampleCableTag(@Param("id") Integer id);

    /**
     *
     * @param orderId
     * @return
     */
    int selectSampleItemS(@Param("orderId") Integer orderId);

    /**
     * 只查询默认第一个的产品
     * @param id
     * @return
     */
    List<SampleProductExportDto> selectSampleBySampleIdOne(@Param("id") Integer id);

    /**
     * 查询日历图
     * @param
     * @return
     */
    List<Map<String, Object>> selectCalendarWorkByWeek(@Param("day") Integer day);

    /**
     *
     * @param partNo
     * @return
     */
    StructureTestObject selectProductByPartNo(@Param("partNo") String partNo);

    /**
     *
     * @param partNo
     * @return
     */
    StructureTestObject selectByPartNo(@Param("partNo") String partNo);

    /**
     * 成品标签打印
     * @param ids
     * @return
     */
    List<InsOrderPrintingVo> labelOrderPrinting(@Param("ids") List<Integer> ids);

    /**
     * 查询导出信息
     * @return
     */
    List<SampleOrderDto> rawAllInsOrderExport(@Param("ew") QueryWrapper<SampleOrderDto> ew, @Param("laboratory") String laboratory, @Param("isOrderAll") String isOrderAll);

    /**
     * 根据订单查询试验室
     * @param insOrderId
     * @return
     */
    String selectLaboratoryByOrderId(@Param("insOrderId") Integer insOrderId);

    /**
     * 查询订单信息
     * @param insSampleId
     * @return
     */
    InsOrder selectFirstSubmit(@Param("insSampleId") Integer insSampleId);

    /**
     * 根据订单id查询订单导出信息
     * @param orderIds
     * @return
     */
    List<SampleOrderDto> getInsOrderExportByIds(@Param("orderIds") List<String> orderIds);

    /**
     * 查询当前时间是否有该检测项的抽样计划订单
     * @param itmeId    样品Id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    Integer selectNotSpotCheckOrder(@Param("itmeId") Integer itmeId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}

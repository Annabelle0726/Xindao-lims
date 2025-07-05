package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.pojo.InsUnqualifiedRetestProduct;
import com.ruoyi.inspect.dto.*;
import com.ruoyi.inspect.vo.InsOrderPrintingVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author gaoaoy
 * @description 针对表【ins_order(检验下单)】的数据库操作Service
 * @createDate 2024-03-12 16:17:55
 */
public interface InsOrderService extends IService<InsOrder> {

    //获取单位检验下单数据
    IPage<SampleOrderDto> selectInsOrderParameter(IPage<InsOrder> page, SampleOrderDto sampleOrderDto);


    //修改检验下单数据
    int upInsOrder(Integer orderId,Integer sampleId, String appointed, Integer userId,String sonLaboratory);

    int addInsOrder(List<SampleProductDto> list, InsOrder insOrder);

    Map<String, Object> getInsOrder(Integer id);

    int upInsOrderOfState(InsOrder insOrder);

    Map<String, Object> getInsOrderAndSample(Integer id, String laboratory);

    IPage<SampleProductDto2> selectSampleAndProductByOrderId(IPage<SampleProductDto2> page, SampleProductDto2 sampleProductDto);


    int updateStatus(Integer id);


    // 获取ifs库存信息
    void getIfsOrder();

    /**
     * 修改订单单号
     * @param insOrder
     */
    void updateEntrustCode(InsOrder insOrder);

    List<InsUnqualifiedRetestProduct> getRetestResult(Integer insProductId);



    void updateIfsInventoryQuantity(Integer id);

    /**
     * 铜材下单
     * @param list
     * @param copperInsOrder
     * @return
     */
    int addRawCopperOrder(List<SampleProductDto> list, CopperInsOrderDto copperInsOrder);

    /**
     * 修改委托编号
     * @param insOrder
     */
    void updateOrderEntrustCode(InsOrder insOrder);


    /**
     * 修改检验下单内容
     * @param insOrderUpdateDto
     * @return
     */
    Boolean updateInsOrder(InsOrderUpdateDto insOrderUpdateDto);

    /**
     * 成品标签打印
     * @param ids
     * @return
     */
    List<InsOrderPrintingVo> labelOrderPrinting(List<Integer> ids);

    /**
     * 根据样品id查询检验项树
     * @param insSampleId
     * @return
     */
    List<StandardProductList> getProductTreeBySampleId(Integer insSampleId);

    /**
     * 添加遗漏的检验项
     * @param omitOrderProductDto
     * @return
     */
    boolean addOmitOrderProduct(OmitOrderProductDto omitOrderProductDto);

    /**
     * 成品检验单导出
     * @param sampleOrderDto
     * @param response
     */
    void rawAllInsOrderExport(SampleOrderDto sampleOrderDto, HttpServletResponse response);

    /**
     * 修改样品型号
     * @param insSample
     */
    void updateSampleModel(InsSample insSample);

    /**
     * 查询当前时间是否没有该检测项的抽样计划
     * @param sampleList
     * @param insOrder
     * @return
     */
    Result judgeNotSpotCheckOrder(List<SampleProductDto> sampleList, InsOrder insOrder);
}

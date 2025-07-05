package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.InsProductAnalysisDto;
import com.ruoyi.inspect.dto.ProductResultDto2;
import com.ruoyi.inspect.dto.SampleProductRawAnalysisDto;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.pojo.InsProductDeviationWarningDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【ins_product(检验项目)】的数据库操作Mapper
* @createDate 2024-03-08 09:45:03
* @Entity com.yuanchu.mom.pojo.InsProduct
*/
public interface InsProductMapper extends BaseMapper<InsProduct> {

    int selectOrderManDay(@Param("orderId") Integer orderId);

    Map<String, String> selectUserById(@Param("userId") Integer userId);

    List<ProductResultDto2> getProductAndResult(@Param("sampleId") Integer sampleId);

    int updateInspected(@Param("id") Integer id);

    List<InsProduct> selectFiberInsProduct(@Param("ids") List<Integer> ids, @Param("laboratory") String laboratory);

    IPage<InsProduct> selectNoProducts(@Param("page") Page page, @Param("orderId") Integer orderId, @Param("noIds") List<Integer> noIds);

    int selectInsProductCountByOrderId(@Param("orderId") Integer orderId);

    /**
     * 根据样品id查询检验项目
     * @param sampleIds
     * @return
     */
    List<SampleProductRawAnalysisDto> selectListBySampleIds(@Param("sampleIds") List<Integer> sampleIds);

    /**
     * 查询所有的检验项
     * @param sampleIds
     * @return
     */
    List<String> selectItemNameBySampleIds(@Param("sampleIds") List<Integer> sampleIds);

    /**
     * 根据订单id查询不合格项
     * @param orderId
     * @return
     */
    List<String> selectUnqualifiedList(@Param("orderId") Integer orderId);

    /**
     * 查询判断是否有不判定项目,和全都是判定项
     * @param orderId
     * @return
     */
    int selectNoJudge(@Param("orderId") Integer orderId);

    /**
     * 根据订单查询产品信息
     * @param orderId
     * @return
     */
    List<InsProduct> selectProductByOrderId(Integer orderId);

    /**
     * 查询同一型号, 同一厂家, 同一检测项的检测数据
     * @param insProduct
     * @param supplierName
     * @return
     */
    List<InsProductDeviationWarningDetail> selectAnalysis(@Param("insProduct") InsProduct insProduct, @Param("supplierName") String supplierName);
}





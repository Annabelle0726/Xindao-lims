package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.InsOrderPlanDTO;
import com.ruoyi.inspect.dto.SampleProductDto;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.vo.InsOrderPlanTaskSwitchVo;
import com.ruoyi.inspect.vo.InsOrderPlanVO;
import com.ruoyi.inspect.vo.SampleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【ins_sample(检验样品)】的数据库操作Mapper
* @createDate 2024-03-14 17:14:57
* @Entity com.yuanchu.mom.pojo.InsSample
*/
public interface InsSampleMapper extends BaseMapper<InsSample> {

    IPage<InsOrderPlanVO> findInsSampleAndOrder(Page page,
                                                @Param("ew") QueryWrapper<InsOrderPlanDTO> ew,
                                                @Param("userName") String userName ,
                                                @Param("userId") Integer userId,
                                                @Param("sonLaboratory") String sonLaboratory,
                                                @Param("laboratory") String laboratory,
                                                @Param("isCheck") Integer isCheck);

    IPage<InsOrderPlanTaskSwitchVo> inspectionOrderDetailsTaskSwitching(Page page, @Param("ew") QueryWrapper<InsOrderPlanDTO> ew, @Param("userId") Integer userId, @Param("sonLaboratory") String sonLaboratory, @Param("laboratory") String laboratory);

    List<SampleProductDto> selectSampleProductListByOrderId(@Param("id") Integer id);

    List<SampleProductDto> selectSampleProductListByOrderId2(@Param("id") Integer id);

    Map<String,Object> selectInsOrder(@Param("id") Integer id);

    List<SampleProductDto> getInsOrderAndSample(@Param("id") Integer id, @Param("laboratory") String laboratory);

    String getSampleEn(@Param("sample") String sample);

    /**
     * 可能没有产品直接就是对象
     * @param sample
     * @return
     */
    String getSampleEnByObject(@Param("sample") String sample);

    /**
     * 样品
     * @param id
     * @param laboratory
     * @return
     */
    List<InsProduct> getInsProduct1(@Param("id") Integer id, @Param("laboratory") String laboratory, @Param("cableTag") String cableTag, @Param("repetitionTag") String repetitionTag);


    /**
     * 原材料
     * @param id
     * @param laboratory
     * @param rawMaterialTag
     * @return
     */
    List<InsProduct> getInsProduct6(@Param("id") Integer id, @Param("laboratory") String laboratory, @Param("rawMaterialTag") String rawMaterialTag);

    SampleVo getDetailById(@Param("sampleId") Integer sampleId);

    String selMethodById(@Param("sampleId") Integer sampleId);

    /**
     * 电缆配置查看配置标识
     * @param id
     * @param laboratory
     * @return
     */
    List<Map<String, Object>> getCableTag(@Param("id") Integer id, @Param("laboratory") String laboratory);

    /**
     * 原材料查看配置标识
     * @param id
     * @param laboratory
     * @return
     */
    List<Map<String, Object>> getRawMaterialTag(@Param("id") Integer id, @Param("laboratory") String laboratory);

    /**
     * 查看重复标识
     * @param id
     * @param laboratory
     * @return
     */
    List<Map<String, Object>> getRepetitionTag(@Param("id") Integer id, @Param("laboratory") String laboratory, @Param("cableTag") String cableTag);


    List<SampleProductDto> selectExemptionByOrderId(@Param("id") Integer id);

    /**
     * 查询厂家密度(有型号)
     * @param sample
     * @param production
     * @return
     */
    String selectSupplierDensityModel(@Param("sample") String sample, @Param("production") String production, @Param("model") String model);

    /**
     * 查询厂家密度(没型号)
     * @param sample
     * @param production
     * @return
     */
    String selectSupplierDensity(@Param("sample") String sample, @Param("production") String production);


    /**
     * 查询检验项检验结果
     * @param itemIds
     * @return
     */
    List<InsProduct> selectProductResult(@Param("itemIds") List<Integer> itemIds);

    /**
     * 根据订单id查询样品
     * @param insOrderId
     * @return
     */
    List<InsSample> getSampleByOrderId(Integer insOrderId);
}





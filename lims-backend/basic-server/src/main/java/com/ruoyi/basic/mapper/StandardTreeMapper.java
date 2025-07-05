package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.*;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.basic.pojo.StandardMethodList;
import com.ruoyi.basic.pojo.StandardProductList;
import com.ruoyi.basic.pojo.StandardTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 * @description 针对表【standard_tree(标准树)】的数据库操作Mapper
 * @createDate 2024-03-01 15:06:44
 * @Entity com.ruoyi.basic.pojo.StandardTree
 */
public interface StandardTreeMapper extends BaseMapper<StandardTree> {

    List<FactoryDto> selectStandardTreeList();

    List<StandardTree> selectStandardTreeList2(String sampleType);

    StandardProductList selectStandardProductById(Integer id);

    List<StandardProductList> getStandardProductListBySample(String sampleType);

    List<StandardProductList> selectStandardProductListByTree(@Param("tree") String tree, @Param("sample") String sample, @Param("model") String model, @Param("trees") String trees, @Param("laboratory") String laboratory);

    List<StandardProductList> selectStandardProductListByTree2(@Param("tree") String tree, @Param("sample") String sample, @Param("model") String model, @Param("trees") String trees, @Param("laboratory") String laboratory);


    List<SampleTypeDto> getStandardTree2();

    String getLaboratory(String str);

    Integer getStructureItemParameterId(String sampleType, String item, String itemChild, String inspectionItemClass);

    List<ProductDto> selectPList(String name);

    String selSample(String sample);

    List<FactoryDto> selectStandardTreeListByPartNo(@Param("partNo") String partNo);

    IPage<IfsInventoryQuantity> selectIfsPage(IPage<IfsInventoryQuantity> page,
                                              @Param("ew") QueryWrapper<IfsInventoryQuantity> ew);

    IPage<IfsInventoryQuantityDto> getIfsByStateOne(IPage<IfsInventoryQuantityDto> page,
                                                    @Param("ew") QueryWrapper<IfsInventoryQuantityDto> ew);


    IPage<IfsInventoryQuantityCheckDto> selectIfsInventoryQuantity(Page<IfsInventoryQuantityCheckDto> page, @Param("ew")QueryWrapper<IfsInventoryQuantityCheckDto> ew);

    /**
     * 原材报检查询全部(分页)
     * @param page
     * @param ew
     * @return
     */
    IPage<IfsInventoryQuantitySupplierDto> getIfsByOver(Page<IfsInventoryQuantitySupplierDto> page, @Param("ew") QueryWrapper<IfsInventoryQuantitySupplierDto> ew, @Param("beginDeclareDate") String beginDeclareDate, @Param("endDeclareDate")String endDeclareDate);

    /**
     * 原材报检查询全部(无分页)
     * @param ew
     * @return
     */
    List<IfsInventoryQuantitySupplierDto> getIfsByOverList(@Param("ew") QueryWrapper<IfsInventoryQuantitySupplierDto> ew, @Param("beginDeclareDate") String beginDeclareDate, @Param("endDeclareDate")String endDeclareDate);

    /**
     * 原材料查询季度检验
     * @param page
     * @param
     * @param beginDeclareDate
     * @param endDeclareDate
     * @return
     */
    IPage<IfsInventoryQuantitySupplierDto> getIfsByQuarter(Page<IfsInventoryQuantitySupplierDto> page, @Param("ew") QueryWrapper<IfsInventoryQuantitySupplierDto> ew, @Param("beginDeclareDate") String beginDeclareDate, @Param("endDeclareDate")String endDeclareDate);

    /**
     * 批量查询树
     * @param sampleTypeValues
     * @return
     */
    List<SampleDto> getStandardTree3Batch(@Param("sampleTypeValues") Set<String> sampleTypeValues);

    /**
     * 查询树
     * @param sampleType
     * @return
     */
    List<SampleDto> getStandardTree3(@Param("sampleType") String sampleType);

    /**
     * 根据原材料id列表查询原材料信息
     * @param ifsIds
     * @return
     */
    List<IfsInventoryQuantitySupplierDto> getIfsByIds(@Param("ifsIds") List<String> ifsIds);

}





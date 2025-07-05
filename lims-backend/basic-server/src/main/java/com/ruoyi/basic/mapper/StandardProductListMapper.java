package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.basic.pojo.StandardProductList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【standard_product_list(标准树下的检验项目)】的数据库操作Mapper
* @createDate 2024-03-05 10:33:29
* @Entity com.ruoyi.basic.pojo.StandardProductList
*/
public interface StandardProductListMapper extends BaseMapper<StandardProductList> {

    IPage<StandardProductList> standardProductListIPage(@Param("id") Integer id, @Param("tree") String tree, IPage<StandardProductList> page, @Param("laboratory") String laboratory, @Param("insItem") String insItem, @Param("insItems") String insItems);

    StandardProductList getOne(@Param("standardMethodListId") Integer standardMethodListId, @Param("inspectionItem") String inspectionItem, @Param("sample") String sample, @Param("inspectionItemSubclass") String inspectionItemSubclass, @Param("model") String model, @Param("inspectionItemClass") String inspectionItemClass);

    List<StandardProductList> selectDetail(@Param("standardMethodListId") Integer standardMethodListId, @Param("state") int state, @Param("model") String model, @Param("isCableTag") String isCableTag);

    List<StandardProductList> selectDetail2(@Param("standardMethodListId") Integer standardMethodListId, @Param("state") int state, @Param("tree") String tree, @Param("isCableTag") String isCableTag);

    /**
     * 查询零件颜色
     * @param partNo
     * @return
     */
    Map<String, String> selectPartColor(@Param("partNo") String partNo);

    /**
     * 区间修改
     * @param productList
     * @return
     */
    void updateSection(@Param("productList") StandardProductList productList);


    /**
     * 从上往下修改顺序
     * @param beginIndex 开始位置
     * @param endIndex 结束位置
     * @param methodId 标准id
     * @param tree 树
     */
    void updateSortUp(@Param("beginIndex") Integer beginIndex,
                      @Param("endIndex") Integer endIndex,
                      @Param("methodId") Integer methodId,
                      @Param("tree") String tree);

    /**
     * 从下网上修改顺序
     * @param beginIndex
     * @param endIndex
     * @param methodId
     * @param tree
     */
    void updateSortDown(@Param("beginIndex") Integer beginIndex,
                        @Param("endIndex") Integer endIndex,
                        @Param("methodId") Integer methodId,
                        @Param("tree") String tree);

    /**
     * 批量添加标准
     * @param productLists
     */
    void saveBatchProductLists(@Param("productLists") List<StandardProductList> productLists);
}





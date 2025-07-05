package com.ruoyi.basic.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.dto.CopyStandardProductListDto;
import com.ruoyi.basic.dto.InsSampleReceiveDto;
import com.ruoyi.basic.dto.ResetTreeDragDTO;
import com.ruoyi.basic.pojo.StandardProductList;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【standard_product_list(标准树下的检验项目)】的数据库操作Service
 * @createDate 2024-03-05 10:33:29
 */
public interface StandardProductListService extends IService<StandardProductList> {

    int upStandardProductList(StandardProductList list);

    List<StandardProductList> selectStandardProductList(InsSampleReceiveDto insSample);

    Map<String, Object> selectStandardProductListByMethodId(Integer id, String tree);

    IPage<StandardProductList> selectStandardProductByMethodId(Integer id, String tree, Integer page, String laboratory, String item, String items);

    Map<String, List<?>> selectStandardProductEnumByMethodId(Integer id, String tree, String item);

    boolean updateSection(StandardProductList list);

    /**
     * 标准库拖拽
     * @param resetTreeDragDTO
     */
    void resetTreeDrag(ResetTreeDragDTO resetTreeDragDTO);

    /**
     * 标准库拖拽
     * @param standardProductLists
     */
    void resetTreeDragBatch(List<StandardProductList> standardProductLists);

    /**
     * 检验项要求值对比
     * @param copyStandardProductListDto
     * @return
     */
    List<StandardProductList> copyStandardProductList(CopyStandardProductListDto copyStandardProductListDto);

    /**
     * 检验项要求值对比一个
     * @param copyStandardProductListDto
     * @return
     */
    List<StandardProductList> copyStandardProductOne(CopyStandardProductListDto copyStandardProductListDto);

    /**
     * 检验项复制排序
     * @param copyStandardProductListDto
     * @return
     */
    boolean copyStandardProductSort(CopyStandardProductListDto copyStandardProductListDto);
}

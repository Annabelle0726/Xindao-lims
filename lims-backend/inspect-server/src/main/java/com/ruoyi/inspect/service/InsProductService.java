package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.dto.InsProductBindingDto;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【ins_product(检验项目)】的数据库操作Service
* @createDate 2024-03-17 23:57:39
*/
public interface InsProductService extends IService<InsProduct> {

    int selectOrderManDay(Integer orderId);

    int updateInspected(Integer id,String ids);

    boolean write(InsOrder insOrder);

    IPage<InsProduct> selectNoProducts(Page page, InsProduct insProduct, Integer orderId, String ids);

    void checkUpdate(Integer orderId,Integer state);


    /**
     * 根据检验项id查询检验项树信息
     * @param productId
     * @return
     */
    List<InsProduct> getProductTreeByProductId(Integer productId);


    /**
     * 特殊检验项绑定
     * @param insProductBindingDto
     * @return
     */
    boolean bindingProductTreeByProductId(InsProductBindingDto insProductBindingDto);

    /**
     * 删除特殊检验项绑定信息
     * @param productId
     * @return
     */
    boolean removeBindingProductTree(Integer productId);
}

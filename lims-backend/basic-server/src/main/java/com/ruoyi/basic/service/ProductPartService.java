package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.ProductPart;

public interface ProductPartService extends IService<ProductPart> {

    IPage<ProductPart> selectByProductId(IPage<ProductPart> page,ProductPart productPart);

    void addProductPart(ProductPart productPart);

    void updateProductPartById(ProductPart productPart);
}

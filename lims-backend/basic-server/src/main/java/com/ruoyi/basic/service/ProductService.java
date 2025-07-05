package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.dto.ProductDTO1;
import com.ruoyi.basic.excel.StructureTestObjectData;
import com.ruoyi.basic.pojo.Product;

import java.util.List;

/**
* @author z1292
* @description 针对表【product(产品表)】的数据库操作Service
* @createDate 2024-04-26 01:11:02
*/
public interface ProductService extends IService<Product> {

    IPage<Product> selectProductListByObjectId(Page page, ProductDTO1 productDto);

    int addProduct(Product product);

    int upProduct(Product product);

    int delProduct(Integer id);

    void importPartExcel(List<StructureTestObjectData> list);
}

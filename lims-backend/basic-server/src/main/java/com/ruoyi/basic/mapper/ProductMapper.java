package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.ProductDTO1;
import com.ruoyi.basic.pojo.Product;
import org.apache.ibatis.annotations.Param;

/**
* @author z1292
* @description 针对表【product(产品表)】的数据库操作Mapper
* @createDate 2024-04-26 01:11:02
* @Entity com.ruoyi.basic.pojo.Product
*/
public interface ProductMapper extends BaseMapper<Product> {

    IPage<Product> selectProductListByObjectId(Page page, @Param("ew") QueryWrapper<ProductDTO1> ew, @Param("partNo") String partNo);
}





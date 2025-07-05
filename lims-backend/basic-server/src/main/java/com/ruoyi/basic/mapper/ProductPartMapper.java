package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.basic.pojo.ProductPart;
import org.apache.ibatis.annotations.Param;

public interface ProductPartMapper extends BaseMapper<ProductPart> {
    IPage<ProductPart> selectListByProductId(IPage<ProductPart> page,
                                              @Param("ew") QueryWrapper<ProductPart> ew,
                                              @Param("productId") Integer productId);
}

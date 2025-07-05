package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.pojo.InsUnqualifiedRetestProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 不合格检验项目复测
 * @author 鷟
 * @since 2024-09-03
 */
@Mapper
public interface InsUnqualifiedRetestProductMapper extends BaseMapper<InsUnqualifiedRetestProduct> {

    /**
     * 查询不合格内容
     * @param ids
     * @return
     */
    List<InsUnqualifiedRetestProduct> selectRetestProduct(@Param("ids") List<Integer> ids);

    // 样品
    List<InsProduct> getInsProductUnqualifiedRetest1(@Param("id") Integer id, @Param("laboratory") String laboratory, @Param("cableTag") String cableTag, @Param("repetitionTag") String repetitionTag, @Param("retestTag") String retestTag);


    // 原材料下单
    List<InsProduct> getInsProductUnqualifiedRetest6(@Param("id") Integer id, @Param("laboratory") String laboratory, @Param("rawMaterialTag") String rawMaterialTag , @Param("retestTag") String retestTag);
}


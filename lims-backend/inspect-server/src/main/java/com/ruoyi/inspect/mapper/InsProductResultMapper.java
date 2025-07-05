package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.inspect.pojo.InsProductResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【ins_product_result(检验项目的结果)】的数据库操作Mapper
 * @createDate 2024-03-28 10:29:37
 * @Entity com.yuanchu.mom.pojo.InsProductResult
 */
public interface InsProductResultMapper extends BaseMapper<InsProductResult> {

    List<InsProductResult> selDetail(@Param("ips") List<Integer> ips);

    /**
     * 根据订单id查询检验结果
     * @param orderId
     * @return
     */
    List<InsProductResult> selectResultByOrderId(@Param("orderId") Integer orderId);
}





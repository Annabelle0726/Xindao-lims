package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.basic.dto.IfsInventoryQuantityDto;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface IfsInventoryQuantityMapper extends BaseMapper<IfsInventoryQuantity> {

    /**
     * 打印标签查询
     * @param ids
     * @return
     */
    List<IfsInventoryQuantityDto> printLabel(@Param("ids") List<Integer> ids);

    /**
     *
     * @param ifsInventoryId
     * @return
     */
    int selectReportCountById(@Param("ifsInventoryId") Integer ifsInventoryId);


    /**
     * 查询当前季度是否出现过该材料
     * @param partDetail         型号
     * @param supplierName       供应商名称
     * @param startOfNextQuarter 季度开始时间
     * @param endOfQuarter       季度结束时间
     * @return
     */
    Integer selectIsFirst(@Param("partDetail") String partDetail,
                          @Param("supplierName") String supplierName,
                          @Param("startOfNextQuarter") LocalDateTime startOfNextQuarter,
                          @Param("endOfQuarter") LocalDateTime endOfQuarter);
}

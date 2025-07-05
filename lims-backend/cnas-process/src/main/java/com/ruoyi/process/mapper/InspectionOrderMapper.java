package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.process.dto.InspectionOrderDto;
import com.ruoyi.process.pojo.InspectionOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验委托单 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-09
 */
public interface InspectionOrderMapper extends BaseMapper<InspectionOrder> {

    /**
     * 检验委托单分页查询
     * @param page
     * @param ew
     * @return
     */
    IPage<InspectionOrderDto> pageInspectionOrder(Page page, @Param("ew") QueryWrapper<InspectionOrder> ew);

    /**
     * 委托单查询成品订单
     * @param page
     * @param insOrderQueryWrapper
     * @return
     */
    IPage<InsOrder> getInsOrderOnInspection(Page page, @Param("ew") QueryWrapper<InsOrder> insOrderQueryWrapper);

}

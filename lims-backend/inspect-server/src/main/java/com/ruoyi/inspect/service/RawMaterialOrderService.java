package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.*;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.SampleProductDto;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.dto.CopperInsOrderDto;
import com.ruoyi.inspect.dto.RawMaterialStandardTreeDto;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuo
 * @Date 2024/7/31
 */
public interface RawMaterialOrderService {

    Result selectStandardTreeListByPartNo(String partNo);

    /**
     * 报检查询
     * @param page
     * @param ifsInventoryQuantity
     * @return
     */
    IPage<IfsInventoryQuantity> getWarehouseSubmit(IPage<IfsInventoryQuantity> page, IfsInventoryQuantity ifsInventoryQuantity);

    /**
     * 查询检验值
     * @param page
     * @param ifsInventoryQuantityDto
     * @return
     */
    IPage<IfsInventoryQuantityDto> getIfsByStateOne(IPage<IfsInventoryQuantityDto> page, IfsInventoryQuantityDto ifsInventoryQuantityDto);

    int inspectionReport(List<Integer> ids);

    int revokeInspectionReport(Integer id);

    List<IfsInventoryQuantityDto> printLabel(List<Integer> ids);

    int inspectionReportOne(IfsInventoryQuantity ifsInventoryQuantity);

    String getIndustryChain(Integer id);

    /**
     * 原材料撤销下单
     * @param ifsInventoryId
     * @return
     */
    boolean repealRawOrder(Integer ifsInventoryId);

    /**
     * 添加免检订单
     * @param list
     * @param insOrder
     * @return
     */
    int addExemptionOrder(List<SampleProductDto> list, InsOrder insOrder);

    IPage<IfsInventoryQuantityCheckDto> selectIfsInventoryQuantity(Page<IfsInventoryQuantityCheckDto> page, IfsInventoryQuantityCheckDto ifsInventoryQuantity);

    /**
     * 查询已检验
     * @param page
     * @param ifsInventoryQuantityDto
     * @return
     */
    IPage<IfsInventoryQuantitySupplierDto>  getIfsByOver(Page<IfsInventoryQuantitySupplierDto> page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto);

    int delIfsInventory(Integer id);

    /**
     * 原材料放行免检
     * @param ifsInventoryId
     * @return
     */
    boolean rawOrderRelease(Integer ifsInventoryId, String partDetail);

    /**
     * 原材料下单通知免检或者多次检验
     * @param ifsInventoryId
     * @return
     */
    int notificationRawOrder(Integer ifsInventoryId);

    /**
     * 手动添加原材信息
     * @param ifsInventoryQuantity
     */
    void addIfsInventoryQuantity(IfsInventoryQuantity ifsInventoryQuantity);


    /**
     * 铜单丝下单免检
     * @param list
     * @param copperInsOrder
     */
    int addRawCopperOrderExemptionOrder(List<SampleProductDto> list, CopperInsOrderDto copperInsOrder);

    /**
     * 让步u放行
     * @param ifsInventoryId
     * @return
     */
    boolean concessionRelease(Integer ifsInventoryId);

    /**
     * 原材料进厂撤销下单
     * @param enterOrderId
     * @return
     */
    boolean repealEnterRawOrder(Integer enterOrderId);

    /**
     * 原材料季度撤销下单
     * @param quarterOrderId
     * @return
     */
    boolean repealQuarterRawOrder(Integer quarterOrderId);

    /**
     * 原材料报检全部信息导出
     * @param dto
     * @param response
     */
    void rawAllExport(IfsInventoryQuantitySupplierDto dto, HttpServletResponse response) throws UnsupportedEncodingException;

    /**
     * 原材料查询可以季度检验的内容
     * @param page
     * @param ifsInventoryQuantityDto
     * @return
     */
    IPage<IfsInventoryQuantitySupplierDto> getIfsByQuarter(Page page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto);

    /**
     * advancedGodown
     * @param ifsInventoryId
     * @return
     */
    boolean advancedGodown(Integer ifsInventoryId);
}

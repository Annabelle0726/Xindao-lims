package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.process.dto.InspectionOrderDto;
import com.ruoyi.process.pojo.InspectionOrder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 检验委托单 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-09
 */
public interface InspectionOrderService extends IService<InspectionOrder> {

    /**
     * 检验委托单分页查询
     * @param page
     * @param inspectionOrder
     * @return
     */
    IPage<InspectionOrderDto> pageInspectionOrder(Page page, InspectionOrder inspectionOrder);

    /**
     * 检验委托单新增
     * @param InspectionOrder
     * @return
     */
    boolean addInspectionOrder(InspectionOrderDto InspectionOrder);

    /**
     * 检验委托单修改
     * @param InspectionOrder
     * @return
     */
    boolean updateInspectionOrder(InspectionOrderDto InspectionOrder);

    /**
     * 检验委托单删除
     * @param inspectionOrderId
     * @return
     */
    boolean delInspectionOrder(Integer inspectionOrderId);

    /**
     * 检验委托单查看详情
     * @param inspectionOrderId
     * @return
     */
    InspectionOrderDto getInspectionOrderOne(Integer inspectionOrderId);

    /**
     * 根据成品订单id查询委托单信息
     * @param insOrderId
     * @return
     */
    InspectionOrderDto getInspectionOrderByInsOderId(Integer insOrderId);

    /**
     * 委托单查询成品订单
     * @return
     */
    IPage<InsOrder> getInsOrderOnInspection(Page page, InsOrder insOrder);

    /**
     * 委托单成品报告上传
     * @param file
     * @param inspectionOrderId
     * @return
     */
    boolean uploadInspectionOrderFile(MultipartFile file, Integer inspectionOrderId);

    /**
     * 导出检验委托单
     * @param inspectionOrderId
     * @param response
     */
    void exportInspectionOrder(Integer inspectionOrderId, HttpServletResponse response);
}

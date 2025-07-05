package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.process.dto.InspectionOrderDto;
import com.ruoyi.process.pojo.InspectionOrder;
import com.ruoyi.process.service.InspectionOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 检验委托单 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-09
 */
@Api(tags = "检验委托单")
@AllArgsConstructor
@RestController
@RequestMapping("/inspectionOrder")
public class InspectionOrderController {

    private InspectionOrderService inspectionOrderService;

    /**
     * 委托单检验分页查询
     * @return
     */
    @ApiOperation(value = "委托单检验分页查询")
    @GetMapping("/pageInspectionOrder")
    public Result<IPage<InspectionOrderDto>> pageInspectionOrder(InspectionOrder inspectionOrder,Page page) throws Exception {
        return Result.success(inspectionOrderService.pageInspectionOrder(page, inspectionOrder));
    }

    /**
     * 委托单检验新增
     * @return
     */

    @ApiOperation(value = "委托单检验新增")
    @PostMapping("/addInspectionOrder")
    public Result addInspectionOrder(@RequestBody InspectionOrderDto InspectionOrder){
        return Result.success(inspectionOrderService.addInspectionOrder(InspectionOrder));
    }

    /**
     * 委托单检验修改
     * @return
     */
    @ApiOperation(value = "委托单检验修改")
    @PostMapping("/updateInspectionOrder")
    public Result updateInspectionOrder(@RequestBody InspectionOrderDto InspectionOrder){
        return Result.success(inspectionOrderService.updateInspectionOrder(InspectionOrder));
    }

    /**
     * 委托单检验删除
     * @return
     */
    @ApiOperation(value = "委托单检验删除")
    @DeleteMapping("/delInspectionOrder")
    public Result delInspectionOrder(Integer inspectionOrderId){
        return Result.success(inspectionOrderService.delInspectionOrder(inspectionOrderId));
    }

    /**
     * 委托单检验查看详情
     * @return
     */
    @ApiOperation(value = "委托单检验查看详情")
    @GetMapping("/getInspectionOrderOne")
    public Result<InspectionOrderDto> getInspectionOrderOne(Integer inspectionOrderId){
        return Result.success(inspectionOrderService.getInspectionOrderOne(inspectionOrderId));
    }

    /**
     * 委托单检验查看详情
     * @return
     */
    @ApiOperation(value = "根据成品订单查询委托单详情")
    @GetMapping("/getInspectionOrderByInsOderId")
    public Result<InspectionOrderDto> getInspectionOrderByInsOderId(Integer insOrderId){
        return Result.success(inspectionOrderService.getInspectionOrderByInsOderId(insOrderId));
    }

    /**
     * 委托单查询成品订单
     * @return
     */
    @ApiOperation(value = "委托单查询成品订单")
    @GetMapping("/getInsOrderOnInspection")
    public Result<IPage<InsOrder>> getInsOrderOnInspection(Page page ,InsOrder insOrder) throws Exception {
        return Result.success(inspectionOrderService.getInsOrderOnInspection(page, insOrder));
    }

    /**
     * 委托单成品报告上传
     * @return
     */
    @ApiOperation(value = "委托单成品报告上传")
    @PostMapping("/uploadInspectionOrderFile")
    public Result uploadInspectionOrderFile(MultipartFile file, Integer inspectionOrderId) {
        return Result.success(inspectionOrderService.uploadInspectionOrderFile(file, inspectionOrderId));
    }

    /**
     * 导出检验委托单
     * @return
     */
    @ApiOperation(value = "导出检验委托单")
    @GetMapping("/exportInspectionOrder")
    public void exportInspectionOrder(Integer inspectionOrderId, HttpServletResponse response){
        inspectionOrderService.exportInspectionOrder(inspectionOrderId, response);
    }
}

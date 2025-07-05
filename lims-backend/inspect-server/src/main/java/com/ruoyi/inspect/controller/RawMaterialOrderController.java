package com.ruoyi.inspect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.*;
import com.ruoyi.basic.pojo.IfsInventoryQuantity;
import com.ruoyi.common.annotation.PersonalScope;
import com.ruoyi.inspect.dto.InsPlaceOrderDto;
import com.ruoyi.inspect.dto.SampleProductDto;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.RawMaterialOrderTemplate;
import com.ruoyi.inspect.service.InsOrderService;
import com.ruoyi.inspect.service.RawMaterialOrderService;
import com.ruoyi.inspect.service.RawMaterialOrderTemplateService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.inspect.dto.CopperInsOrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuo
 * @Date 2024/7/31
 */
@RequestMapping("/rawMaterialOrder")
@RestController
@AllArgsConstructor
@Api(tags = "原材料下单")
public class RawMaterialOrderController {

    private InsOrderService insOrderService;
    private RawMaterialOrderService rawMaterialOrderService;
    private RawMaterialOrderTemplateService rawMaterialOrderTemplateService;

    @ApiOperation(value = "更具零件号获取标准树")
    @GetMapping("/selectStandardTreeListByPartNo")
    public Result selectStandardTreeListByPartNo(String partNo) {
        return rawMaterialOrderService.selectStandardTreeListByPartNo(partNo);
    }

    @ApiOperation(value = "原材料检验查询代下单")
    @GetMapping("/getPurchaseOrder")
    public Result getPurchaseOrder(Page page, IfsInventoryQuantityCheckDto ifsInventoryQuantity){
        return Result.success(rawMaterialOrderService.selectIfsInventoryQuantity(page, ifsInventoryQuantity));
    }

    @ApiOperation(value = "原材料检验查询检验中")
    @GetMapping("/getIfsByStateOne")
    @PreAuthorize("@ss.hasPermi('business:order')")
    @PersonalScope(permsName = "business:order", objectName = IfsInventoryQuantityDto.class, paramName = "createUser")
    public Result getIfsByStateOne(Page page, IfsInventoryQuantityDto ifsInventoryQuantityDto){
        return Result.success(rawMaterialOrderService.getIfsByStateOne(page, ifsInventoryQuantityDto));
    }

    @ApiOperation(value = "原材料检验查询已检验")
    @GetMapping("/getIfsByOver")
    @PreAuthorize("@ss.hasPermi('business:order')")
    @PersonalScope(permsName = "business:order", objectName = IfsInventoryQuantitySupplierDto.class, paramName = "createUser")
    public Result getIfsByOver(Page page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto){
        return Result.success(rawMaterialOrderService.getIfsByOver(page, ifsInventoryQuantityDto));
    }

    @ApiOperation(value = "添加原材料检验单模板")
    @PostMapping("/addRawMaterOrderTemplate")
    public Result<?> addRawMaterOrderTemplate(@RequestBody RawMaterialOrderTemplate rawMaterialOrderTemplate) {
        return Result.success(rawMaterialOrderTemplateService.addRawMaterOrderTemplate(rawMaterialOrderTemplate));
    }
    @ApiOperation(value = "查询原材料检验单模板列表")
    @GetMapping("/selectRawMaterOrderTemplate")
    public Result<?> selectRawMaterOrderTemplate(String partNo) {
        return Result.success(rawMaterialOrderTemplateService.selectRawMaterOrderTemplate(partNo));
    }

    @ApiOperation(value = "通过原材料检验单模板id获取检验单模板内容")
    @GetMapping("/selectRawMaterOrderTemplateById")
    public Result<?> selectRawMaterOrderTemplateById(Integer id) {
        return Result.success(rawMaterialOrderTemplateService.selectRawMaterOrderTemplateById(id));
    }

    @ApiOperation(value = "删除原材料检验单模板")
    @DeleteMapping("/delRawMaterOrderTemplate")
    public Result<?> delRawMaterOrderTemplate(Integer id) {
        return Result.success(rawMaterialOrderTemplateService.delRawMaterOrderTemplate(id));
    }


    /**
     * 报检批量
     * @param param 原材料id
     * @return
     */
    @ApiOperation(value = "报检批量")
    @PostMapping("/inspectionReport")
    public Result<?> inspectionReport(@RequestBody Map<String, Object> param) {
        List<Integer> ids = (List<Integer>) param.get("ids");
        return Result.success(rawMaterialOrderService.inspectionReport(ids));
    }

    /**
     * 取消报检
     * @param param
     * @return
     */
    @ApiOperation(value = "撤销报检")
    @PostMapping("/revokeInspectionReport")
    public Result<?> revokeInspectionReport(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        return Result.success(rawMaterialOrderService.revokeInspectionReport(id));
    }

    /**
     * 打印标签查询
     * @param param 原材料id
     * @return
     */
    @ApiOperation(value = "打印标签查询")
    @PostMapping("/printLabel")
    public Result<?> printLabel(@RequestBody Map<String, Object> param) {
        List<Integer> ids = (List<Integer>) param.get("ids");
        return Result.success(rawMaterialOrderService.printLabel(ids));
    }

    /**
     * 报检
     * @param ifsInventoryQuantity 原材料
     * @return
     */
    @ApiOperation(value = "报检")
    @PostMapping("/inspectionReportOne")
    public Result<?> inspectionReportOne(@RequestBody IfsInventoryQuantity ifsInventoryQuantity) {

        return Result.success(rawMaterialOrderService.inspectionReportOne(ifsInventoryQuantity));
    }

    /**
     * 获取铜产业链检测数据
     * @param id 原材料id
     * @return
     */
    @ApiOperation(value = "获取铜产业链检测数据")
    @GetMapping("/getIndustryChain")
    public Result<?> getIndustryChain(Integer id) {
        return Result.success(rawMaterialOrderService.getIndustryChain(id));
    }

    /**
     * 原材料撤销
     * @param param
     * @return
     */
    @ApiOperation(value = "原材料撤销下单")
    @GetMapping("/repealRawOrder")
    public Result<?> repealRawOrder(@RequestBody Map<String, Object> param){
        Integer ifsInventoryId = (Integer) param.get("ifsInventoryId");
        return Result.success(rawMaterialOrderService.repealRawOrder(ifsInventoryId));
    }


    @ApiOperation(value = "原材料下单免检")
    @PostMapping("/addExemptionOrder")
    public Result<?> addExemptionOrder(@RequestBody InsPlaceOrderDto insPlaceOrderDto) {
        return Result.success(rawMaterialOrderService.addExemptionOrder(insPlaceOrderDto.getSampleList(), insPlaceOrderDto.getInsOrder()));
    }


    @ApiOperation(value = "仓库报检查询")
    @GetMapping("/getWarehouseSubmit")
    public Result getWarehouseSubmit(Page page, IfsInventoryQuantity ifsInventoryQuantity) throws Exception {
        return Result.success(rawMaterialOrderService.getWarehouseSubmit(page, ifsInventoryQuantity));
    }

    /**
     * 取消报检
     * @param id
     * @return
     */
    @ApiOperation(value = "删除原材料报检信息")
    @DeleteMapping("/delIfsInventory")
    public Result<?> delIfsInventory(Integer id) {
        return Result.success(rawMaterialOrderService.delIfsInventory(id));
    }

    /**
     * 原材料下单放行
     * @param param
     * @return
     */
    @ApiOperation(value = "原材料下单放行免检")
    @PostMapping("/rawOrderRelease")
    public Result<?> rawOrderRelease(@RequestBody Map<String, Object> param){
        Integer ifsInventoryId = (Integer) param.get("ifsInventoryId");
        String partDetail = (String) param.get("partDetail");
        return Result.success(rawMaterialOrderService.rawOrderRelease(ifsInventoryId, partDetail));
    }

    /**
     * 原材料下单通知免检或者多次检验
     * @param ifsInventoryId
     * @return
     */
    @ApiOperation(value = "原材料下单通知免检或者多次检验")
    @GetMapping("/notificationRawOrder")
    public Result<?> notificationRawOrder(Integer ifsInventoryId){
        return Result.success(rawMaterialOrderService.notificationRawOrder(ifsInventoryId));
    }


    @ApiOperation(value = "原材料报检查询全部")
    @PreAuthorize("@ss.hasPermi('get:Ifs:ByAll')")
    @GetMapping("/getIfsByAll")
    public Result getIfsByAll(Page page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto){
        return Result.success(rawMaterialOrderService.getIfsByOver(page, ifsInventoryQuantityDto));
    }


    @ApiOperation(value = "新增原材料报检信息")
    @PostMapping("/addIfsInventoryQuantity")
    public Result addIfsInventoryQuantity(@RequestBody IfsInventoryQuantity ifsInventoryQuantity) {
        rawMaterialOrderService.addIfsInventoryQuantity(ifsInventoryQuantity);
        return Result.success();
    }

    @ApiOperation(value = "原材料下单查看已完成信息")
    @GetMapping("/getIfsByFinish")
    public Result getIfsByFinish(Page page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto) throws Exception {
        ifsInventoryQuantityDto.setIsFinish(1);
        return Result.success(rawMaterialOrderService.getIfsByOver(page, ifsInventoryQuantityDto));
    }

    @ApiOperation(value = "微信发送文件测试")
    @GetMapping("/wxSend")
    public Result wxSend(String user, String content, String filePath) throws Exception {
        WxCpUtils.inform(user, content, new File(filePath));
        return Result.success();
    }



    @ApiOperation(value = "铜单丝下单")
    @PostMapping("/addRawCopperOrder")
    public Result<?> addRawCopperOrder(@RequestBody InsPlaceOrderDto insPlaceOrderDto) {
        return Result.success(insOrderService.addRawCopperOrder(insPlaceOrderDto.getSampleList(), insPlaceOrderDto.getCopperInsOrder()));
    }

    @ApiOperation(value = "铜单丝下单免检")
    @PostMapping("/addRawCopperOrderExemptionOrder")
    public Result<?> addRawCopperOrderExemptionOrder(@RequestBody InsPlaceOrderDto insPlaceOrderDto) {
        return Result.success(rawMaterialOrderService.addRawCopperOrderExemptionOrder(insPlaceOrderDto.getSampleList(), insPlaceOrderDto.getCopperInsOrder()));
    }

    /**
     * 让步放行
     * @param param
     * @return
     */
    @ApiOperation(value = "让步放行")
    @PostMapping("/concessionRelease")
    public Result<?> concessionRelease(@RequestBody Map<String, Object> param){
        Integer ifsInventoryId = (Integer) param.get("ifsInventoryId");
        return Result.success(rawMaterialOrderService.concessionRelease(ifsInventoryId));
    }

    /**
     * 原材料进厂撤销下单
     * @param param
     * @return
     */
    @ApiOperation(value = "原材料进厂撤销下单")
    @PostMapping("/repealEnterRawOrder")
    public Result<?> repealEnterRawOrder(@RequestBody Map<String, Object> param){
        Integer enterOrderId = (Integer) param.get("enterOrderId");
        return Result.success(rawMaterialOrderService.repealEnterRawOrder(enterOrderId));
    }

    /**
     * 原材料季度撤销下单
     * @param param
     * @return
     */
    @ApiOperation(value = "原材料季度撤销下单")
    @PostMapping("/repealQuarterRawOrder")
    public Result<?> repealQuarterRawOrder(@RequestBody Map<String, Object> param){
        Integer quarterOrderId = (Integer) param.get("quarterOrderId");
        return Result.success(rawMaterialOrderService.repealQuarterRawOrder(quarterOrderId));
    }

    @ApiOperation(value = "全部信息导出")
    @GetMapping("/rawAllExport")
    public void rawAllExport(IfsInventoryQuantitySupplierDto dto, HttpServletResponse response) throws Exception {
        rawMaterialOrderService.rawAllExport(dto,response);
    }

    @ApiOperation(value = "原材料检验查询季度检验")
    @GetMapping("/getIfsByQuarter")
    @PreAuthorize("@ss.hasPermi('business:order')")
    @PersonalScope(permsName = "business:order", objectName = IfsInventoryQuantitySupplierDto.class, paramName = "createUser")
    public Result getIfsByQuarter(Page page, IfsInventoryQuantitySupplierDto ifsInventoryQuantityDto){
        return Result.success(rawMaterialOrderService.getIfsByQuarter(page, ifsInventoryQuantityDto));
    }

    /**
     * 提前入库
     * @param param
     * @return
     */
    @ApiOperation(value = "提前入库")
    @PostMapping("/advancedGodown")
    public Result<?> advancedGodown(@RequestBody Map<String, Object> param){
        Integer ifsInventoryId = (Integer) param.get("ifsInventoryId");
        return Result.success(rawMaterialOrderService.advancedGodown(ifsInventoryId));
    }

}

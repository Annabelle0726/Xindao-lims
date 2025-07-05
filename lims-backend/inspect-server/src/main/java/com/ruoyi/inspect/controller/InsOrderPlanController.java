package com.ruoyi.inspect.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.InsOrderPlanDTO;
import com.ruoyi.inspect.dto.InsOrderPlanProductDto;
import com.ruoyi.inspect.dto.InsProductBindingDto;
import com.ruoyi.inspect.dto.SaveInsContextDto;
import com.ruoyi.inspect.pojo.InsOrder;
import com.ruoyi.inspect.pojo.InsOrderFactoryVerify;
import com.ruoyi.inspect.pojo.InsOrderFile;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.service.InsOrderFileService;
import com.ruoyi.inspect.service.InsOrderPlanService;
import com.ruoyi.inspect.service.InsOrderRatesService;
import com.ruoyi.inspect.service.InsProductService;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursTemporaryDto;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHoursTemporary;
import com.ruoyi.performance.service.AuxiliaryOutputWorkingHoursTemporaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检验任务-控制层
 */
@RestController
@RequestMapping("/insOrderPlan")
@Api(tags = "检验任务")
@AllArgsConstructor
public class InsOrderPlanController {

    private InsOrderPlanService insOrderPlanService;
    private InsOrderFileService insOrderFileService;
    private InsProductService insProductService;
    private AuxiliaryOutputWorkingHoursTemporaryService auxiliaryOutputWorkingHoursTemporaryService;


    @ApiOperation(value = "获取检验任务列表")
    @GetMapping("/selectInsOrderPlanList")
    public Result selectInsOrderPlanList(Page page, InsOrderPlanDTO insOrderPlanDTO){
        return Result.success(insOrderPlanService.selectInsOrderPlanList(page, insOrderPlanDTO));
    }

    @ApiOperation(value = "检验单详情-任务切换")
    @GetMapping("/inspectionOrderDetailsTaskSwitching")
    public Result inspectionOrderDetailsTaskSwitching(Page page, InsOrderPlanDTO insOrderPlanDTO) throws Exception {
        return Result.success(insOrderPlanService.inspectionOrderDetailsTaskSwitching(page, insOrderPlanDTO));
    }

    @ApiOperation(value = "认领任务计划")
    @PostMapping("/claimInsOrderPlan")
    public Result claimInsOrderPlan(@RequestBody InsOrderPlanDTO data) {
        return Result.success(insOrderPlanService.claimInsOrderPlan(data));
    }


    @ApiOperation(value = "查询检验操作")
    @GetMapping("/doInsOrder")
    public Result<?> doInsOrder(Integer id, String laboratory) {
        return Result.success(insOrderPlanService.doInsOrder(id, laboratory));
    }

    @ApiOperation("获取检验项目和模板内容")
    @GetMapping("/getInsProduct")
    public Result<?> getInsProduct(InsOrderPlanProductDto insOrderPlanProductDto) {
        return Result.success(insOrderPlanService.getInsProduct(insOrderPlanProductDto));
    }


    @ApiOperation(value = "任务交接")
    @PostMapping("/upPlanUser")
    public Result<?> upPlanUser(Integer userId, Integer orderId, String sonLaboratory) {
        return Result.success(insOrderPlanService.upPlanUser(userId, orderId, sonLaboratory));
    }

    @ApiOperation(value = "判断交接的试验室")
    @GetMapping("/upPlanUser2")
    public Result<?> upPlanUser2(Integer orderId) {
        return Result.success(insOrderPlanService.upPlanUser2(orderId));
    }


    @ApiOperation(value = "复核检验任务")
    @PostMapping("/verifyPlan")
    public Result<?> verifyPlan(Integer orderId, String laboratory, Integer type, String tell, Integer userId) {
        return Result.success(insOrderPlanService.rawMaterialVerifyPlan(orderId, laboratory, type, tell, userId));
    }

    @ApiOperation(value = "校验检验任务提交")
    @PostMapping("/checkSubmitPlan")
    public Result<?> checkSubmitPlan(Integer orderId, String laboratory) {
        return Result.success(insOrderPlanService.checkSubmitPlan(orderId, laboratory));
    }


    @ApiOperation(value = "检验任务提交")
    @PostMapping("/submitPlan")
    public Result<?> submitPlan(Integer orderId, String laboratory, Integer verifyUser, String entrustCode) {
        int num = insOrderPlanService.submitPlan(orderId, laboratory, verifyUser, entrustCode);
        return num == 1 ? Result.success() : Result.fail("提交失败，部分项目还未进行检验");
    }

    @ApiOperation(value = "保存检验内容")
    @PostMapping("/saveInsContext")
    public Result<?> saveInsContext(@RequestBody SaveInsContextDto saveInsContextDto) {
        insOrderPlanService.saveInsContext(saveInsContextDto);
        return Result.success();
    }

    @ApiOperation(value = "查看检验单下的附件列表")
    @GetMapping("/getFileList")
    public Result<?> getFileList(Page page, InsOrderFile insOrderFile) throws Exception {
        return Result.success(insOrderPlanService.getFileList(page, insOrderFile));
    }


    @ApiOperation(value = "上传附件")
    @PreAuthorize("@ss.hasPermi('upload:order:file')")
    @PostMapping("/uploadFile")
    public Result<?> taskUploadFile(Integer orderId, MultipartFile file) {
        return Result.success(insOrderPlanService.uploadFile(orderId, file));
    }


    @ApiOperation(value = "删除附件")
    @PreAuthorize("@ss.hasPermi('del:order:file')")
    @DeleteMapping("/delfile")
    public Result<?> delfile(Integer id) {
        return Result.success(insOrderFileService.removeById(id));
    }


    @ApiOperation(value = "下载附件")
    @GetMapping("/downFile")
    public Result<?> downFile(Integer id) {
        InsOrderFile insOrderFile = insOrderFileService.getById(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", insOrderFile.getType());
        map.put("fileUrl", insOrderFile.getFileUrl());
        return Result.success(map);
    }

    @ApiOperation(value = "填写温度与湿度")
    @PostMapping("/write")
    public Result<?> write(@RequestBody InsOrder insOrder) {
        return Result.success(insProductService.write(insOrder));
    }


    @ApiOperation(value = "电缆配置,查看配置标识")
    @GetMapping("/getCableTag")
    public Result<?> getCableTag(Integer id, String laboratory) {
        return Result.success(insOrderPlanService.getCableTag(id, laboratory));
    }

    @ApiOperation(value = "原材料,查看配置标识")
    @GetMapping("/getRawMaterialTag")
    public Result<?> getRawMaterialTag(Integer id, String laboratory) {
        return Result.success(insOrderPlanService.getRawMaterialTag(id, laboratory));
    }

    @ApiOperation(value = "查看重复标识")
    @GetMapping("/getRepetitionTag")
    public Result<?> getRepetitionTag(Integer id, String laboratory, String cableTag) {
        return Result.success(insOrderPlanService.getRepetitionTag(id, laboratory, cableTag));
    }

    @ApiOperation("新增不合格复测信息")
    @PostMapping("/addUnqualifiedRetest")
    public Result<?> addDisqualificationRetest(@RequestBody List<InsProduct> insProductsList) {
        return Result.success(insOrderPlanService.addDisqualificationRetest(insProductsList));
    }

    @ApiOperation("获取不合格复测数据")
    @GetMapping("/getInsProductUnqualifiedRetest")
    public Result<?> getInsProductUnqualifiedRetest(InsOrderPlanProductDto insOrderPlanProductDto) {
        return Result.success(insOrderPlanService.getInsProductUnqualifiedRetest(insOrderPlanProductDto));
    }

    /**
     *
     * @return
     */
    @ApiOperation(value = "保存 不合格复测检验内容")
    @PostMapping("/saveUnqualifiedContext")
    public Result<?> saveUnqualifiedContext(@RequestBody SaveInsContextDto saveInsContextDto) {
        Map<String, Object> param2 = JSON.parseObject(saveInsContextDto.getParam(), Map.class);
        insOrderPlanService.saveUnqualifiedContext(param2, saveInsContextDto.getCurrentTable(), saveInsContextDto.getSampleId(), saveInsContextDto.getOrderId(), saveInsContextDto.getSonLaboratory());
        return Result.success();
    }

    @ApiOperation(value = "查询原材料进货验证")
    @GetMapping("/getFactoryVerify")
    public Result<?> getFactoryVerify(Integer insOrderId) {
        return Result.success(insOrderPlanService.getFactoryVerify(insOrderId));
    }



    @ApiOperation(value = "保存原材料进货验证")
    @PostMapping("/addFactoryVerify")
    public Result<?> addFactoryVerify(@RequestBody InsOrderFactoryVerify factoryVerify) {
        return Result.success(insOrderPlanService.addFactoryVerify(factoryVerify));
    }


    @ApiOperation(value = "修改检验项内容")
    @PreAuthorize("@ss.hasPermi('update:product:onPlan')")
    @PostMapping("/updateProductOnPlan")
    public Result<?> updateProductOnPlan(@RequestBody InsProduct insProduct) {
        insProductService.updateById(insProduct);
        return Result.success();
    }


    @ApiOperation(value = "根据检验项id查询检验项树信息")
    @GetMapping("/getProductTreeByProductId")
    public Result<List<InsProduct>> getProductTreeByProductId(Integer productId) {
        return Result.success(insProductService.getProductTreeByProductId(productId));
    }



    @ApiOperation(value = "特殊检验项绑定")
    @PostMapping("/bindingProductTreeByProductId")
    public Result<?> bindingProductTreeByProductId(@RequestBody InsProductBindingDto insProductBindingDto) {
        return Result.success(insProductService.bindingProductTreeByProductId(insProductBindingDto));
    }


    @ApiOperation(value = "特殊检验项绑定查询")
    @GetMapping("/getBindingProductByProductId")
    public Result<List<InsProduct>> getBindingProductByProductId(Integer productId) {
        return Result.success(insProductService.list(Wrappers.<InsProduct>lambdaQuery()
                .eq(InsProduct::getBindingProductId, productId)
                .orderByAsc(InsProduct::getSort)));
    }



    @ApiOperation(value = "删除特殊检验项绑定信息")
    @DeleteMapping("/delProductTreeByProductId")
    public Result<?> delProductTreeByProductId(Integer productId) {
        return Result.success(insProductService.removeBindingProductTree(productId));
    }

    /**
     * 根据订单查询工时详情
     * @return
     */
    @ApiOperation(value = "根据订单查询工时详情")
    @PreAuthorize("@ss.hasPermi('get:working:hours:byOrder')")
    @GetMapping("/getWorkingHoursByOrderId")
    public Result<IPage<AuxiliaryOutputWorkingHoursTemporaryDto>> getWorkingHoursByOrderId(Page page, AuxiliaryOutputWorkingHoursTemporaryDto workingHoursTemporary) {
        return Result.success(auxiliaryOutputWorkingHoursTemporaryService.getWorkingHoursByOrderId(page, workingHoursTemporary));
    }

    /**
     * 修改工时详情
     * @return
     */
    @ApiOperation(value = "修改工时详情")
    @PreAuthorize("@ss.hasPermi('update:order:workingHours')")
    @PostMapping("/updateWorkingHours")
    public Result updateWorkingHours(@RequestBody AuxiliaryOutputWorkingHoursTemporary workingHoursTemporary) {
        return Result.success(auxiliaryOutputWorkingHoursTemporaryService.updateById(workingHoursTemporary));
    }
}

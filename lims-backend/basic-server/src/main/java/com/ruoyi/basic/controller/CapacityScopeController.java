package com.ruoyi.basic.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.PageTestObjectDto;
import com.ruoyi.basic.dto.ProductDTO1;
import com.ruoyi.basic.excel.StructureTestObjectData;
import com.ruoyi.basic.excel.StructureTestObjectListener;
import com.ruoyi.basic.pojo.Product;
import com.ruoyi.basic.pojo.StructureItemParameter;
import com.ruoyi.basic.pojo.StructureTestObject;
import com.ruoyi.basic.service.CapacityScopeService;
import com.ruoyi.basic.service.ProductService;
import com.ruoyi.basic.service.StructureItemParameterService;
import com.ruoyi.common.core.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 检验项目参数(StructureItemParameter)表控制层
 *
 * @author makejava
 * @since 2024-02-26 16:21:17
 */
@Api(tags = "能力范围")
@AllArgsConstructor
@RestController
@RequestMapping("/capacityScope")
public class CapacityScopeController {

    private CapacityScopeService capacityScopeService;

    private ProductService productService;

    private StructureItemParameterService structureItemParameterService;


    @ApiOperation(value = "获取项目检验参数列表")
    @GetMapping("/selectItemParameterList")
    public Result selectItemParameterList(Page page,StructureItemParameter itemParameter) {
        return Result.success(capacityScopeService.selectItemParameterList(page, itemParameter));
    }

    @ApiOperation(value = "添加项目检验参数")
    @PostMapping("/addItemParameter")
    public Result addItemParameter(@RequestBody StructureItemParameter itemParameter) {
        return Result.success(capacityScopeService.addItemParameter(itemParameter));
    }

    @ApiOperation(value = "删除项目检验参数")
    @DeleteMapping("/delItemParameter")
    public Result<?> delItemParameter(Integer id) {
        return Result.success(capacityScopeService.delItemParameter(id));
    }

    @ApiOperation(value = "修改项目检验参数")
    @PostMapping("/upItemParameter")
    public Result<?> upItemParameter(@RequestBody StructureItemParameter itemParameter) {
        return Result.success(capacityScopeService.upItemParameter(itemParameter));
    }

    @ApiOperation(value = "获取检验对象")
    @GetMapping("/selectTestObjectList")
    public Result selectTestObjectList(Page page,PageTestObjectDto pageTestObjectDto) {
        return Result.success(capacityScopeService.selectTestObjectList(page, pageTestObjectDto));
    }

    @ApiOperation(value = "添加检验对象")
    @PostMapping("/addTestObject")
    public Result addTestObject(@RequestBody StructureTestObject testObject) {
        return Result.success(capacityScopeService.addTestObject(testObject));
    }

    @ApiOperation(value = "删除检验对象")
    @DeleteMapping("/delTestObject")
    public Result<?> delTestObject(Integer id) {
        return Result.success(capacityScopeService.delTestObject(id));
    }

    @ApiOperation(value = "修改检验对象")
    @PostMapping("/upTestObject")
    public Result upTestObject(@RequestBody StructureTestObject testObject) {
        return Result.success(capacityScopeService.upTestObject(testObject));
    }

    @ApiOperation(value = "获取检验对象枚举")
    @GetMapping("/selectTestObjectByName")
    public Result selectTestObjectByName() {
        return Result.success(capacityScopeService.selectTestObjectByName());
    }

    @ApiOperation(value = "设备里面选择检验项目(树形结构)")
    @GetMapping("/getInsProduction")
    public Result getInsProduction() {
        return Result.success(capacityScopeService.getInsProduction());
    }

    @ApiOperation(value = "维护检验对象的产品")
    @GetMapping("/selectProductListByObjectId")
    public Result selectProductListByObjectId(Page page,ProductDTO1 productDTO) {
        return Result.success(productService.selectProductListByObjectId(page, productDTO));
    }

    @ApiOperation(value = "添加产品")
    @PostMapping("/addProduct")
    public Result addProduct(@RequestBody Product product) {
        return Result.success(productService.addProduct(product));
    }

    @ApiOperation(value = "修改产品")
    @PostMapping("/upProduct")
    public Result upProduct(@RequestBody Product product) {
        return Result.success(productService.upProduct(product));
    }

    @ApiOperation(value = "删除产品")
    @DeleteMapping("/delProduct")
    public Result delProduct(Integer id) {
        return Result.success(productService.delProduct(id));
    }

    @ApiOperation(value = "获取检验对象树")
    @GetMapping("/getItemTree")
    public Result getItemTree() {
        return Result.success(capacityScopeService.getItemTree());
    }


    @ApiOperation(value = "装备导入检验项目")
    @PostMapping("/importEquipData")
    @Transactional
    public Result importEquipData(@RequestParam("file") MultipartFile file) throws Exception {
        structureItemParameterService.importEquipData(file);
        return Result.success();
    }

    @ApiOperation(value = "导入检验对象")
    @PostMapping("/importExcel")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), StructureTestObjectData.class, new StructureTestObjectListener(productService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}

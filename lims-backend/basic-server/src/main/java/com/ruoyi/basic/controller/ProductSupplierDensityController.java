package com.ruoyi.basic.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.ProductSupplierDensity;
import com.ruoyi.basic.service.ProductSupplierDensityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 产品厂家密度绑定表
 *
 * @author zhuo
 * @since 2024-09-19
 */
@RestController
@RequestMapping("/productSupplierDensity")
@AllArgsConstructor
@Api(tags = "产品厂家密度绑定")
public class ProductSupplierDensityController {

    private ProductSupplierDensityService productSupplierDensityService;

    @ApiOperation(value = "根据产品id查询厂家密度绑定")
    @GetMapping("/selectSupplierDensityByProductId")
    public Result selectSupplierDensityByProductId(Page page,ProductSupplierDensity supplierDensity) {
        return Result.success(productSupplierDensityService.selectByProductId(page, supplierDensity));
    }

    @ApiOperation(value = "新增厂家密度绑定")
    @PostMapping("/addProductSupplierDensity")
    public Result addProductSupplierDensity(@RequestBody ProductSupplierDensity supplierDensity) {
        productSupplierDensityService.addProductSupplierDensity(supplierDensity);
        return Result.success();
    }

    @ApiOperation(value = "更新厂家密度绑定")
    @PostMapping("/updateProductSupplierDensity")
    public Result updateProductSupplierDensity(@RequestBody ProductSupplierDensity supplierDensity) {
        productSupplierDensityService.updateProductSupplierDensity(supplierDensity);
        return Result.success();
    }

    @ApiOperation(value = "删除厂家密度绑定")
    @DeleteMapping("/deleteProductSupplierDensity")
    public Result deleteProductSupplierDensity(Integer id) {
        productSupplierDensityService.removeById(id);
        return Result.success();
    }

}


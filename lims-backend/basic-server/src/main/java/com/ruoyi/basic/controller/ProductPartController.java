package com.ruoyi.basic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.ProductPart;
import com.ruoyi.basic.service.ProductPartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/productPart")
@Api(tags = "产品零件绑定")
public class ProductPartController {

    private ProductPartService productPartService;

    @ApiOperation(value = "根据产品id查询零件")
    @GetMapping("/selectByProductId")
    public Result selectByProductId(Page page,ProductPart productPart){
        return Result.success(productPartService.selectByProductId(page,productPart));
    }

    @ApiOperation(value = "新增产品零件")
    @PostMapping("/addProductPart")
    public Result addProductPart(@RequestBody ProductPart productPart) {
        productPartService.addProductPart(productPart);
        return Result.success();
    }

    @ApiOperation(value = "更新产品零件")
    @PostMapping("/updateProductPart")
    public Result updateProductPart(@RequestBody ProductPart productPart) {
        productPartService.updateProductPartById(productPart);
        return Result.success();
    }

    @ApiOperation(value = "删除产品零件")
    @DeleteMapping("/deleteProductPart")
    public Result deleteProductPart(Integer id) {
        productPartService.removeById(id);
        return Result.success();
    }

}

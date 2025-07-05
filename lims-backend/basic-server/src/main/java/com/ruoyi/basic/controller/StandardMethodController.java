package com.ruoyi.basic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.StandardMethod;
import com.ruoyi.basic.service.StandardMethodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "标准方法")
@RestController
@RequestMapping("/standardMethod")
@AllArgsConstructor
public class StandardMethodController {

    private StandardMethodService standardMethodService;

    @ApiOperation(value = "获取标准方法列表")
    @GetMapping("/selectStandardMethodList")
    public Result selectStandardMethodList(Page page,StandardMethod standardMethod)  {
        return Result.success(standardMethodService.selectStandardMethodList(page, standardMethod));
    }

    @ApiOperation(value = "获取标准方法枚举")
    @GetMapping("/selectStandardMethods")
    public Result selectStandardMethods(){
        return Result.success(standardMethodService.selectStandardMethods());
    }

    @ApiOperation(value = "添加标准方法")
    @PostMapping("/addStandardMethod")
    public Result addStandardMethod(@RequestBody StandardMethod standardMethod) {
        return Result.success(standardMethodService.addStandardMethod(standardMethod));
    }

    @ApiOperation(value = "删除标准方法")
    @DeleteMapping("/delStandardMethod")
    public Result<?> delStandardMethod(Integer id) {
        return Result.success(standardMethodService.delStandardMethod(id));
    }

    @ApiOperation(value = "修改标准方法")
    @PostMapping("/upStandardMethod")
    public Result<?> upStandardMethod(@RequestBody StandardMethod standardMethod) {
        return Result.success(standardMethodService.upStandardMethod(standardMethod));
    }

}

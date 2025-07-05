package com.ruoyi.web.controller.system;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.Custom;
import com.ruoyi.system.service.CustomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 客户管理
 *
 * @author zhuo
 * @since 2025-02-13
 */
@Api(tags = "客户管理")
@RestController
@RequestMapping("/system/custom")
public class CustomController {

    @Resource
    private CustomService customService;

    @ApiOperation(value = "获取客户列表")
    @GetMapping("/selectCustomPageList")
    public Result<IPage<Custom>> selectCustomPageList(Page page, Custom custom) throws Exception {
        return Result.success(customService.selectCustomPageList(page, custom));
    }
    @ApiOperation(value = "删除客户信息")
    @DeleteMapping("/delCustomById")
    public Result delCustomById(Long id) {
        return Result.success(customService.delCustomById(id));
    }

    @ApiOperation(value = "新增客户信息")
    @PostMapping("/addCustom")
    public Result addCustom(@RequestBody Custom custom) {
        return Result.success(customService.addCustom(custom));
    }


    @ApiOperation(value = "修改客户信息")
    @PostMapping("/upCustom")
    public Result upCustom(@RequestBody Custom custom) {
        return Result.success(customService.upCustom(custom));
    }

    @ApiOperation(value = "获取客户枚举")
    @GetMapping ("/selectCustomEnum")
    public Result<List<Custom>> selectCustomEnum(){
        return Result.success(customService.selectCustomEnum());
    }

}


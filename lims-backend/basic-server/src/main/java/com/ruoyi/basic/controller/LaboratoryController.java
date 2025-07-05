package com.ruoyi.basic.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.basic.service.LaboratoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 实验室管理(LaboratoryController)表控制层
 */
@Api(tags = "场所或设施")

@RestController
@RequestMapping("/laboratoryScope")
public class LaboratoryController {

    @Resource
    private LaboratoryService laboratoryService;

    @ApiOperation(value = "查询实验室管理列表")
    @GetMapping("/selectItemParameter")
    public Result selectItemParameter(Page page ,Laboratory itemParameter) {
        return Result.success(laboratoryService.selectItemParameter(page, itemParameter));
    }

    @ApiOperation(value = "添加实验室参数")
    @PostMapping("/addParameter")
    public Result addParameter(@RequestBody Laboratory itemParameter) {
        return Result.success(laboratoryService.addParameter(itemParameter));
    }

    @ApiOperation(value = "删除实验室参数")
    @DeleteMapping("/delParameter")
    public Result<?> delParameter(Integer id) {
        return Result.success(laboratoryService.delParameter(id));
    }

    @ApiOperation(value = "修改实验室参数")
    @PostMapping("/upParameter")
    public Result<?> upParameter(@RequestBody Laboratory itemParameter) {
        return Result.success(laboratoryService.upParameter(itemParameter));
    }

    @ApiOperation(value = "获取实验室名称")
    @GetMapping("/obtainItemParameterList")
    public Result obtainItemParameterList() {
        return Result.success(laboratoryService.obtainItemParameterList());
    }
}








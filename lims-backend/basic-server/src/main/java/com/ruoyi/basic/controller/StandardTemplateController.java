package com.ruoyi.basic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.basic.pojo.StandardTemplate;
import com.ruoyi.basic.service.StandardTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/StandardTemplate")
@RestController
@AllArgsConstructor
@Api(tags = "原始记录模板")
public class StandardTemplateController {

    private StandardTemplateService standardTemplateService;

    @ApiOperation(value = "获取原始记录模板列表")
    @GetMapping("/selectStandardTemplatePageList")
    public Result selectStandardTemplatePageList(Page page,StandardTemplate standardTemplate){
        return Result.success(standardTemplateService.selectStandardTemplatePageList(page, standardTemplate));
    }

    @ApiOperation(value = "添加原始记录模板")
    @PostMapping("/addStandardTemplate")
    public Result addStandardTemplate(@RequestBody StandardTemplate standardTemplate) {
        return Result.success(standardTemplateService.addStandardTemplate(standardTemplate));
    }

    @ApiOperation(value = "修改原始记录模板")
    @PostMapping("/upStandardTemplate")
    public Result<?> upStandardTemplate(@RequestBody StandardTemplate standardTemplate) {
        return Result.success(standardTemplateService.upStandardTemplate(standardTemplate));
    }

    @ApiOperation(value = "删除原始记录模板")
    @DeleteMapping("/delStandardTemplate")
    public Result<?> delStandardTemplate(Integer id) {
        return Result.success(standardTemplateService.delStandardTemplate(id));
    }

    @ApiOperation(value = "查询原始记录模板枚举")
    @GetMapping("/getStandardTemplate")
    public Result<?> getStandardTemplate() {
        return Result.success(standardTemplateService.getStandardTemplate());
    }

    @ApiOperation(value = "通过模板id获取检验项模板内容")
    @GetMapping("/getStandTempThingById")
    public Result<?> getStandTempThingById(Integer id) {
        return Result.success(standardTemplateService.getStandTempThingById(id));
    }

    @ApiOperation(value = "编辑模板编制")
    @GetMapping("/getEditTemplatePreparation")
    public Result<?> getEditTemplatePreparation(@RequestParam("id") Integer id) {
        return Result.success(standardTemplateService.getStandTempThingById(id));
    }

    @ApiOperation(value = "复制原始记录模板")
    @PostMapping("/copyStandardTemplate")
    public Result copyStandardTemplate(@RequestBody StandardTemplate newTemplate) {
        return Result.success(standardTemplateService.copyStandardTemplate(newTemplate));
    }
}

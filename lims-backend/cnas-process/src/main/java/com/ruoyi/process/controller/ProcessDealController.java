package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.process.pojo.ProcessDeal;
import com.ruoyi.process.service.ProcessDealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 检测或校准物品的处置 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-02 02:50:19
 */
@RestController
@RequestMapping("/processDeal")
@Api(tags = "检测或校准物品的处置")
public class ProcessDealController {

    @Resource
    private ProcessDealService processDealService;

    @ApiOperation(value = "新增检测或校准物品的处置")
    @PostMapping("/addProcessDeal")
    public Result addProcessDeal(@RequestBody ProcessDeal processDeal) {
        return Result.success(processDealService.saveOrUpdate(processDeal));
    }

    @ApiOperation(value = "删除检测或校准物品的处置")
    @DeleteMapping("/delProcessDeal")
    public Result delProcessDeal(Integer id) {
        return Result.success(processDealService.removeById(id));
    }


    @ApiOperation(value = "查看检测或校准物品的处置")
    @GetMapping("/getProcessDeal")
    public Result getProcessDeal(Integer id) {
        return Result.success(processDealService.list(Wrappers.<ProcessDeal>lambdaQuery().eq(ProcessDeal::getTotaldealId,id)));
    }

}

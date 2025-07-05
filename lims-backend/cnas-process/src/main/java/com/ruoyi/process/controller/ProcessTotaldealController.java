package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.pojo.ProcessTotaldeal;
import com.ruoyi.process.service.ProcessTotaldealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 检测或校准物品的处置总表(历史) 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-02 03:59:09
 */
@RestController
@RequestMapping("/processTotaldeal")
@Api(tags = "检测或校准物品的处置")
public class ProcessTotaldealController {

    @Resource
    private ProcessTotaldealService processTotaldealService;

    @ApiOperation(value = "查询检测或校准物品的处置列表")
    @GetMapping("/pageProcessTotaldeal")
    public Result pageProcessTotaldeal(ProcessTotaldeal processTotaldeal , Page page) throws Exception {
        return Result.success(processTotaldealService.pageProcessTotaldeal(page, processTotaldeal));
    }

    @ApiOperation(value = "新增检测或校准物品的处置")
    @PostMapping("/addProcessTotaldeal")
    public Result addProcessTotaldeal(@RequestBody Map<String, String> param) {
        String month = param.get("month");
        return Result.success(processTotaldealService.addProcessTotaldeal(month));
    }

    @ApiOperation(value = "提交检测或校准物品的处置")
    @PostMapping("/submitProcessTotaldeal")
    public Result submitProcessTotaldeal(@RequestBody Map<String, Integer> param) {
        Integer id = param.get("id");
        return Result.success(processTotaldealService.submitProcessTotaldeal(id));
    }

    @ApiOperation(value = "审核检测或校准物品的处置")
    @PostMapping("/checkProcessTotaldeal")
    public Result checkProcessTotaldeal(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String state = (String) param.get("state");
        return Result.success(processTotaldealService.checkProcessTotaldeal(id, state));
    }

    @ApiOperation(value = "批准检测或校准物品的处置")
    @PostMapping("/ratifyProcessTotaldeal")
    public Result ratifyProcessTotaldeal(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String state = (String) param.get("state");
        return Result.success(processTotaldealService.ratifyProcessTotaldeal(id,state));
    }

    @ApiOperation(value = "导出检测或校准物品的处置")
    @GetMapping("/exportProcessTotaldeal")
    public void exportProcessTotaldeal(Integer id, HttpServletResponse response) {
        processTotaldealService.exportProcessTotaldeal(id,response);
    }


}

package com.ruoyi.require.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.excel.FeStandardSubstanceExcel;
import com.ruoyi.require.pojo.FeStandardSubstance;
import com.ruoyi.require.service.FeStandardSubstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 标准物质清单 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-13 03:58:59
 */
@Api(tags = "标准物质清单")
@RestController
@RequestMapping("/feStandardSubstance")
public class FeStandardSubstanceController {


    @Resource
    private FeStandardSubstanceService feStandardSubstanceService;

    @ApiOperation(value = "标准物质清单查询")
    @GetMapping("/getPageStandardSubstance")
    public Result<IPage<FeStandardSubstance>> getPageStandardSubstance(Page page, FeStandardSubstance feStandardSubstance) {
        IPage<FeStandardSubstance> ipage = feStandardSubstanceService.page(page, feStandardSubstance);
        return Result.success(ipage);
    }

    @ApiOperation(value = "标准物质清单新增编辑")
    @PostMapping("/addStandardSubstance")
    public Result addStandardSubstance(@RequestBody FeStandardSubstance feStandardSubstance) {
        return Result.success(feStandardSubstanceService.saveOrUpdate(feStandardSubstance));
    }

    @ApiOperation(value = "标准物质清单删除")
    @DeleteMapping("/removeStandardSubstance")
    public Result removeStandardSubstance(Integer id) {
        return Result.success(feStandardSubstanceService.removeById(id));
    }

    @ApiOperation(value = "标准物质清单查询所有")
    @GetMapping("/getStandardSubstanceAll")
    public Result<List<FeStandardSubstance>> getStandardSubstanceAll( ) {
        return Result.success(feStandardSubstanceService.list());
    }

    @ApiOperation(value = "标准物质清单导出")
    @GetMapping("exportOfStandardSubstanceList")
    public void exportOfStandardSubstanceList(FeStandardSubstance feStandardSubstance,
                                              HttpServletResponse response) throws Exception {
        IPage<FeStandardSubstance> ipage = feStandardSubstanceService.page(new Page<>(1, -1), feStandardSubstance);
        List<FeStandardSubstanceExcel> studentList  = JSONObject.parseArray(JSON.toJSONString(ipage.getRecords()), FeStandardSubstanceExcel.class);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("requestType", "excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        // 设置单元格样式
        // 保存到第一个sheet中
        EasyExcel.write(response.getOutputStream())
                .head(FeStandardSubstanceExcel.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                .sheet("sheet")
                .doWrite(studentList);
    }
}

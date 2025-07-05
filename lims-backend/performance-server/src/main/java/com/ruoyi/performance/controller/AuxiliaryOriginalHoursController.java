package com.ruoyi.performance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;
import com.ruoyi.performance.service.AuxiliaryOriginalHoursService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Api(tags = "工时统计-原始工时")
@AllArgsConstructor
@RestController
@RequestMapping("/auxiliaryOriginalHours")
public class AuxiliaryOriginalHoursController {

    @Resource
    AuxiliaryOriginalHoursService auxiliaryOriginalHoursService;

    @ApiOperation(value = "查询原始工时")
    @GetMapping("/selectAuxiliaryOriginalHours")
    public Result selectAuxiliaryOriginalHours(Page page, AuxiliaryOriginalHoursLookDto entity) throws Exception {
        return Result.success(auxiliaryOriginalHoursService.selectAuxiliaryOriginalHours(page, entity));
    }

    @ApiOperation(value = "导出原始工时")
    @GetMapping("/exportOriginalHours")
    public void exportOriginalHours(@RequestParam("month") String month,  @RequestParam("name") String name,  @RequestParam("departLims") String departLims,HttpServletResponse response) throws IOException {
        auxiliaryOriginalHoursService.exportWorkingHours(month,name,departLims,response);
    }

    @ApiOperation(value = "查询月份全部工时")
    @GetMapping("/selectAuxiliaryAllByMonth")
    public Result selectAuxiliaryAllByMonth(AuxiliaryOriginalHoursLookDto dto){
        return Result.success(auxiliaryOriginalHoursService.selectAuxiliaryAllByMonth(dto));
    }
}

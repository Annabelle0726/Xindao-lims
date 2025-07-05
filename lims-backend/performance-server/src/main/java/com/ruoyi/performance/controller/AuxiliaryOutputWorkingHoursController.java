package com.ruoyi.performance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursDto;
import com.ruoyi.performance.service.AuxiliaryOutputWorkingHoursService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 日工时管理的产量工时 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 03:48:48
 */
@Api(tags = "日工时管理-产量工时")
@AllArgsConstructor
@RestController
@RequestMapping("/auxiliaryOutputWorkingHours")
public class AuxiliaryOutputWorkingHoursController {

    @Resource
    private AuxiliaryOutputWorkingHoursService auxiliaryOutputWorkingHoursService;

    @ApiOperation(value = "查询产量工时")
    @GetMapping("/selectAuxiliaryOutputWorkingHours")
    public Result selectAuxiliaryOutputWorkingHours(Page page,AuxiliaryOutputWorkingHoursDto entity) throws Exception {
        return Result.success(auxiliaryOutputWorkingHoursService.selectAuxiliaryOutputWorkingHours(page, entity));
    }

    @ApiOperation(value = "统计产量工时汇总和辅助工时汇总")
    @GetMapping("/collectWorkingHours")
    public Result collectWorkingHours(AuxiliaryOutputWorkingHoursDto entity)throws Exception{
        return Result.success(auxiliaryOutputWorkingHoursService.collectWorkingHours(entity));
    }


    @ApiOperation(value = "导出产量工时+辅助工时")
    @GetMapping("/exportWorkingHours")
    public void exportWorkingHours(HttpServletResponse response) throws IOException {
        auxiliaryOutputWorkingHoursService.exportWorkingHours(response);
    }

    @ApiOperation(value = "组长权限")
    @PostMapping("/leader")
    public Result leader() {
        return Result.success();
    }


    /**
     * 导出产量工时
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "导出产量工时")
    @GetMapping("/exportOutputHours")
    public void exportOutputHours(AuxiliaryOutputWorkingHoursDto entity, HttpServletResponse response) throws Exception {
        auxiliaryOutputWorkingHoursService.exportOutputHours(entity, response);
    }

}

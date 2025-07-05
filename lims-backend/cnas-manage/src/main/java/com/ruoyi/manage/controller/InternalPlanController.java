package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.dto.InternalPlanDto;
import com.ruoyi.manage.pojo.InternalPlan;
import com.ruoyi.manage.service.InternalPlanService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 内审年度计划 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-13 03:27:47
 */
@Api(tags = "内审年度计划")
@AllArgsConstructor
@RestController
@RequestMapping("/internalPlan")
public class InternalPlanController {


    private InternalPlanService internalPlanService;

    /**
     * 内审年度计划分页查询
     * @param
     * @return
     */

    @ApiOperation(value = "内审年度计划分页查询")
    @GetMapping("/pageInternalPlan")
    public Result<IPage<InternalPlanDto>> pageInternalPlan(Page page,InternalPlan internalPlan) throws Exception {
        return Result.success(internalPlanService.pageInternalPlan(page, internalPlan));
    }

    /**
     * 内审年度计划新增
     * @return
     */

    @ApiOperation(value = "内审年度计划新增")
    @PostMapping("/addInternalPlan")
    public Result addInternalPlan(@RequestBody InternalPlanDto internalPlan){
        return Result.success(internalPlanService.addInternalPlan(internalPlan));
    }

    /**
     * 内审年度计划修改
     * @return
     */

    @ApiOperation(value = "内审年度计划修改")
    @PostMapping("/updateInternalPlan")
    public Result updateInternalPlan(@RequestBody InternalPlanDto internalPlan){
        return Result.success(internalPlanService.updateInternalPlan(internalPlan));
    }

    /**
     * 内审年度计划删除
     * @return
     */

    @ApiOperation(value = "内审年度计划删除")
    @DeleteMapping("/delInternalPlan")
    public Result delInternalPlan(Integer planId){
        return Result.success(internalPlanService.delInternalPlan(planId));
    }

    /**
     * 内审年度计划查看详情
     * @return
     */

    @ApiOperation(value = "内审年度计划查看详情")
    @GetMapping("/getInternalPlanOne")
    public Result<InternalPlanDto> getInternalPlanOne(Integer planId){
        return Result.success(internalPlanService.getInternalPlanOne(planId));
    }


    /**
     * 内审年度计划审核
     * @return
     */
    @ApiOperation(value = "内审年度计划审核")
    @PostMapping("/examineInternalPlan")
    public Result examineInternalPlan(@RequestBody InternalPlanDto internalPlanDto){
        return Result.success(internalPlanService.examineInternalPlan(internalPlanDto));
    }

    /**
     * 内审年度计划批准
     * @return
     */
    @ApiOperation(value = "内审实施计划批准")
    @PostMapping("/ratifyInternalPlan")
    public Result ratifyInternalPlan(@RequestBody InternalPlanDto internalPlanDto){
        return Result.success(internalPlanService.ratifyInternalPlan(internalPlanDto));
    }

    /**
     * 导出内审年度计划
     * @return
     */

    @ApiOperation(value = "导出内审年度计划")
    @GetMapping("/exportInternalPlan")
    public void exportInternalPlan(Integer planId, HttpServletResponse response){
        internalPlanService.exportInternalImplement(planId, response);
    }
}

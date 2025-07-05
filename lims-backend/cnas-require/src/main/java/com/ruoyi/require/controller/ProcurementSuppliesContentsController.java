package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.require.pojo.ProcurementSuppliesContents;
import com.ruoyi.require.service.ProcurementSuppliesContentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "服务和供应品采购公司列表")
@RestController
@RequestMapping("/procurementSuppliesContents")
@AllArgsConstructor
public class ProcurementSuppliesContentsController {

    private ProcurementSuppliesContentsService procurementSuppliesService;

    @ApiOperation(value = "查询目录列表")
    @GetMapping("/directoryListing")
    public Result directoryListing() {
        return Result.success(procurementSuppliesService.directoryListing());
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/selectProcurementSuppliesContentById")
    public Result selectProcurementSuppliesContentById(Integer id) {
        return Result.success(procurementSuppliesService.getById(id));
    }

    @ApiOperation(value = "添加子节点")
    @PostMapping("/addProcurementSuppliesContents")
    public Result addProcurementSuppliesContents(@RequestBody ProcurementSuppliesContents procurementSupplies) {
        procurementSupplies.setUpdateTime(LocalDateTime.now());
        procurementSuppliesService.save(procurementSupplies);
        return Result.success(procurementSupplies.getId());
    }

    @ApiOperation(value = "更新子节点")
    @PostMapping("/updateProcurementSuppliesContents")
    public Result updateProcurementSuppliesContents(@RequestBody ProcurementSuppliesContents procurementSupplies) {
        return Result.success(procurementSuppliesService.updateById(procurementSupplies));
    }

    @ApiOperation(value = "删除子节点")
    @DeleteMapping("/deleteProcurementSuppliesContentById")
    public Result deleteProcurementSuppliesContentById(Integer id) {
        List<ProcurementSuppliesContents> list = procurementSuppliesService.list(new LambdaQueryWrapper<ProcurementSuppliesContents>()
                .eq(ProcurementSuppliesContents::getParentId, id));
        if(list.size() > 0) {
            throw new ErrorException("该节点含有子节点,请先删除子节点");
        }
        procurementSuppliesService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "查询所有节点")
    @GetMapping("/getNodeNames")
    public Result getNodeNames() {
        return Result.success(procurementSuppliesService.getNodeNames());
    }

    @ApiOperation(value = "获取所有人员")
    @GetMapping("/getUserList")
    public Result getUserList() {
        return Result.success(procurementSuppliesService.getUserList());
    }


}

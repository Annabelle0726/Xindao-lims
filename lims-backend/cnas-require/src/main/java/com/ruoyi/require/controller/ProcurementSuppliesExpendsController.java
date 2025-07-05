package com.ruoyi.require.controller;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.require.dto.ProcurementSuppliesExpendDto;
import com.ruoyi.require.service.ProcurementSuppliesExpendsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-15 03:47:19
 */
@Api(tags = "服务和供应品采购耗材消耗")
@RestController
@RequestMapping("/procurementSuppliesExpends")
public class ProcurementSuppliesExpendsController {
    @Autowired
    private ProcurementSuppliesExpendsService procurementSuppliesExpendsService;

    @ApiOperation(value = "查询所有记录")
    @GetMapping("/procurementSuppliesExpendlist")
    public Result procurementSuppliesExpendlist(Long procurementSuppliesListId) {
        return Result.success(procurementSuppliesExpendsService.selectAll(procurementSuppliesListId));
    }

    @ApiOperation(value = "新增消耗记录")
    @PostMapping("/addProcurementSuppliesExpends")
    public Result addProcurementSuppliesExpends(@RequestBody ProcurementSuppliesExpendDto dto) throws ServiceException {
        Integer added = procurementSuppliesExpendsService.addExpends(dto);

        if ( added == 0) {
            throw new RuntimeException("当前库存不足");
        }
        return Result.success(added);
    }

    @ApiOperation(value = "删除消耗记录")
    @DeleteMapping("/deleteProcurementSuppliesExpends")
    public Result deleteProcurementSuppliesExpends(Long expendId) throws ServiceException {
        return Result.success(procurementSuppliesExpendsService.deleteExpends(expendId));
    }
}

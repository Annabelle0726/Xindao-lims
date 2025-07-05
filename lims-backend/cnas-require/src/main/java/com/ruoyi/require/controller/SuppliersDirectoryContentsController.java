package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.require.pojo.SuppliersDirectoryContents;
import com.ruoyi.require.service.SuppliersDirectoryContentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务和供应品采购目录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-12-17 06:14:51
 */
@Api(tags = "供应商管理树")
@RestController
@RequestMapping("/suppliersDirectoryContents")
public class SuppliersDirectoryContentsController {

    @Autowired
    private SuppliersDirectoryContentsService suppliersDirectoryContentsService;

    @ApiOperation(value = "查询目录列表")
    @GetMapping("/suppliersDirectoryContentsListing")
    public Result suppliersDirectoryContentsListing() {
        return Result.success(suppliersDirectoryContentsService.directoryListing());

    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/selectSuppliersDirectoryContentsById")
    public Result selectSuppliersDirectoryContentsById(Integer id) {
        return Result.success(suppliersDirectoryContentsService.getById(id));
    }

    @ApiOperation(value = "添加子节点")
    @PostMapping("/addSuppliersDirectoryContents")
    public Result addSuppliersDirectoryContents(@RequestBody SuppliersDirectoryContents suppliersDirectoryContents ) {
        suppliersDirectoryContents.setUpdateTime(LocalDateTime.now());
        suppliersDirectoryContentsService.save(suppliersDirectoryContents);
        return Result.success(suppliersDirectoryContents.getId());
    }

    @ApiOperation(value = "更新子节点")
    @PostMapping("/updateSuppliersDirectoryContents")
    public Result updateSuppliersDirectoryContents(@RequestBody SuppliersDirectoryContents suppliersDirectoryContents) {
        return Result.success(suppliersDirectoryContentsService.updateById(suppliersDirectoryContents));
    }

    @ApiOperation(value = "删除子节点")
    @DeleteMapping("/deleteSuppliersDirectoryContentsById")
    public Result deleteSuppliersDirectoryContentsById(Integer id) {
        List<SuppliersDirectoryContents> list = suppliersDirectoryContentsService.list(new LambdaQueryWrapper<SuppliersDirectoryContents>()
                .eq(SuppliersDirectoryContents::getParentId, id));
        if(list.size() > 0) {
            throw new ErrorException("该节点含有子节点,请先删除子节点");
        }
        suppliersDirectoryContentsService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "查询所有节点")
    @GetMapping("/getSuppliersDirectoryContentsNodeNames")
    public Result getSuppliersDirectoryContentsNodeNames() {
        return Result.success(suppliersDirectoryContentsService.getSuppliersDirectoryContentsNodeNames());
    }

}

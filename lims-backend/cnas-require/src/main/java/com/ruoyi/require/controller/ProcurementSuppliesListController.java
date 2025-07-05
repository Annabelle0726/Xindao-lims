package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.require.dto.ProcurementSuppliesListDto;
import com.ruoyi.require.mapper.ProcurementSuppliesListMapper;
import com.ruoyi.require.mapper.SupplierManagementMapper;
import com.ruoyi.require.pojo.ProcurementSuppliesList;
import com.ruoyi.require.pojo.SupplierManagement;
import com.ruoyi.require.service.ProcurementSuppliesListService;
import com.ruoyi.system.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务与供应商 耗材列表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-15 04:04:32
 */
@Api(tags = "服务和供应品采购耗材")
@RestController
@RequestMapping("/procurementSuppliesList")
public class ProcurementSuppliesListController {
    @Autowired
    private ProcurementSuppliesListService procurementSuppliesListService;

    @Autowired
    private ProcurementSuppliesListMapper listMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SupplierManagementMapper supplierManagementMapper;

    @ApiOperation(value = "分页查询")
    @GetMapping("/procurementSuppliesList")
    public Result<IPage<ProcurementSuppliesListDto>> procurementSuppliesList(Page page, ProcurementSuppliesListDto list) {
        return Result.success(procurementSuppliesListService.selectList(page, list));
    }

    @ApiOperation(value = "根据id查询耗材")
    @GetMapping("/selectProcurementSuppliesListById")
    public Result selectProcurementSuppliesListById(Integer id) {
        ProcurementSuppliesList procurementSuppliesStore = listMapper.selectById(id);
        ProcurementSuppliesListDto dto = new ProcurementSuppliesListDto();
        BeanUtils.copyProperties(procurementSuppliesStore, dto);

        User user = userMapper.selectById(procurementSuppliesStore.getPersonInCharge());
        User updateUser = userMapper.selectById(procurementSuppliesStore.getUpdateUser());
        SupplierManagement supplierManagement = supplierManagementMapper.selectById(procurementSuppliesStore.getSupplier());

        dto.setPersonInChargeName(user.getName());
        dto.setUpdateUserName(updateUser.getName());
        dto.setSupplierName(supplierManagement.getSupplierName());
        return Result.success(dto);
    }

    @ApiOperation(value = "新增耗材")
    @PostMapping("/addProcurementSuppliesList")
    public Result addProcurementSuppliesList(@RequestBody ProcurementSuppliesListDto dto) {
        return Result.success(procurementSuppliesListService.addProcurementSuppliesList(dto));
    }

    @ApiOperation(value = "编辑耗材")
    @PostMapping("/updateProcurementSuppliesList")
    public Result updateProcurementSuppliesList(@RequestBody ProcurementSuppliesListDto dto) {
        return Result.success(procurementSuppliesListService.updateProcurementSuppliesList(dto));
    }

    @ApiOperation(value = "删除耗材")
    @DeleteMapping("/deleteProcurementSuppliesList")
    public Result deleteProcurementSuppliesList(Long id) {
        return Result.success(listMapper.deleteById(id));
    }

    @ApiOperation(value = "导出耗材列表")
    @GetMapping("/exportProcurementSuppliesList")
    public void exportProcurementSuppliesList(Integer parentId ,HttpServletResponse response) throws Exception {
        procurementSuppliesListService.exportProcurementSuppliesList(parentId,response);

    }
}

package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.require.pojo.SupplierManagement;
import com.ruoyi.require.service.ProcurementSuppliesContentsService;
import com.ruoyi.require.service.SupplierManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-15 02:46:45
 */
@Api(tags = "供应商管理")
@RestController
@RequestMapping("/supplierManagement")
@AllArgsConstructor
public class SupplierManagementController {

    @Autowired
    private SupplierManagementService supplierManagementService;

    @Autowired
    private ProcurementSuppliesContentsService procurementSuppliesContentsService;


    @ApiOperation("查询供方名录")
    @GetMapping("/selectSupplierManagementByParentId/{parentId}")
    public Result<List<SupplierManagement>> selectSupplierManagementByParentId(@PathVariable Integer parentId) throws Exception {
        return Result.success(supplierManagementService.selectSupplierManagementByParentId(parentId));
    }


    @ApiOperation("分页查询合格供方名录")
    @GetMapping("/selectQualifiedSupplierManagementPage")
    public Result<IPage<SupplierManagement>> selectQualifiedSupplierManagement(SupplierManagement supplierManagement, Page page) throws Exception {
        return Result.success(supplierManagementService.selectQualifiedSupplierManagement(page, supplierManagement));
    }

    @ApiOperation("根据ID查询供应商")
    @GetMapping("/selectQualifiedSupplierManagementById/{supplierManagementId}")
    public Result<List<SupplierManagement>> selectQualifiedSupplierManagementById(@PathVariable Integer supplierManagementId) throws Exception {
        return Result.success(supplierManagementService.selectQualifiedSupplierManagementById(supplierManagementId));
    }

    @ApiOperation("新增供应商")
    @PostMapping("/addSupplierManagement")
    public Result addSupplierManagement(@RequestBody SupplierManagement supplierManagement) {
        return Result.success(supplierManagementService.save(supplierManagement));
    }

    @ApiOperation("修改供应商")
    @PostMapping("/updateSupplierManagement")
    public Result updateSupplierManagement(@RequestBody SupplierManagement supplierManagement) {
        supplierManagement.setUpdateUser(SecurityUtils.getUserId().intValue());
        return Result.success(supplierManagementService.updateById(supplierManagement));
    }

    @ApiOperation("删除供应商")
    @DeleteMapping("/delSupplierManagement")
    public Result delSupplierManagement(Integer supplierManagementId) {
        return Result.success(supplierManagementService.removeById(supplierManagementId));
    }

    @ApiOperation("导出供应商")
    @PostMapping("/exportSupplierManagement/{parentId}")
    public void exportSupplierManagement(@PathVariable Integer parentId, HttpServletResponse response) throws Exception {
        supplierManagementService.exportSupplierManagement(parentId, response);
    }

    @ApiOperation("查询全部供方名录")
    @GetMapping("/selectSupplierManagementAll")
    public Result<List<SupplierManagement>> selectSupplierManagementAll() throws Exception {
        return Result.success(supplierManagementService.selectSupplierManagementAll());
    }

}

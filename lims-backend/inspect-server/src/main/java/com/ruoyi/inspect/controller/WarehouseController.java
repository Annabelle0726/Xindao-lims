package com.ruoyi.inspect.controller;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.pojo.Warehouse;
import com.ruoyi.inspect.pojo.WarehouseShelf;
import com.ruoyi.inspect.service.WarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/warehouse")
@RestController
@AllArgsConstructor
@Api(tags = "样品管理")
public class WarehouseController {

    private WarehouseService warehouseService;

    @ApiOperation("添加仓库")
    @PostMapping("/addWarehouse")
    public Result addWarehouse(@RequestBody Warehouse warehouse) {
        return Result.success(warehouseService.addWarehouse(warehouse));
    }


    @ApiOperation("查询仓库")
    @GetMapping("/selectWarehouse")
    public Result selectWarehouse() {
        return Result.success(warehouseService.selectWarehouse());
    }

    @PostMapping("/addShelf")
    @ApiOperation("添加货架")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "name", name = "名称", dataTypeClass = String.class),
            @ApiImplicitParam(value = "row", name = "行", dataTypeClass = Integer.class),
            @ApiImplicitParam(value = "col", name = "列", dataTypeClass = Integer.class),
            @ApiImplicitParam(value = "warehouseId", name = "仓库id", dataTypeClass = Integer.class)
    })
    public Result addShelf(@RequestBody WarehouseShelf warehouseShelf) {
        return Result.success(warehouseService.addShelf(warehouseShelf));
    }


    @ApiOperation("删除仓库")
    @DeleteMapping("/delWarehouse")
    public Result delWarehouse(Integer id) {
        return Result.success(warehouseService.delWarehouse(id));
    }


    @ApiOperation("修改仓库")
    @PostMapping("/upWarehouse")
    public Result upWarehouse(@RequestBody Warehouse warehouse) {
        return Result.success(warehouseService.upWarehouse(warehouse));
    }


    @ApiOperation("删除货架")
    @DeleteMapping("/delShelf")
    public Result delShelf(Integer id) {
        return Result.success(warehouseService.delShelf(id));
    }


    @ApiOperation("修改货架")
    @PostMapping("/upShelf")
    public Result upShelf(@RequestBody WarehouseShelf warehouseShelf) {
        return Result.success(warehouseService.upShelf(warehouseShelf));
    }

    @ApiOperation("查询货架下的存放信息")
    @GetMapping("/getWarehouse")
    public Result getWarehouse(Integer shelfId) {
        return Result.success(warehouseService.getWarehouse(shelfId));
    }


    @ApiOperation("样品入库")
    @PostMapping("/inWarehouse")
    public Result inWarehouse(@RequestBody Map<String, String> param) {
        String trees = param.get("trees");
        String sampleCode = param.get("sampleCode");
        return Result.success(warehouseService.inWarehouse(trees, sampleCode));
    }


    @PostMapping("/outWarehouse")
    @ApiOperation("样品出库")
    public Result outWarehouse(@RequestBody Map<String, String> param) {
        String sampleCode = param.get("sampleCode");
        return Result.success(warehouseService.outWarehouse(sampleCode));
    }

    @ApiOperation("查询样品详细记录")
    @GetMapping("/getSampleRecord")
    public Result getSampleRecord(Integer id) {
        return Result.success(warehouseService.getSampleRecord(id));
    }

    @ApiOperation("通过样品编号进行检索")
    @GetMapping("/searchSampleId")
    public Result searchSampleId(String sampleCode) {
        return Result.success(warehouseService.searchSampleId(sampleCode));
    }
}

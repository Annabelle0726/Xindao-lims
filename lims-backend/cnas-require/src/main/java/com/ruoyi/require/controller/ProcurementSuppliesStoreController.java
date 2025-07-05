package com.ruoyi.require.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.require.dto.StoreDto;
import com.ruoyi.require.pojo.ProcurementSuppliesConsumables;
import com.ruoyi.require.pojo.ProcurementSuppliesStore;
import com.ruoyi.require.service.ProcurementSuppliesConsumablesService;
import com.ruoyi.require.service.ProcurementSuppliesStoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "服务和供应品采购耗材入库")
@RestController
@RequestMapping("/procurementSuppliesStore")
@AllArgsConstructor
public class ProcurementSuppliesStoreController {

    private ProcurementSuppliesStoreService storeService;

    private ProcurementSuppliesConsumablesService consumablesService;


    @ApiOperation(value = "耗材入库分页查询")
    @GetMapping("/storeList")
    public Result storeList(Page page,StoreDto storeDto) throws Exception {
        return Result.success(storeService.selectStoreList(page, storeDto));
    }

    @ApiOperation(value = "添加耗材入库列表")
    @PostMapping("/addStore")
    public Result addStore(@RequestBody Map<String,Object> map) {
        storeService.addStore(map);
        return Result.success();
    }

    @ApiOperation(value = "删除耗材入库")
    @DeleteMapping("/deleteStore")
    public Result deleteStore(@RequestParam("id") Integer id,@RequestParam("consumablesId") Integer consumablesId) {
        storeService.deleteStore(id,consumablesId);
        return Result.success();
    }

    @ApiOperation(value = "更新耗材入库")
        @PostMapping("/updateStore")
    public Result updateStore(@RequestBody Map<String,Object> map) {
        storeService.updateStore(map);
        return Result.success();
    }

    @ApiOperation(value = "根据id查询耗材入库")
    @GetMapping("/selectStoreById")
    public Result selectStore(Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        ProcurementSuppliesStore procurementSuppliesStore = storeService.getById(id);
        List<ProcurementSuppliesConsumables> list = consumablesService.list(new LambdaQueryWrapper<ProcurementSuppliesConsumables>()
                .eq(ProcurementSuppliesConsumables::getStoreId, id));
        map.put("store", procurementSuppliesStore);
        map.put("consumables", list);
        return Result.success(map);
    }

    @ApiOperation("导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(Integer parentId, HttpServletResponse response) throws IOException {
        storeService.exportExcel(parentId, response);
    }

}

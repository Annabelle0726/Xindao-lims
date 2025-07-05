package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.dto.WarehouseCellAndSampleDto;
import com.ruoyi.inspect.dto.WarehouseDto;
import com.ruoyi.inspect.pojo.Warehouse;
import com.ruoyi.inspect.pojo.WarehouseShelf;

import java.util.List;
import java.util.Map;

/**
* @author z1292
* @description 针对表【warehouse(仓库)】的数据库操作Service
* @createDate 2024-04-06 12:13:57
*/
public interface WarehouseService extends IService<Warehouse> {

    int addWarehouse(Warehouse warehouse);

    List<WarehouseDto> selectWarehouse();

    int addShelf(WarehouseShelf warehouseShelf);

    int delWarehouse(Integer id);

    int upWarehouse(Warehouse warehouse);

    int delShelf(Integer id);

    int upShelf(WarehouseShelf warehouseShelf);

    List<WarehouseCellAndSampleDto> getWarehouse(Integer shelfId);

    int inWarehouse(String trees, String sampleCode);

    int outWarehouse(String sampleCode);

    Map<String, Object> getSampleRecord(Integer id);

    int searchSampleId(String sampleCode);
}

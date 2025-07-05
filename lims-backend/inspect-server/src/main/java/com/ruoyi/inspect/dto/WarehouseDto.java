package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.Warehouse;
import com.ruoyi.inspect.pojo.WarehouseShelf;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseDto extends Warehouse {

    List<WarehouseShelf> warehouseShelfList;

}

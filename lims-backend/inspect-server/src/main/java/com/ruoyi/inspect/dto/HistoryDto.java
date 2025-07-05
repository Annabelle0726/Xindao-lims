package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.WarehouseHistory;
import lombok.Data;

@Data
public class HistoryDto extends WarehouseHistory {

    private String createUserName;

    private String warehouseCode;

}

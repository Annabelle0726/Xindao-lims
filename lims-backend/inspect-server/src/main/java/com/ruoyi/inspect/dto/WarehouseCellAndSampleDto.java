package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsSample;
import com.ruoyi.inspect.pojo.WarehouseCell;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseCellAndSampleDto extends WarehouseCell {

    private List<InsSample> samples;

}

package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.inspect.dto.WarehouseCellAndSampleDto;
import com.ruoyi.inspect.pojo.WarehouseCell;

import java.util.List;

/**
* @author z1292
* @description 针对表【warehouse_cell(货架单元格)】的数据库操作Mapper
* @createDate 2024-04-06 12:12:12
* @Entity com.yuanchu.mom.pojo.WarehouseCell
*/
public interface WarehouseCellMapper extends BaseMapper<WarehouseCell> {

    List<WarehouseCellAndSampleDto> getWarehouse(Integer shelfId);

}





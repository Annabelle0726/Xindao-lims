package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.inspect.dto.HistoryDto;
import com.ruoyi.inspect.pojo.WarehouseHistory;

import java.util.List;

/**
* @author z1292
* @description 针对表【warehouse_history(出入库记录)】的数据库操作Mapper
* @createDate 2024-04-06 12:12:12
* @Entity com.yuanchu.mom.pojo.WarehouseHistory
*/
public interface WarehouseHistoryMapper extends BaseMapper<WarehouseHistory> {

    String getUserNameById(Integer userId);

    List<HistoryDto> getHistoryListBySampleId(Integer sampleId);

}





package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.inspect.dto.WarehouseDto;
import com.ruoyi.inspect.pojo.Warehouse;

import java.util.List;

/**
* @author z1292
* @description 针对表【warehouse(仓库)】的数据库操作Mapper
* @createDate 2024-04-06 12:12:12
* @Entity com.yuanchu.mom.pojo.Warehouse
*/
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    List<WarehouseDto> selectWarehouseList();

}





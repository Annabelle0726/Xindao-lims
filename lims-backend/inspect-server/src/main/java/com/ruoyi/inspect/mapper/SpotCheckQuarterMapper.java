package com.ruoyi.inspect.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.SpotCheckQuarterDto;
import com.ruoyi.inspect.pojo.SpotCheckQuarter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 季度抽样计划
 *
 * @author zhuo
 * @since 2024-10-09
 */
@Mapper
public interface SpotCheckQuarterMapper extends BaseMapper<SpotCheckQuarter> {

    /**
     * 季度抽样分页查询
     * @param page
     * @param ew
     * @return
     */
    IPage<SpotCheckQuarterDto> getQuarterPage(Page page, @Param("ew") QueryWrapper<SpotCheckQuarterDto> ew);

    /**
     * 成品下单界面查询季度信息
     * @return
     */
    List<Map<String, Object>> getQuarterOnOrder();

}


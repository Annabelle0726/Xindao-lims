package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.dto.TestItemDto;
import com.ruoyi.basic.pojo.StructureItemParameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检验项目参数(StructureItemParameter)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-26 16:21:17
 */
public interface StructureItemParameterMapper extends BaseMapper<StructureItemParameter> {

    IPage<StructureItemParameter> selectItemParameterList(Page page, @Param("ew") QueryWrapper<StructureItemParameter> ew);

    List<Map<String, Object>> getInsProduction();

    List<TestItemDto> getItemTree();


    /**
     * 查询设备信息
     * @param managementNumberList
     * @return
     */
    List<Integer> selectDeviceIdsByNumber(@Param("managementNumberList") List<String> managementNumberList);
}


package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.dto.PageTestObjectDto;
import com.ruoyi.basic.dto.TestItemDto;

import com.ruoyi.basic.pojo.StructureItemParameter;
import com.ruoyi.basic.pojo.StructureTestObject;

import java.util.List;
import java.util.Map;

/**
 * 检验项目参数(StructureItemParameter)表服务接口
 *
 * @author makejava
 * @since 2024-02-26 16:21:17
 */
public interface CapacityScopeService extends IService<StructureItemParameter> {

    IPage<StructureItemParameter> selectItemParameterList(Page page, StructureItemParameter itemParameter);

    int addItemParameter(StructureItemParameter itemParameter);

    int delItemParameter(Integer id);

    int upItemParameter(StructureItemParameter itemParameter);

    IPage<PageTestObjectDto> selectTestObjectList(Page page, PageTestObjectDto pageTestObjectDto);

    int addTestObject(StructureTestObject testObject);

    int delTestObject(Integer id);

    int upTestObject(StructureTestObject testObject);

    List<StructureTestObject> selectTestObjectByName();

    //设备里面选择检验项目(树形结构)
    List<Map<String, Object>> getInsProduction();

    List<TestItemDto> getItemTree();
}


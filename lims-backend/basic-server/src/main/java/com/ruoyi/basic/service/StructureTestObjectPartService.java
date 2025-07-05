package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.StructureTestObjectPart;

/**
 * 检验对象零件表
 *
 * @author zhuo
 * @since 2024-08-07
 */
public interface StructureTestObjectPartService extends IService<StructureTestObjectPart> {

    IPage<StructureTestObjectPart> selectByTestObjectId(Page page, StructureTestObjectPart structureTestObjectPart);

    void addTestObjectPart(StructureTestObjectPart structureTestObjectPart);

    void updateTestObjectPart(StructureTestObjectPart structureTestObjectPart);
}


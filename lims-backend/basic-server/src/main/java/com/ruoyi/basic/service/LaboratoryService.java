package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.Laboratory;

import java.util.List;

/**
 * 实验室管理(Laboratory)表服务接口
 */
public interface LaboratoryService extends IService<Laboratory> {

    IPage<Laboratory> selectItemParameter(Page page, Laboratory itemParameter);

    int addParameter(Laboratory itemParameter);

    int delParameter(Integer id);

    int upParameter(Laboratory itemParameter);

    List<Laboratory> obtainItemParameterList();


}


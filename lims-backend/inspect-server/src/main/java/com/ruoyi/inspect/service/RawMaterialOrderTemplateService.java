package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.pojo.RawMaterialOrderTemplate;

import java.util.List;

/**
 * 原材料下单模板
 *
 * @author zhuo
 * @since 2024-08-05
 */
public interface RawMaterialOrderTemplateService extends IService<RawMaterialOrderTemplate> {

    int addRawMaterOrderTemplate(RawMaterialOrderTemplate rawMaterialOrderTemplate);

    List<RawMaterialOrderTemplate> selectRawMaterOrderTemplate(String partNo);

    String selectRawMaterOrderTemplateById(Integer id);

    int delRawMaterOrderTemplate(Integer id);

}


package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.inspect.pojo.InsOrderTemplate;

import java.util.List;

/**
* @author Administrator
* @description 针对表【ins_order_template】的数据库操作Service
* @createDate 2024-03-18 14:14:54
*/
public interface InsOrderTemplateService extends IService<InsOrderTemplate> {

    int addInsOrderTemplate(InsOrderTemplate insOrderTemplate);

    List<InsOrderTemplate> selectInsOrderTemplate(String company);

    String selectInsOrderTemplateById(Integer id);

    int delInsOrderTemplate(Integer id);

}

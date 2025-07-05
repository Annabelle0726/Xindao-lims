package com.ruoyi.basic.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.basic.pojo.StandardTemplate;

import java.util.List;

/**
* @author Administrator
* @description 针对表【standard_template(标准模板)】的数据库操作Service
* @createDate 2024-03-11 13:47:52
*/
public interface StandardTemplateService extends IService<StandardTemplate> {

    IPage<StandardTemplate> selectStandardTemplatePageList(Page page, StandardTemplate standardTemplate);

    int addStandardTemplate(StandardTemplate standardTemplate);

    int upStandardTemplate(StandardTemplate standardTemplate);

    int delStandardTemplate(Integer id);

    List<StandardTemplate> getStandardTemplate();

    String getStandTempThingById(Integer templateId);

    String getStandTempNameById(Integer templateId);

    StandardTemplate getStandTempIdByName(String name);

    int copyStandardTemplate(StandardTemplate newTemplate);
}

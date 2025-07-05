package com.ruoyi.basic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.StandardTemplate;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【standard_template(标准模板)】的数据库操作Mapper
* @createDate 2024-03-11 13:47:52
* @Entity com.ruoyi.basic.pojo.StandardTemplate
*/
public interface StandardTemplateMapper extends BaseMapper<StandardTemplate> {

    IPage<StandardTemplate> selectStandardTemplatePageList(Page page, @Param("ew") QueryWrapper<StandardTemplate> ew);

    StandardTemplate getStandTempIdByName(@Param("name") String name);

    /**
     * 查询压缩后的数据
     * @param templateId
     * @return
     */
    String selectCompressThing(@Param("templateId") Integer templateId);

    /**
     * 绑定该模板检验项参数统计
     *
     * @param templateId
     * @return
     */
    int countBindTemplateItemParameter(@Param("templateId") Integer templateId);
}





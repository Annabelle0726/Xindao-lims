package com.ruoyi.basic.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.mapper.StandardTemplateMapper;
import com.ruoyi.basic.pojo.StandardTemplate;
import com.ruoyi.basic.service.StandardTemplateService;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.common.utils.GZipUtil;
import com.ruoyi.common.utils.QueryWrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【standard_template(标准模板)】的数据库操作Service实现
* @createDate 2024-03-11 13:47:52
*/
@Service
@AllArgsConstructor
public class StandardTemplateServiceImpl extends ServiceImpl<StandardTemplateMapper, StandardTemplate>
    implements StandardTemplateService{

    private StandardTemplateMapper standardTemplateMapper;


    private final NumberGenerator<StandardTemplate> numberGenerator;

    @Override
    public IPage<StandardTemplate> selectStandardTemplatePageList(Page page, StandardTemplate standardTemplate) {
        return standardTemplateMapper.selectStandardTemplatePageList(page, QueryWrappers.queryWrappers(standardTemplate));
    }

    @Override
    public int addStandardTemplate(StandardTemplate standardTemplate) {
        if (StringUtils.isBlank(standardTemplate.getNumber())) {
            String giveCode = numberGenerator.generateNumberWithPrefix(5,
                    "MB" + DateUtil.format(new Date(), "yyMM"),
                    StandardTemplate::getNumber);
            standardTemplate.setNumber(giveCode);
        }
        if (StringUtils.isNotBlank(standardTemplate.getThing())) {
            standardTemplate.setThing(GZipUtil.compress(standardTemplate.getThing()));
        } else {
            standardTemplate.setThing(null);
        }
        return standardTemplateMapper.insert(standardTemplate);
    }

    @Override
    public int upStandardTemplate(StandardTemplate standardTemplate) {
        if (StringUtils.isNotBlank(standardTemplate.getThing())) {
            standardTemplate.setThing(GZipUtil.compress(standardTemplate.getThing()));
        } else {
            standardTemplate.setThing(null);
        }
        return standardTemplateMapper.updateById(standardTemplate);
    }

    @Override
    public int delStandardTemplate(Integer id) {
        // 判断是否被绑定
        int count = standardTemplateMapper.countBindTemplateItemParameter(id);
        if (count > 0) {
            throw new RuntimeException("该模板已绑定检验项目参数，禁止删除");
        }
        return standardTemplateMapper.deleteById(id);
    }

    @Override
    public List<StandardTemplate> getStandardTemplate() {
        return standardTemplateMapper.selectList(Wrappers.<StandardTemplate>lambdaQuery().select(StandardTemplate::getId,StandardTemplate::getName));
    }

    @Override
    public String getStandTempThingById(Integer templateId) {
        StandardTemplate standardTemplate = standardTemplateMapper.selectOne(Wrappers.<StandardTemplate>lambdaQuery()
                .eq(StandardTemplate::getId, templateId)
                .select(StandardTemplate::getThing));
        if(standardTemplate==null){
            return null;
        }else{
            return GZipUtil.uncompress(standardTemplate.getThing());
        }
    }

    @Override
    public String getStandTempNameById(Integer templateId) {
        StandardTemplate standardTemplate = standardTemplateMapper.selectOne(Wrappers.<StandardTemplate>lambdaQuery()
                .eq(StandardTemplate::getId, templateId)
                .select(StandardTemplate::getName));
        if(standardTemplate==null){
            return null;
        }else{
            return standardTemplate.getName();
        }
    }

    @Override
    public StandardTemplate getStandTempIdByName(String name) {
        return standardTemplateMapper.getStandTempIdByName(name);
    }

    /**
     * 复制原始记录模板
     * @param newTemplate
     * @return
     */
    @Override
    public int copyStandardTemplate(StandardTemplate newTemplate) {
        // 查询旧模板
        StandardTemplate standardTemplate = baseMapper.selectById(newTemplate.getId());
        newTemplate.setThing(standardTemplate.getThing());

        if (StringUtils.isBlank(newTemplate.getNumber())) {
            String giveCode = numberGenerator.generateNumberWithPrefix(5,
                    "MB" + DateUtil.format(new Date(), "yyMM"),
                    StandardTemplate::getNumber);
            newTemplate.setNumber(giveCode);
        }
        newTemplate.setId(null);

        return standardTemplateMapper.insert(newTemplate);
    }
}

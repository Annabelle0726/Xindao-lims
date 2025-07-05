package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.inspect.mapper.InsOrderTemplateMapper;
import com.ruoyi.inspect.pojo.InsOrderTemplate;
import com.ruoyi.inspect.service.InsOrderTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【ins_order_template】的数据库操作Service实现
 * @createDate 2024-03-18 14:14:54
 */
@Service
@AllArgsConstructor
public class InsOrderTemplateServiceImpl extends ServiceImpl<InsOrderTemplateMapper, InsOrderTemplate>
        implements InsOrderTemplateService {

    private InsOrderTemplateMapper insOrderTemplateMapper;

    @Override
    public int addInsOrderTemplate(InsOrderTemplate insOrderTemplate) {
        return insOrderTemplateMapper.insert(insOrderTemplate);
    }

    @Override
    public List<InsOrderTemplate> selectInsOrderTemplate(String company) {
        if (ObjectUtils.isEmpty(company) || company.equals("")) {
            return insOrderTemplateMapper.selectList(Wrappers.<InsOrderTemplate>lambdaQuery()
                    .select(InsOrderTemplate.class, info -> !info.getColumn().equals("thing")));
        } else {
            //根据委托单位进行筛选
            return insOrderTemplateMapper.selectList(Wrappers.<InsOrderTemplate>lambdaQuery()
                    .like(InsOrderTemplate::getThing,"\"company\":\""+ company+"\"")
                    .select(InsOrderTemplate.class, info -> !info.getColumn().equals("thing")));
        }
    }

    @Override
    public String selectInsOrderTemplateById(Integer id) {
        return insOrderTemplateMapper.selectById(id).getThing();
    }

    @Override
    public int delInsOrderTemplate(Integer id) {
        return insOrderTemplateMapper.deleteById(id);
    }
}





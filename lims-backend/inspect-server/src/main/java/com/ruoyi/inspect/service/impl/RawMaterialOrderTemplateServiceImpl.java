package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.inspect.mapper.RawMaterialOrderTemplateMapper;
import com.ruoyi.inspect.pojo.RawMaterialOrderTemplate;
import com.ruoyi.inspect.service.RawMaterialOrderTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 原材料下单模板
 *
 * @author zhuo
 * @since 2024-08-05
 */
@Service
@AllArgsConstructor
public class RawMaterialOrderTemplateServiceImpl extends ServiceImpl<RawMaterialOrderTemplateMapper, RawMaterialOrderTemplate> implements RawMaterialOrderTemplateService {


    /**
     * 添加原材料检验单模板
     * @param rawMaterialOrderTemplate
     * @return
     */
    @Override
    public int addRawMaterOrderTemplate(RawMaterialOrderTemplate rawMaterialOrderTemplate) {
        if (StringUtils.isBlank(rawMaterialOrderTemplate.getPartNo())) {
            throw new RuntimeException("缺少零件号");
        }
        return baseMapper.insert(rawMaterialOrderTemplate);
    }

    /**
     * 查询原材料检验单模板列表
     * @param partNo
     * @return
     */
    @Override
    public List<RawMaterialOrderTemplate> selectRawMaterOrderTemplate(String partNo) {
        return baseMapper.selectList(Wrappers.<RawMaterialOrderTemplate>lambdaQuery()
                .eq(RawMaterialOrderTemplate::getPartNo, partNo));
    }

    /**
     * 通过原材料检验单模板id获取检验单模板内容
     * @param id
     * @return
     */
    @Override
    public String selectRawMaterOrderTemplateById(Integer id) {
        return baseMapper.selectById(id).getThing();
    }

    /**
     * 删除原材料检验单模板
     * @param id
     * @return
     */
    @Override
    public int delRawMaterOrderTemplate(Integer id) {
        return baseMapper.deleteById(id);
    }

}


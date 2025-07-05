package com.ruoyi.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.mapper.StructureTestObjectMapper;
import com.ruoyi.basic.pojo.StructureTestObject;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.basic.mapper.LaboratoryMapper;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.basic.service.LaboratoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实验室管理(Laboratory)表服务实现类
 */
@Service
@AllArgsConstructor
public class LaboratoryServiceImpl extends ServiceImpl<LaboratoryMapper, Laboratory> implements LaboratoryService {


    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private StructureTestObjectMapper structureTestObjectMapper;

    @Override
    public IPage<Laboratory> selectItemParameter(Page page, Laboratory itemParameter) {
        return laboratoryMapper.selectItemParameter(page, QueryWrappers.queryWrappers(itemParameter));
    }

    @Override
    public int addParameter(Laboratory itemParameter) {
        return laboratoryMapper.insert(itemParameter);
    }

    @Override
    public int delParameter(Integer id) {
        // 判断是否绑定对象
        QueryWrapper<StructureTestObject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("laboratory_id",id);
        List<StructureTestObject> structureTestObjectList = structureTestObjectMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(structureTestObjectList)){
            throw new RuntimeException("已绑定对象，禁止删除");
        }
        return laboratoryMapper.deleteById(id);
    }

    @Override
    public int upParameter(Laboratory itemParameter) {
        return laboratoryMapper.updateById(itemParameter);
    }

    @Override
    public List<Laboratory> obtainItemParameterList() {
        return laboratoryMapper.selectList(Wrappers.<Laboratory>lambdaQuery().select(Laboratory::getLaboratoryName, Laboratory::getId));
    }


}


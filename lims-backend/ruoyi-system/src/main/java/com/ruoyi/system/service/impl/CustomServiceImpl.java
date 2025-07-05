package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.Custom;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.system.mapper.CustomMapper;
import com.ruoyi.system.service.CustomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【power(用户id)】的数据库操作Service实现
* @createDate 2023-12-27 02:37:38
*/
@Service
@AllArgsConstructor
public class CustomServiceImpl extends ServiceImpl<CustomMapper, Custom>
    implements CustomService {

    private CustomMapper customMapper;

    @Override
    public IPage<Custom> selectCustomPageList(IPage<Custom> page, Custom custom) {
        return customMapper.selectCustomPageList(page, QueryWrappers.queryWrappers(custom));
    }

    @Override
    public int delCustomById(Long id) {
        return customMapper.deleteById(id);
    }

    @Override
    public int addCustom(Custom custom) {
        return customMapper.insert(custom);
    }

    @Override
    public int upCustom(Custom custom) {
        return customMapper.updateById(custom);
    }

    @Override
    public List<Custom> selectCustomEnum() {
        return customMapper.selectList(Wrappers.<Custom>lambdaQuery().select(Custom::getId,Custom::getCompany));
    }

    @Override
    public Custom getCustomId(String company) {
        return customMapper.selectOne(Wrappers.<Custom>lambdaQuery().eq(Custom::getCompany, company).last("limit 1"));
    }
}





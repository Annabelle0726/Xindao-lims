package com.ruoyi.basic.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.basic.mapper.SealMapper;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.basic.pojo.Seal;
import com.ruoyi.basic.service.SealService;
import com.ruoyi.common.utils.QueryWrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SealServiceImpl extends ServiceImpl<SealMapper, Seal> implements SealService {
    private SealMapper sealMapper;


    @Override
    public int addSeal(Seal seal) {
        return sealMapper.insert(seal);
    }




    @Override
    public IPage<Seal> selectSeal(Page page, Seal seal) {
        return sealMapper.selectSeal(page, QueryWrappers.queryWrappers(seal));
    }
    @Override
    public List<Laboratory> Laboratory(Integer labId) {
        return sealMapper.selectLaboratory(labId);
    }
}

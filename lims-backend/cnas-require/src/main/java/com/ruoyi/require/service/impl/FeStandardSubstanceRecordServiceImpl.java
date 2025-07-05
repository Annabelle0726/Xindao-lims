package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.require.mapper.FeStandardSubstanceMapper;
import com.ruoyi.require.mapper.FeStandardSubstanceRecordMapper;
import com.ruoyi.require.pojo.FeStandardSubstance;
import com.ruoyi.require.pojo.FeStandardSubstanceRecord;
import com.ruoyi.require.service.FeStandardSubstanceRecordService;
import com.ruoyi.require.vo.SubstanceRecordVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 标准物质清单借用归还记录表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-14 01:49:11
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class FeStandardSubstanceRecordServiceImpl extends ServiceImpl<FeStandardSubstanceRecordMapper, FeStandardSubstanceRecord> implements FeStandardSubstanceRecordService {

    @Resource
    private FeStandardSubstanceMapper feStandardSubstanceMapper;

    @Override
    public void borrowSubstance(FeStandardSubstanceRecord feStandardSubstanceRecord) {
        FeStandardSubstance standardSubstance = feStandardSubstanceMapper.selectOne(Wrappers.<FeStandardSubstance>lambdaQuery()
                .eq(FeStandardSubstance::getId, feStandardSubstanceRecord.getSubstanceId())
                .eq(FeStandardSubstance::getState, 0)
                .last("limit 1"));
        if (ObjectUtils.isEmpty(standardSubstance)){
            throw new ErrorException("该物质已被借出！");
        }
        this.baseMapper.insert(feStandardSubstanceRecord);
        standardSubstance.setState(1);
        feStandardSubstanceMapper.updateById(standardSubstance);
    }

    @Override
    public void returnSubstance(FeStandardSubstanceRecord feStandardSubstanceRecord) {
        FeStandardSubstance standardSubstance = feStandardSubstanceMapper.selectOne(Wrappers.<FeStandardSubstance>lambdaQuery()
                .eq(FeStandardSubstance::getId, feStandardSubstanceRecord.getSubstanceId())
                .eq(FeStandardSubstance::getState, 1));
        if (ObjectUtils.isEmpty(standardSubstance)){
            throw new ErrorException("该物质未被领用，无需归还！");
        }
        standardSubstance.setState(0);
        feStandardSubstanceMapper.updateById(standardSubstance);
        baseMapper.update(feStandardSubstanceRecord, Wrappers.<FeStandardSubstanceRecord>lambdaUpdate()
                .eq(FeStandardSubstanceRecord::getSubstanceId, feStandardSubstanceRecord.getSubstanceId())
                .eq(FeStandardSubstanceRecord::getStatus, 0));
    }

    @Override
    public List<FeStandardSubstanceRecord> getSubstanceRecord(Integer id) {
        QueryWrapper<FeStandardSubstanceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("substance_id",id);
        List<FeStandardSubstanceRecord> list = this.baseMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public IPage<SubstanceRecordVo> getPage(Page page, SubstanceRecordVo vo) {
        return this.baseMapper.getPage(page,vo);
    }
}

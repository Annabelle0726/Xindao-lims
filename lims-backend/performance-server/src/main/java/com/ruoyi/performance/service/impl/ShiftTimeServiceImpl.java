package com.ruoyi.performance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.performance.mapper.ShiftTimeMapper;
import com.ruoyi.performance.pojo.ShiftTime;
import com.ruoyi.performance.service.ShiftTimeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 班次对应的时间 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-24 11:22:17
 */
@Service
public class ShiftTimeServiceImpl extends ServiceImpl<ShiftTimeMapper, ShiftTime> implements ShiftTimeService {

    @Resource
    ShiftTimeMapper shiftTimeMapper;

    @Override
    public void shiftTimeAdd(ShiftTime shiftTime) {
        List<ShiftTime> shiftTimes = shiftTimeMapper.selectList(Wrappers.<ShiftTime>lambdaQuery().eq(ShiftTime::getShift, shiftTime.getShift()));
        if (shiftTimes.size() > 0) {
           throw new BaseException("已存在该班次的时间配置,请删掉后再新增!");
        }
        shiftTimeMapper.insert(shiftTime);
    }

    @Override
    public List<ShiftTime> shiftTimeList() {
        return shiftTimeMapper.selectList(null);
    }
}

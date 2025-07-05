package com.ruoyi.performance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.performance.mapper.AuxiliaryWorkingHoursMapper;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHours;
import com.ruoyi.performance.service.AuxiliaryWorkingHoursService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 辅助工时 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-09 06:58:31
 */
@Service
public class AuxiliaryWorkingHoursServiceImpl extends ServiceImpl<AuxiliaryWorkingHoursMapper, AuxiliaryWorkingHours> implements AuxiliaryWorkingHoursService {

    @Resource
    private  AuxiliaryWorkingHoursMapper auxiliaryWorkingHoursMapper;
    @Override
    public IPage<AuxiliaryWorkingHours> selectAuxiliaryWorkingHours(Page page, AuxiliaryWorkingHours auxiliaryWorkingHours) {
        return auxiliaryWorkingHoursMapper.selectAuxiliaryWorkingHours(page, QueryWrappers.queryWrappers(auxiliaryWorkingHours));
    }

    @Override
    public int deleteAuxiliaryWorkingHours(Integer id) {
        // 判断日工时管理中是否已绑定待删除辅助工时配置
        int count = auxiliaryWorkingHoursMapper.hourDayBindAuxiliaryCount(id);
        if(count > 0){
            throw new RuntimeException("已在日工时管理中绑定，禁止删除！");
        }
        return auxiliaryWorkingHoursMapper.deleteById(id);
    }

    @Override
    public int upAuxiliaryWorkingHours(AuxiliaryWorkingHours auxiliaryWorkingHours) {
        return auxiliaryWorkingHoursMapper.updateById(auxiliaryWorkingHours);
    }

    @Override
    public int insertAuxiliaryWorkingHours(AuxiliaryWorkingHours auxiliaryWorkingHours) {
        //编号不能重复
        List<String> strings = auxiliaryWorkingHoursMapper.selectList(null).stream().map(AuxiliaryWorkingHours::getNumber).distinct().collect(Collectors.toList());
        if (strings.contains(auxiliaryWorkingHours.getNumber())){
            throw new RuntimeException("编号不能重复!");
        }
        return auxiliaryWorkingHoursMapper.insert(auxiliaryWorkingHours);
    }
}

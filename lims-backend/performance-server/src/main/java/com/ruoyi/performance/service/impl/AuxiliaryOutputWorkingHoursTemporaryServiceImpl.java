package com.ruoyi.performance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursTemporaryDto;
import com.ruoyi.performance.mapper.AuxiliaryOutputWorkingHoursTemporaryMapper;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHoursTemporary;
import com.ruoyi.performance.service.AuxiliaryOutputWorkingHoursTemporaryService;
import org.springframework.stereotype.Service;

/**
 * 工时暂存表
 *
 * @author zhuo
 * @since 2025-02-27
 */
@Service
public class AuxiliaryOutputWorkingHoursTemporaryServiceImpl extends ServiceImpl<AuxiliaryOutputWorkingHoursTemporaryMapper, AuxiliaryOutputWorkingHoursTemporary> implements AuxiliaryOutputWorkingHoursTemporaryService {

    /**
     * 根据订单查询工时详情
     * @param page
     * @param workingHoursTemporary
     * @return
     */
    @Override
    public IPage<AuxiliaryOutputWorkingHoursTemporaryDto> getWorkingHoursByOrderId(Page page, AuxiliaryOutputWorkingHoursTemporaryDto workingHoursTemporary) {
        return baseMapper.getWorkingHoursByOrderId(page, QueryWrappers.queryWrappers(workingHoursTemporary));
    }
}


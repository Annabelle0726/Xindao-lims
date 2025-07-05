package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.dto.InsProductDeviationWarningDto;
import com.ruoyi.inspect.pojo.InsProductDeviationWarning;
import com.ruoyi.inspect.mapper.InsProductDeviationWarningMapper;
import com.ruoyi.inspect.service.InsProductDeviationWarningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 检验项偏差预警主表 服务实现类
 * </p>
 *
 * @author
 * @since 2025-03-28 02:18:02
 */
@Service
public class InsProductDeviationWarningServiceImpl extends ServiceImpl<InsProductDeviationWarningMapper, InsProductDeviationWarning> implements InsProductDeviationWarningService {

    /**
     * 查看预警列表
     * @param page
     * @param deviationWarningDto
     * @return
     */
    @Override
    public IPage<InsProductDeviationWarningDto> selectDeviationWarningPage(Page page, InsProductDeviationWarningDto deviationWarningDto) {
        return baseMapper.selectDeviationWarningPage(page, QueryWrappers.queryWrappers(deviationWarningDto));
    }
}

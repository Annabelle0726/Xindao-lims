package com.ruoyi.inspect.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.inspect.dto.InsProductDeviationWarningDto;
import com.ruoyi.inspect.pojo.InsProductDeviationWarning;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检验项偏差预警主表 服务类
 * </p>
 *
 * @author
 * @since 2025-03-28 02:18:02
 */
public interface InsProductDeviationWarningService extends IService<InsProductDeviationWarning> {

    /**
     * 查询预警列表
     * @param page
     * @param deviationWarningDto
     * @return
     */
    IPage<InsProductDeviationWarningDto> selectDeviationWarningPage(Page page, InsProductDeviationWarningDto deviationWarningDto);
}

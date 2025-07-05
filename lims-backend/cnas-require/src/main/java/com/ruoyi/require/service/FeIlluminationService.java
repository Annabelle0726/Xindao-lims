package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.FeIlluminationDto;
import com.ruoyi.require.pojo.FeIllumination;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-照度记录表 服务类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:15:57
 */
public interface FeIlluminationService extends IService<FeIllumination> {

    IPage<FeIlluminationDto> getFeLightningProtection(Page page);

    /**
     * 导出照度记录
     * @param intensityIlluminationId
     * @param response
     */
    void exportFeIllumination(Integer intensityIlluminationId, HttpServletResponse response);
}

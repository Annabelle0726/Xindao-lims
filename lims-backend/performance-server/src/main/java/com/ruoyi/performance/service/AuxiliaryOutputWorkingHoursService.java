package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.dto.AuxiliaryOutputWorkingHoursDto;
import com.ruoyi.performance.pojo.AuxiliaryOutputWorkingHours;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 日工时管理的产量工时 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 03:48:48
 */
public interface AuxiliaryOutputWorkingHoursService extends IService<AuxiliaryOutputWorkingHours> {

    IPage<AuxiliaryOutputWorkingHoursDto> selectAuxiliaryOutputWorkingHours(Page page, AuxiliaryOutputWorkingHoursDto auxiliaryOutputWorkingHoursDto);

    /**
     * 统计产量工时汇总和辅助工时汇总
     * @return
     */
    Map<String,Object> collectWorkingHours(AuxiliaryOutputWorkingHoursDto auxiliaryOutputWorkingHoursDto);

    /**
     * 导出
     * @param response
     * @throws IOException
     */
    void exportWorkingHours(HttpServletResponse response)throws IOException;

    /**
     * 导出产量工时
     * @param entity
     * @param response
     */
    void exportOutputHours(AuxiliaryOutputWorkingHoursDto entity, HttpServletResponse response);
}

package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.performance.dto.AuxiliaryAllDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursDto;
import com.ruoyi.performance.dto.AuxiliaryOriginalHoursLookDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AuxiliaryOriginalHoursService {

    IPage<AuxiliaryOriginalHoursDto> selectAuxiliaryOriginalHours(Page page, AuxiliaryOriginalHoursLookDto auxiliaryOriginalHoursLookDto);

    /**
     * 导出原始工时
     *
     * @param response
     */
    void exportWorkingHours(String month, String name, String departLims,HttpServletResponse response) throws IOException;

    /**
     * 查询月份全部工时
     * @return
     */
    List<AuxiliaryAllDto> selectAuxiliaryAllByMonth(AuxiliaryOriginalHoursLookDto dto);
}

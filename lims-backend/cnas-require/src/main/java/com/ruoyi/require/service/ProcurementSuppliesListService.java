package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.ProcurementSuppliesListDto;
import com.ruoyi.require.pojo.ProcurementSuppliesList;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务与供应商 耗材列表 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-15 04:04:32
 */
public interface ProcurementSuppliesListService extends IService<ProcurementSuppliesList> {
    IPage<ProcurementSuppliesListDto> selectList(Page page, ProcurementSuppliesListDto list);

    public Integer addProcurementSuppliesList(ProcurementSuppliesListDto dto);

    public Integer updateProcurementSuppliesList(ProcurementSuppliesListDto dto);

    void exportProcurementSuppliesList(Integer contentsId,HttpServletResponse response);

}

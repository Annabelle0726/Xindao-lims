package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.ProcurementSuppliesListDto;
import com.ruoyi.require.pojo.ProcurementSuppliesList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务与供应商 耗材列表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-15 04:04:32
 */
@Mapper
public interface ProcurementSuppliesListMapper extends BaseMapper<ProcurementSuppliesList> {
    IPage<ProcurementSuppliesList> selectProcurementSuppliesList(Page page, @Param("ew") ProcurementSuppliesListDto ew);

    ProcurementSuppliesList selectProcurementSuppliesListForUpdate(@Param("id") Long id);

    List<ProcurementSuppliesList> selectProcurementSuppliesListAll(@Param("ew") ProcurementSuppliesListDto ew);

    List<ProcurementSuppliesList> selectProcurementSuppliesListByContentsId(@Param("id") Integer contentsId);
}

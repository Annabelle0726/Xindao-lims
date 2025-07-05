package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.StoreDto;
import com.ruoyi.require.excel.StoreExcel;
import com.ruoyi.require.pojo.ProcurementSuppliesStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcurementSuppliesStoreMapper extends BaseMapper<ProcurementSuppliesStore> {

    IPage<StoreDto> selectStoreList(Page page, @Param("ew") QueryWrapper<StoreDto> ew);

    /**
     *       和树关联的条件，，，目前没有关联关系    WHERE s.contents_id = #{contentsId}
     * @param contentsId
     * @return
     */
    List<StoreExcel> exportExcel(Integer contentsId);

}

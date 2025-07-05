package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.StoreDto;
import com.ruoyi.require.pojo.ProcurementSuppliesStore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface ProcurementSuppliesStoreService extends IService<ProcurementSuppliesStore> {
    void addStore(Map<String,Object> map);
    void updateStore(Map<String,Object> map);

    void deleteStore(Integer id, Integer consumablesId);
    IPage<StoreDto> selectStoreList(Page page, StoreDto storeDto);

    void exportExcel(Integer contentsId, HttpServletResponse response) throws IOException;
}

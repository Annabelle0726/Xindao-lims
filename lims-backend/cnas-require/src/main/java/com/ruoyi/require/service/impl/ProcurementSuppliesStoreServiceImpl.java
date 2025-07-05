package com.ruoyi.require.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.require.dto.StoreDto;
import com.ruoyi.require.excel.StoreExcel;
import com.ruoyi.require.mapper.ProcurementSuppliesConsumablesMapper;
import com.ruoyi.require.mapper.ProcurementSuppliesStoreMapper;
import com.ruoyi.require.pojo.ProcurementSuppliesConsumables;
import com.ruoyi.require.pojo.ProcurementSuppliesStore;
import com.ruoyi.require.service.ProcurementSuppliesStoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProcurementSuppliesStoreServiceImpl extends ServiceImpl<ProcurementSuppliesStoreMapper, ProcurementSuppliesStore>
        implements ProcurementSuppliesStoreService {

    private ProcurementSuppliesConsumablesMapper consumablesMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStore(Map<String,Object> map) {
        ProcurementSuppliesStore procurementSuppliesStore = JSONObject.parseObject(JSONObject.toJSONString(map.get("store")), ProcurementSuppliesStore.class);
        procurementSuppliesStore.setRegistrant(SecurityUtils.getUserId().intValue());
        procurementSuppliesStore.setRegistrantTime(LocalDate.now());
        baseMapper.insert(procurementSuppliesStore);
        List<ProcurementSuppliesConsumables> list = JSON.parseArray(JSONObject.toJSONString(map.get("consumables")), ProcurementSuppliesConsumables.class);
        // 生成货号
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if(!Objects.isNull(list) && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setStoreId(procurementSuppliesStore.getId());
                String itemNumber ="HCRK" + LocalDate.now().format(dateTimeFormatter) +  String.format("%03d", i);
                list.get(i).setItemNumber(itemNumber);
                consumablesMapper.insert(list.get(i));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStore(Map<String,Object> map) {
        ProcurementSuppliesStore procurementSuppliesStore = JSONObject.parseObject(JSONObject.toJSONString(map.get("store")), ProcurementSuppliesStore.class);
        baseMapper.updateById(procurementSuppliesStore);
        List<ProcurementSuppliesConsumables> list = JSON.parseArray(JSONObject.toJSONString(map.get("consumables")), ProcurementSuppliesConsumables.class);
        consumablesMapper.delete(new LambdaQueryWrapper<ProcurementSuppliesConsumables>()
                .eq(ProcurementSuppliesConsumables::getStoreId, procurementSuppliesStore.getId()));
        if(!Objects.isNull(list) && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setId(null);
                list.get(i).setStoreId(procurementSuppliesStore.getId());
                consumablesMapper.insert(list.get(i));
            }
        }
    }

    @Override
    public void deleteStore(Integer id, Integer consumablesId) {
        consumablesMapper.deleteById(consumablesId);
        List<ProcurementSuppliesConsumables> consumables = consumablesMapper.selectList(new LambdaQueryWrapper<ProcurementSuppliesConsumables>()
                .eq(ProcurementSuppliesConsumables::getStoreId, id));
        // 判断是否还有耗材 没有就将主表Store删除
        if(CollectionUtils.isEmpty(consumables)) {
            baseMapper.deleteById(id);
        }
    }

    @Override
    public  IPage<StoreDto> selectStoreList(Page page, StoreDto storeDto) {
        IPage<StoreDto> iPage = baseMapper.selectStoreList(page, QueryWrappers.queryWrappers(storeDto));
        return iPage;
    }

    /**
     * 导出excel
     * @param contentsId
     * @param response
     */
    @Override
    public void exportExcel(Integer contentsId, HttpServletResponse response) throws IOException {
        List<StoreExcel> storeExcels = baseMapper.exportExcel(contentsId);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("耗材入库", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0, "耗材入库").head(StoreExcel.class).build();
        excelWriter.write(storeExcels, writeSheet);
        excelWriter.finish();
    }
}

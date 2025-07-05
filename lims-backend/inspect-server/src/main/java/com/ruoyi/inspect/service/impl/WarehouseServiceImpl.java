package com.ruoyi.inspect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.dto.HistoryDto;
import com.ruoyi.inspect.dto.WarehouseCellAndSampleDto;
import com.ruoyi.inspect.dto.WarehouseDto;
import com.ruoyi.inspect.mapper.*;
import com.ruoyi.inspect.pojo.*;
import com.ruoyi.inspect.service.WarehouseCellService;
import com.ruoyi.inspect.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【ins_sample_user(样品负责人记录)】的数据库操作Service实现
* @createDate 2024-03-14 17:12:02
*/
@Service
@AllArgsConstructor
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse>
    implements WarehouseService {

    private WarehouseMapper warehouseMapper;

    private WarehouseShelfMapper warehouseShelfMapper;

    private WarehouseCellMapper warehouseCellMapper;

    private WarehouseHistoryMapper warehouseHistoryMapper;

    private WarehouseCellService warehouseCellService;

    private InsSampleMapper insSampleMapper;

    private InsProductMapper insProductMapper;

    @Override
    public int addWarehouse(Warehouse warehouse) {
        Warehouse newWarehouse = new Warehouse();
        warehouse.setName(warehouse.getName());
        return warehouseMapper.insert(warehouse);
    }

    @Override
    public List<WarehouseDto> selectWarehouse() {
        return warehouseMapper.selectWarehouseList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addShelf(WarehouseShelf warehouseShelf) {
        warehouseShelfMapper.insert(warehouseShelf);
        List<WarehouseCell> cells = new ArrayList<>();
        for (int i = 1; i < warehouseShelf.getRow() + 1; i++) {
            for (int a = 1; a < warehouseShelf.getCol() + 1; a++) {
                WarehouseCell cell = new WarehouseCell();
                cell.setRow(i);
                cell.setCol(a);
                cell.setState(1);
                cell.setShelfId(warehouseShelf.getId());
                cells.add(cell);
            }
        }
        warehouseCellService.saveBatch(cells);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delWarehouse(Integer id) {
        warehouseShelfMapper.delete(Wrappers.<WarehouseShelf>lambdaUpdate().eq(WarehouseShelf::getWarehouseId, id));
        return warehouseMapper.deleteById(id);
    }

    @Override
    public int upWarehouse(Warehouse warehouse) {
        return warehouseMapper.updateById(warehouse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delShelf(Integer id) {
        warehouseCellMapper.update(null ,Wrappers.<WarehouseCell>lambdaUpdate().eq(WarehouseCell::getShelfId, id).set(WarehouseCell::getState, 0));
        return warehouseShelfMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upShelf(WarehouseShelf warehouseShelf) {
        warehouseCellMapper.update(null, Wrappers.<WarehouseCell>lambdaUpdate()
                .eq(WarehouseCell::getShelfId, warehouseShelf.getWarehouseId())
                .gt(WarehouseCell::getRow, warehouseShelf.getRow())
                .gt(WarehouseCell::getCol, warehouseShelf.getCol())
                .set(WarehouseCell::getState, 0));
        warehouseShelfMapper.updateById(warehouseShelf);
        return 0;
    }

    @Override
    public List<WarehouseCellAndSampleDto> getWarehouse(Integer shelfId) {
        return warehouseCellMapper.getWarehouse(shelfId);
    }

    @Override
    public int inWarehouse(String trees, String sampleCode) {
        String[] tree = trees.split("-");
        if(tree.length < 4){
            throw new ErrorException("库位选择错误");
        }
        List<InsSample> samples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getSampleCode, sampleCode).select(InsSample::getId,InsSample::getCellId));
        if(samples.size()!=1){
            throw new ErrorException("样品编号输入有误");
        }
        if(samples.get(0).getCellId()!=null){
            throw new ErrorException("样品 " + sampleCode + " 未出库");
        }
        WarehouseCell cell = warehouseCellMapper.selectOne(Wrappers.<WarehouseCell>lambdaQuery().eq(WarehouseCell::getShelfId, tree[1]).eq(WarehouseCell::getRow, tree[2]).eq(WarehouseCell::getCol, tree[3]).eq(WarehouseCell::getState, 1).select(WarehouseCell::getId,WarehouseCell::getRow,WarehouseCell::getCol));
        WarehouseShelf shelf = warehouseShelfMapper.selectById(tree[1]);
        Warehouse warehouse = warehouseMapper.selectById(tree[0]);
        WarehouseHistory history = new WarehouseHistory();
        history.setCellId(cell.getId());
        history.setState(1);
        history.setInsSampleId(samples.get(0).getId());
        history.setWarehouseCode(warehouse.getName()+"-"+shelf.getName()+"-"+cell.getRow()+"-"+cell.getCol());
        samples.get(0).setCellId(cell.getId());
        insSampleMapper.updateById(samples.get(0));
        return warehouseHistoryMapper.insert(history);
    }

    @Override
    public int outWarehouse(String sampleCode) {
        List<InsSample> samples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getSampleCode, sampleCode).select(InsSample::getId,InsSample::getCellId));
        if(samples.size()!=1){
            throw new ErrorException("样品编号输入有误");
        }
        if(samples.get(0).getCellId()==null){
            throw new ErrorException("样品 " + sampleCode + " 未入库");
        }
        WarehouseHistory history = new WarehouseHistory();
        history.setState(2);
        history.setInsSampleId(samples.get(0).getId());
        history.setCellId(samples.get(0).getCellId());
        history.setWarehouseCode(warehouseHistoryMapper.selectOne(Wrappers.<WarehouseHistory>lambdaQuery().eq(WarehouseHistory::getInsSampleId, samples.get(0).getId()).select(WarehouseHistory::getWarehouseCode).orderByDesc(WarehouseHistory::getId).last("limit 1")).getWarehouseCode());
        insSampleMapper.update(null, Wrappers.<InsSample>lambdaUpdate().eq(InsSample::getId, samples.get(0).getId()).set(InsSample::getCellId, null));
        return warehouseHistoryMapper.insert(history);
    }

    @Override
    public Map<String, Object> getSampleRecord(Integer id) {
        InsSample insSample = insSampleMapper.selectById(id);
        List<HistoryDto> histories = warehouseHistoryMapper.getHistoryListBySampleId(id);
        Map<String, Object> map = new HashMap<>();
        Map<String, String> sampleHistory = new HashMap<>();
        WarehouseHistory history = histories.get(histories.size() - 1);
        if(history.getState() == 1){
            sampleHistory.put("date", history.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sampleHistory.put("user", warehouseHistoryMapper.getUserNameById(history.getCreateUser()));
            sampleHistory.put("code", history.getWarehouseCode());
        }
        map.put("sampleHistory", sampleHistory);
        map.put("insSample", insSample);
        map.put("histories", histories);
        map.put("products", insProductMapper.getProductAndResult(id));
        return map;
    }

    @Override
    public int searchSampleId(String sampleCode) {
        List<InsSample> samples = insSampleMapper.selectList(Wrappers.<InsSample>lambdaQuery().eq(InsSample::getSampleCode, sampleCode).select(InsSample::getId));
        if(samples.size()!=1){
            throw new ErrorException("样品编号输入有误");
        }
        return samples.get(0).getId();
    }
}





package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.require.mapper.SuppliersDirectoryContentsMapper;
import com.ruoyi.require.pojo.SuppliersDirectoryContents;
import com.ruoyi.require.service.SuppliersDirectoryContentsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务和供应品采购目录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-12-17 06:14:51
 */
@Service
public class SuppliersDirectoryContentsServiceImpl extends ServiceImpl<SuppliersDirectoryContentsMapper, SuppliersDirectoryContents> implements SuppliersDirectoryContentsService {

    @Override
    public ArrayList<SuppliersDirectoryContents> directoryListing() {
        ArrayList<SuppliersDirectoryContents> list = new ArrayList<>();
        // 查出一级目录
        List<SuppliersDirectoryContents> firstLevel = baseMapper.selectList(new LambdaQueryWrapper<SuppliersDirectoryContents>()
                .isNull(SuppliersDirectoryContents::getParentId));

        // 查出含有父节点的 并通过父节点分组
        List<SuppliersDirectoryContents> seconds = baseMapper.selectList(new LambdaQueryWrapper<SuppliersDirectoryContents>()
                .isNotNull(SuppliersDirectoryContents::getParentId));
        if(firstLevel.size() > 0){
            Map<Integer, List<SuppliersDirectoryContents>> collect = seconds.stream()
                    .collect(Collectors.groupingBy(SuppliersDirectoryContents::getParentId));
            // 根据分组的key 赋值对应的children
            for (int i = 0; i < firstLevel.size(); i++) {
                recursion(firstLevel.get(i),collect);
                list.add(firstLevel.get(i));
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getSuppliersDirectoryContentsNodeNames() {
        List<SuppliersDirectoryContents> procurementSuppliesContents = baseMapper.selectList(null);
        List<Map<String, Object>> collect = new ArrayList<>();
        if(procurementSuppliesContents.size() > 0) {
            collect = procurementSuppliesContents.stream().map(item -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("nodeName", item.getNodeName());
                return map;
            }).collect(Collectors.toList());
        }
        return collect;
    }


    public SuppliersDirectoryContents recursion(SuppliersDirectoryContents firstLevel, Map<Integer, List<SuppliersDirectoryContents>> collect) {
        // 将父节点的children赋值
        if(collect.containsKey(firstLevel.getId())) {
            List<SuppliersDirectoryContents> procurementSupplies = collect.get(firstLevel.getId());
            firstLevel.setChildren(procurementSupplies);
            for (int i = 0; i < procurementSupplies.size(); i++) {
                recursion(procurementSupplies.get(i),collect);
            }
        }
        return firstLevel;
    }
}

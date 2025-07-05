package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.require.mapper.ProcurementSuppliesContentsMapper;
import com.ruoyi.require.pojo.ProcurementSuppliesContents;
import com.ruoyi.require.service.ProcurementSuppliesContentsService;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProcurementSuppliesContentsServiceImpl extends ServiceImpl<ProcurementSuppliesContentsMapper, ProcurementSuppliesContents>
        implements ProcurementSuppliesContentsService {

    private UserMapper userMapper;

    @Override
    public List<ProcurementSuppliesContents> directoryListing() {
        ArrayList<ProcurementSuppliesContents> list = new ArrayList<>();
        // 查出一级目录
        List<ProcurementSuppliesContents> firstLevel = baseMapper.selectList(new LambdaQueryWrapper<ProcurementSuppliesContents>()
                .isNull(ProcurementSuppliesContents::getParentId));

        // 查出含有父节点的 并通过父节点分组
        List<ProcurementSuppliesContents> seconds = baseMapper.selectList(new LambdaQueryWrapper<ProcurementSuppliesContents>()
                .isNotNull(ProcurementSuppliesContents::getParentId));
        if(firstLevel.size() > 0){
            Map<Integer, List<ProcurementSuppliesContents>> collect = seconds.stream()
                    .collect(Collectors.groupingBy(ProcurementSuppliesContents::getParentId));
            // 根据分组的key 赋值对应的children
            for (int i = 0; i < firstLevel.size(); i++) {
                recursion(firstLevel.get(i),collect);
                list.add(firstLevel.get(i));
            }
        }
        return list;
    }

    // 递归赋值children
    public ProcurementSuppliesContents recursion(ProcurementSuppliesContents firstLevel, Map<Integer, List<ProcurementSuppliesContents>> collect) {
        // 将父节点的children赋值
        if(collect.containsKey(firstLevel.getId())) {
            List<ProcurementSuppliesContents> procurementSupplies = collect.get(firstLevel.getId());
            firstLevel.setChildren(procurementSupplies);
            for (int i = 0; i < procurementSupplies.size(); i++) {
                recursion(procurementSupplies.get(i),collect);
            }
        }
        return firstLevel;
    }

    @Override
    public List<Map<String, Object>> getNodeNames() {
        List<ProcurementSuppliesContents> procurementSuppliesContents = baseMapper.selectList(null);
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

    @Override
    public List<Map<String, Object>> getUserList() {
        // 1 停用
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getStatus, 0));
        List<Map<String, Object>> collect = new ArrayList<>();
        if(users.size() > 0) {
            collect = users.stream().map(item -> {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", item.getId());
                map.put("nodeName", item.getName());
                return map;
            }).collect(Collectors.toList());
        }
        return collect;
    }
}

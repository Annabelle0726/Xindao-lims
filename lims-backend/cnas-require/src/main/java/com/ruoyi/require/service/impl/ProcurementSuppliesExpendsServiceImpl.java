package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.require.dto.ProcurementSuppliesExpendDto;
import com.ruoyi.require.mapper.ProcurementSuppliesExpendsMapper;
import com.ruoyi.require.mapper.ProcurementSuppliesListMapper;
import com.ruoyi.require.pojo.ProcurementSuppliesExpends;
import com.ruoyi.require.pojo.ProcurementSuppliesList;
import com.ruoyi.require.service.ProcurementSuppliesExpendsService;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-15 03:47:19
 */
@Service
public class ProcurementSuppliesExpendsServiceImpl extends ServiceImpl<ProcurementSuppliesExpendsMapper, ProcurementSuppliesExpends> implements ProcurementSuppliesExpendsService {
    @Autowired
    private ProcurementSuppliesExpendsMapper expendsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProcurementSuppliesListMapper listMapper;

    @Override
    public List<ProcurementSuppliesExpendDto> selectAll(Long procurementSuppliesListId) {
        List<ProcurementSuppliesExpendDto> res = new ArrayList<>();
        List<ProcurementSuppliesExpends> list =  expendsMapper.selectList(new QueryWrapper<ProcurementSuppliesExpends>().lambda().eq(ProcurementSuppliesExpends::getListId, procurementSuppliesListId));
        for (ProcurementSuppliesExpends procurementSuppliesExpend : list) {
            ProcurementSuppliesExpendDto dto = new ProcurementSuppliesExpendDto();
            BeanUtils.copyProperties(procurementSuppliesExpend, dto);

            User enterUser = userMapper.selectById(procurementSuppliesExpend.getEnterUserId());
            User updateUser = userMapper.selectById(procurementSuppliesExpend.getUpdateUserId());
            dto.setEnterUserName(enterUser.getName());
            dto.setUpdateUserName(updateUser.getName());
            res.add(dto);
        }

        return res;
    }

    @Override
    @Transactional
    public Integer addExpends(ProcurementSuppliesExpendDto expendDto) throws ServiceException {
        ProcurementSuppliesList list = listMapper.selectProcurementSuppliesListForUpdate(expendDto.getListId());

        ProcurementSuppliesExpends expends = new ProcurementSuppliesExpends();
        BeanUtils.copyProperties(expendDto, expends);
        expends.setEnterUserId(SecurityUtils.getUserId().intValue());
        expends.setUpdateUserId(SecurityUtils.getUserId().intValue());
        if (list.getCurrentAmount() < expendDto.getAmount()) {
            return 0;
        }

        list.setCurrentAmount(list.getCurrentAmount() - expendDto.getAmount());

        listMapper.updateById(list);
        return expendsMapper.insert(expends);
    }

    @Override
    @Transactional
    public Integer deleteExpends(Long id) {
        ProcurementSuppliesExpends expend = expendsMapper.selectById(id);
        ProcurementSuppliesList list = listMapper.selectProcurementSuppliesListForUpdate(expend.getListId());
        list.setCurrentAmount(list.getCurrentAmount() + expend.getAmount());
        list.setUpdateUser(SecurityUtils.getUserId().intValue());
        listMapper.updateById(list);
        return expendsMapper.deleteById(id);
    }
}

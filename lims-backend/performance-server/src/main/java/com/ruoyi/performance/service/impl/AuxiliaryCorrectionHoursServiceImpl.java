package com.ruoyi.performance.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.performance.dto.AuxiliaryCorrectionHoursDto;
import com.ruoyi.performance.mapper.AuxiliaryCorrectionHoursMapper;
import com.ruoyi.performance.pojo.AuxiliaryCorrectionHours;
import com.ruoyi.performance.service.AuxiliaryCorrectionHoursService;
import com.ruoyi.system.mapper.UserMapper;
import org.apache.commons.math3.analysis.function.Power;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 工时统计的修正工时 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-29 02:38:19
 */
@Service
public class AuxiliaryCorrectionHoursServiceImpl extends ServiceImpl<AuxiliaryCorrectionHoursMapper, AuxiliaryCorrectionHours> implements AuxiliaryCorrectionHoursService {

    @Resource
    AuxiliaryCorrectionHoursMapper auxiliaryCorrectionHoursMapper;

    @Resource
    UserMapper userMapper;


    @Override
    public IPage<AuxiliaryCorrectionHoursDto> selectAuxiliaryCorrectionHours(Page page, AuxiliaryCorrectionHoursDto auxiliaryCorrectionHoursDto) {
        List<Integer> ids = new ArrayList<>();
        String departLims = auxiliaryCorrectionHoursDto.getDepartLims();
        auxiliaryCorrectionHoursDto.setDepartLims(null);
        if (ObjectUtils.isNotEmpty(departLims)) {
            //先模糊查询出来id
            List<Integer> ides = auxiliaryCorrectionHoursMapper.selDepartLimsByName(departLims);
            for (Integer ide : ides) {
                List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                        .like(User::getDepartLimsId, ide));
                if (ObjectUtils.isNotEmpty(users)) {
                    ids.clear();
                    ids.addAll(users.stream().map(User::getId).distinct().collect(Collectors.toList()));
                }
            }
        }
        if (ids.size() == 0) {
            ids = null;
        }
        return auxiliaryCorrectionHoursMapper.selectAuxiliaryCorrectionHours(page, QueryWrappers.queryWrappers(auxiliaryCorrectionHoursDto).eq("month", auxiliaryCorrectionHoursDto.getMonth()), ids);
    }

    //导入上传
    @Override
    public void importExcel(List<AuxiliaryCorrectionHoursDto> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        List<AuxiliaryCorrectionHours> auxiliaryCorrectionHoursList = new ArrayList<>();
        List<AuxiliaryCorrectionHours> auxiliaryCorrectionHoursList1 = new ArrayList<>();
        for (AuxiliaryCorrectionHoursDto auxiliaryCorrectionHoursDto : list) {
            AuxiliaryCorrectionHours auxiliaryCorrectionHours = new AuxiliaryCorrectionHours();
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getName, auxiliaryCorrectionHoursDto.getName()));
            if (ObjectUtils.isEmpty(user)) {
                throw new RuntimeException("系统没有查到" + auxiliaryCorrectionHoursDto.getName() + "这个用户信息!");
            }
            String regex = "\\d{4}-\\d{2}";
            if (!Pattern.matches(regex, auxiliaryCorrectionHoursDto.getMonth())) {
                throw new RuntimeException(auxiliaryCorrectionHoursDto.getMonth() + "格式不正确,月份格式应为yyyy-MM");
            }
            BeanUtils.copyProperties(auxiliaryCorrectionHoursDto, auxiliaryCorrectionHours);
            auxiliaryCorrectionHours.setNameUser(user.getId().intValue());
            AuxiliaryCorrectionHours auxiliaryCorrectionHours1 = auxiliaryCorrectionHoursMapper.selectOne(Wrappers.<AuxiliaryCorrectionHours>lambdaQuery()
                    .eq(AuxiliaryCorrectionHours::getNameUser, user.getId())
                    .eq(AuxiliaryCorrectionHours::getType, auxiliaryCorrectionHours.getType())
                    .eq(AuxiliaryCorrectionHours::getMonth, auxiliaryCorrectionHours.getMonth()));
            if (ObjectUtils.isNotEmpty(auxiliaryCorrectionHours1)) {
                auxiliaryCorrectionHoursList1.add(auxiliaryCorrectionHours);
            } else {
                auxiliaryCorrectionHoursList.add(auxiliaryCorrectionHours);
            }
        }
        //批量新增
        saveBatch(auxiliaryCorrectionHoursList);
        //批量修改
        updateBatchById(auxiliaryCorrectionHoursList1);
    }
}

package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.personnel.dto.PersonTrainingDetailedDto;
import com.ruoyi.personnel.excel.PersonTrainingDetailedUpload;
import com.ruoyi.personnel.mapper.PersonTrainingDetailedMapper;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import com.ruoyi.personnel.service.PersonTrainingDetailedService;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 培训计划详情 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-11 01:46:27
 */
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class PersonTrainingDetailedServiceImpl extends ServiceImpl<PersonTrainingDetailedMapper, PersonTrainingDetailed> implements PersonTrainingDetailedService {


    private UserMapper userMapper;

    @Override
    public void importExcel(List<PersonTrainingDetailedUpload> list, Integer planId) {
        List<PersonTrainingDetailed> personTrainingDetailedList = new ArrayList<>();
        list.forEach(i -> {
            PersonTrainingDetailed personTrainingDetailed = new PersonTrainingDetailed();
            BeanUtils.copyProperties(i, personTrainingDetailed);

            // 匹配讲师
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getName, i.getTrainingLecturerName()));
            if (ObjectUtils.isEmpty(user)) {
                throw new ErrorException("未找到该讲师：" + i.getTrainingLecturerName());
            }
            personTrainingDetailed.setTrainingLecturerId(user.getId());
            personTrainingDetailed.setPlanId(planId);
            personTrainingDetailed.setState(3);

            personTrainingDetailed.setTrainingDate(i.getTrainingDate());


            personTrainingDetailedList.add(personTrainingDetailed);
        });
        // 批量新增
        if (CollectionUtils.isNotEmpty(personTrainingDetailedList)) {
            baseMapper.insertBatchSomeColumn(personTrainingDetailedList);
        }
    }

    @Override
    public void deleteAnnualPlanDetailTable(String ids) {
        String[] split = ids.split(",");
        if (split.length > 0) {
            for (String s : split) {
                baseMapper.deleteById(s);
            }
        }
    }

    @Override
    public IPage<PersonTrainingDetailedDto> queryTheAnnualPlanDetailsTable(Page page, String trainingLecturerName, String courseCode, String trainingDate, Integer id, Integer userId) {
        return baseMapper.queryTheAnnualPlanDetailsTable(page, trainingLecturerName, courseCode, trainingDate, id, userId, null);
    }
}

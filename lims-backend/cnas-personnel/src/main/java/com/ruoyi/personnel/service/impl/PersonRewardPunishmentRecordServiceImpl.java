package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.personnel.dto.PersonRewardPunishmentRecordDto;
import com.ruoyi.personnel.excel.PersonRewardPunishmentRecordExcel;
import com.ruoyi.personnel.mapper.PersonRewardPunishmentRecordMapper;
import com.ruoyi.personnel.pojo.PersonRewardPunishmentRecord;
import com.ruoyi.personnel.service.PersonRewardPunishmentRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 奖惩记录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-08 11:25:02
 */
@Service
public class PersonRewardPunishmentRecordServiceImpl extends ServiceImpl<PersonRewardPunishmentRecordMapper, PersonRewardPunishmentRecord> implements PersonRewardPunishmentRecordService {

    @Override
    public IPage<PersonRewardPunishmentRecordDto> rewardPunishmentPage(Page page, Integer userId, String userName, Date startTime, Date endTime, Integer departmentId) {
        return baseMapper.rewardPunishmentPage(page, userId, userName, startTime, endTime, departmentId);
    }

    @Override
    public List<PersonRewardPunishmentRecordExcel> rewardPunishmentExport(Integer userId, Integer departmentId, String userName, Date startTime, Date endTime) {
        return baseMapper.rewardPunishmentExport(userId, departmentId, userName, startTime, endTime);
    }
}

package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonRewardPunishmentRecordDto;
import com.ruoyi.personnel.excel.PersonRewardPunishmentRecordExcel;
import com.ruoyi.personnel.pojo.PersonRewardPunishmentRecord;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 奖惩记录 服务类
 * </p>
 *
 * @author
 * @since 2024-10-08 11:25:02
 */
public interface PersonRewardPunishmentRecordService extends IService<PersonRewardPunishmentRecord> {

    IPage<PersonRewardPunishmentRecordDto> rewardPunishmentPage(Page page,
                                                                Integer userId,
                                                                String userName,
                                                                Date startTime,
                                                                Date endTime,
                                                                Integer departmentId);

    List<PersonRewardPunishmentRecordExcel> rewardPunishmentExport(Integer userId, Integer departmentId, String userName, Date startTime, Date endTime);
}

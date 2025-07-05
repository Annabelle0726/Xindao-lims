package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonRewardPunishmentRecordDto;
import com.ruoyi.personnel.excel.PersonRewardPunishmentRecordExcel;
import com.ruoyi.personnel.pojo.PersonRewardPunishmentRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 奖惩记录 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-10-08 11:25:02
 */
public interface PersonRewardPunishmentRecordMapper extends BaseMapper<PersonRewardPunishmentRecord> {

    IPage<PersonRewardPunishmentRecordDto> rewardPunishmentPage(Page page, @Param("userId") Integer userId, @Param("userName") String userName,@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("departmentId") Integer departmentId);

    List<PersonRewardPunishmentRecordExcel> rewardPunishmentExport(@Param("userId") Integer userId, @Param("departmentId") Integer departmentId, @Param("userName") String userName, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}

package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonTrainingRecordDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordListDto;
import com.ruoyi.personnel.dto.TrainingRecordPersonDetailedDto;
import com.ruoyi.personnel.pojo.PersonTrainingRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 培训记录 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-10-12 04:50:48
 */
public interface PersonTrainingRecordMapper extends BaseMapper<PersonTrainingRecord> {

    List<PersonTrainingRecordDto> trainingAndAssessmentRecordsPage(@Param("trainingDetailedId") Integer trainingDetailedId,@Param("userName") String userName);

    IPage<PersonTrainingRecordListDto> personnelTrainingPersonnel(Page page, @Param("userName") String userName, @Param("userId") Integer userId,@Param("departLimsId") Integer departLimsId);

    IPage<TrainingRecordPersonDetailedDto> queryPersonnelDetails(Page page, Integer userId);

    /**
     * 根据详情id查询培训人员
     * @param trainingDetailedId
     * @return
     */
    List<PersonTrainingRecordDto> selectListByTrainingDetailedId(@Param("trainingDetailedId") Integer trainingDetailedId);

    /**
     * 查询人员信息
     * @param userId
     * @return
     */
    PersonTrainingRecordListDto selectUserTraining(@Param("userId") Integer userId);

    /**
     * 根据用户id查询培训记录
     * @param userId
     * @return
     */
    List<TrainingRecordPersonDetailedDto> selectPersonDetailedDtos(Integer userId);

    /**
     * 根据用户id和年份查询人员明细 培训记录
     * @param page
     * @param userId
     * @param year
     * @return
     */
    IPage<TrainingRecordPersonDetailedDto> queryPersonnelDetailsOfUserIdAndYear(Page page, @Param("userId") Integer userId, @Param("year") Integer year);

    /**
     * 根据用户id和年份查询人员明细 培训记录导出
     * @param userId
     * @param trainingDate
     * @return
     */
    List<TrainingRecordPersonDetailedDto> selectPersonDetailedDtosByTrainingDate(@Param("userId") Integer userId, @Param("year") Integer year);
}

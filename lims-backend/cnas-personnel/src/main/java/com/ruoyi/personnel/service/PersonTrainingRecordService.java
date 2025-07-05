package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonTrainingRecordDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordListDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordSubmitDto;
import com.ruoyi.personnel.dto.TrainingRecordPersonDetailedDto;
import com.ruoyi.personnel.pojo.PersonTrainingRecord;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 培训记录 服务类
 * </p>
 *
 * @author
 * @since 2024-10-12 04:50:48
 */
public interface PersonTrainingRecordService extends IService<PersonTrainingRecord> {

    List<PersonTrainingRecordDto> trainingAndAssessmentRecordsPage(Integer trainingDetailedId, String userName);

    void deleteTrainingAndAssessmentRecords(String ids);

    IPage<PersonTrainingRecordListDto> personnelTrainingPersonnel(Page page,
                                                                  String userName, Integer userId, Integer departLimsId);


    void claimOfTrainingAndAssessmentRecords(Boolean claimAndClaim, Integer courseId);


    /**
     * 根据用户id和年份查询人员明细 培训记录
     * @param page
     * @param userId
     * @param year
     * @return
     */
    IPage<TrainingRecordPersonDetailedDto> queryPersonnelDetailsOfUserIdAndYear(Page page, Integer userId, Integer year);

    /**
     * 导出人员培训记录
     * @param userId
     * @param trainingDate
     * @param response
     */
    void exportTrainingRecordAddTrainingDate(Integer userId, Integer trainingDate, HttpServletResponse response);

    /**
     * 培训提交
     * @param personTrainingRecordSubmitDto
     */
    void trainingAndAssessmentRecordsAdded(PersonTrainingRecordSubmitDto personTrainingRecordSubmitDto);

}

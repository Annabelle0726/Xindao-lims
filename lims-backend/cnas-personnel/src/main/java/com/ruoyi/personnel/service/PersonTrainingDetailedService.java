package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonTrainingDetailedDto;
import com.ruoyi.personnel.excel.PersonTrainingDetailedUpload;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;

import java.util.List;

/**
 * <p>
 * 培训计划详情 服务类
 * </p>
 *
 * @author
 * @since 2024-10-11 01:46:27
 */
public interface PersonTrainingDetailedService extends IService<PersonTrainingDetailed> {

    void importExcel(List<PersonTrainingDetailedUpload> list, Integer planId);

    void deleteAnnualPlanDetailTable(String ids);

    IPage<PersonTrainingDetailedDto> queryTheAnnualPlanDetailsTable(Page page,
                                                                    String trainingLecturerName,
                                                                    String courseCode, String trainingDate, Integer id, Integer userId);
}

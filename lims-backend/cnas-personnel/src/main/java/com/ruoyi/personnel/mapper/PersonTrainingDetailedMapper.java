package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.framework.mybatis_config.MyBaseMapper;
import com.ruoyi.personnel.dto.PersonTrainingDetailedDto;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 培训计划详情 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-10-11 01:46:27
 */
public interface PersonTrainingDetailedMapper extends MyBaseMapper<PersonTrainingDetailed> {

    IPage<PersonTrainingDetailedDto> queryTheAnnualPlanDetailsTable(Page page,
                                                                    @Param("trainingLecturerName") String trainingLecturerName,
                                                                    @Param("courseCode") String courseCode,
                                                                    @Param("trainingDate") String trainingDate,
                                                                    @Param("id") Integer id,
                                                                    @Param("userId") Integer userId,
                                                                    @Param("loginUserId") Integer loginUserId);

    /**
     * 根据主表id查询详情
     * @param trainingId
     * @return
     */
    List<PersonTrainingDetailedDto> selectTrainingList(@Param("trainingId") Integer trainingId);

    /**
     * 查询详细
     * @param id
     * @return
     */
    PersonTrainingDetailedDto selectTrainingDetail(@Param("id") Integer id);

    /**
     * 查询今年人员培训信息
     * @return
     */
    List<PersonTrainingDetailed> getThisYearTrainingDetailed();
}

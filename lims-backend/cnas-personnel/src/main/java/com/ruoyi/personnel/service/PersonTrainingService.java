package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonTrainingDto;
import com.ruoyi.personnel.pojo.PersonTraining;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 培训计划 服务类
 * </p>
 *
 * @author
 * @since 2024-10-11 01:11:49
 */
public interface PersonTrainingService extends IService<PersonTraining> {

    IPage<PersonTrainingDto> personTrainingSelect(Page page,
                                                  @Param("compilerName") String compilerName, @Param("departmentId") String departmentId);

    void personTrainingImport(MultipartFile file, PersonTraining training);

    void personTrainingDelete(Integer id);

    void reviewAnnualPersonnelTraining(PersonTraining training);

    void approveAnnualPersonnelTraining(PersonTraining training);

    /**
     * 导出人员培训计划
     * @param id
     * @param response
     */
    void exportPersonTraining(Integer id, HttpServletResponse response);

    /**
     * 导出人员培训与考核记录
     * @param id
     * @param response
     */
    void exportPersonTrainingRecord(Integer id, HttpServletResponse response);

    /**
     * 人员培训详情附件新增
     * @param trainingDetailedId
     * @param file
     * @return
     */
    boolean uploadTrainingDetailedFile(Integer trainingDetailedId, MultipartFile file);

    /**
     * 查询今年人员培训信息
     * @return
     */
    List<PersonTrainingDetailed> getThisYearTrainingDetailed();

}

package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.numgen.NumberGenerator;
import com.ruoyi.personnel.dto.PersonTrainingDetailedDto;
import com.ruoyi.personnel.dto.PersonTrainingDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordSubmitDto;
import com.ruoyi.personnel.mapper.PersonTrainingDetailedFileMapper;
import com.ruoyi.personnel.pojo.PersonTraining;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import com.ruoyi.personnel.pojo.PersonTrainingDetailedFile;
import com.ruoyi.personnel.pojo.PersonTrainingRecord;
import com.ruoyi.personnel.service.PersonTrainingDetailedService;
import com.ruoyi.personnel.service.PersonTrainingRecordService;
import com.ruoyi.personnel.service.PersonTrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 培训计划 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-11 01:11:49
 */
@Api(tags = "人员 - 培训计划")
@RestController
@RequestMapping("/personTraining")
public class PersonTrainingController {

    @Autowired
    private PersonTrainingService personTrainingService;

    @Autowired
    private PersonTrainingDetailedService personTrainingDetailedService;

    @Autowired
    private PersonTrainingRecordService personTrainingRecordService;

    @Autowired
    private PersonTrainingDetailedFileMapper personTrainingDetailedFileMapper;

    @Autowired
    private NumberGenerator<PersonTrainingDetailed> numberGenerator;

    @ApiOperation(value = "培训计划 导入")
    @PostMapping("personTrainingImport")
    public Result<?> personTrainingImport(@RequestPart("file") MultipartFile file, PersonTraining training) {
        personTrainingService.personTrainingImport(file, training);
        return Result.success();
    }

    @ApiOperation(value = "培训计划 删除")
    @DeleteMapping("personTrainingDelete")
    public Result<?> personTrainingDelete(@RequestParam("id") Integer id) {
        personTrainingService.personTrainingDelete(id);
        return Result.success();
    }

    @ApiOperation(value = "培训计划 查询")
    @GetMapping("personTrainingSelect")
    public Result<IPage<PersonTrainingDto>> personTrainingSelect(Page page, String compilerName, String departmentId) {
        IPage<PersonTrainingDto> iPage = personTrainingService.personTrainingSelect(page, compilerName, departmentId);
        return Result.success(iPage);
    }

    @ApiOperation(value = "年度培训计划 审核")
    @PostMapping("reviewAnnualPersonnelTraining")
    public Result<?> reviewAnnualPersonnelTraining(@RequestBody PersonTraining training) {
        personTrainingService.reviewAnnualPersonnelTraining(training);
        return Result.success();
    }

    @ApiOperation(value = "培训计划 批准")
    @PostMapping("approveAnnualPersonnelTraining")
    public Result<?> approveAnnualPersonnelTraining(@RequestBody PersonTraining training) {
        personTrainingService.approveAnnualPersonnelTraining(training);
        return Result.success();
    }


    @ApiOperation(value = "年度计划明细表 新增/编辑")
    @PostMapping("addOrUpdatePersonTrainingDetailed")
    public Result<?> addOrUpdatePersonTrainingDetailed(@RequestBody PersonTrainingDetailed personTrainingDetailed) {
        if (ObjectUtils.isEmpty(personTrainingDetailed.getId())) {
            personTrainingDetailed.setState(3);
        }
        personTrainingDetailedService.saveOrUpdate(personTrainingDetailed);
        return Result.success();
    }


    @ApiOperation(value = "年度计划明细表 批量删除")
    @DeleteMapping("deleteAnnualPlanDetailTable")
    public Result<?> deleteAnnualPlanDetailTable(String ids) {
        personTrainingDetailedService.deleteAnnualPlanDetailTable(ids);
        return Result.success();
    }


    @ApiOperation(value = "年度计划明细表 查询")
    @GetMapping("queryTheAnnualPlanDetailsTable")
    public Result<IPage<PersonTrainingDetailedDto>> queryTheAnnualPlanDetailsTable(Page page,
                                                                                   String trainingLecturerName, String courseCode,
                                                                                   String trainingDate, Integer id, Integer userId) {
        IPage<PersonTrainingDetailedDto> iPage = personTrainingDetailedService.queryTheAnnualPlanDetailsTable(page,
                trainingLecturerName, courseCode, trainingDate, id, userId);
        return Result.success(iPage);
    }


    @ApiOperation(value = "培训与考核记录 查询")
    @GetMapping("trainingAndAssessmentRecordsPage")
    public Result<List<PersonTrainingRecordDto>> trainingAndAssessmentRecordsPage(Integer trainingDetailedId,
                                                                                   String userName) {
        List<PersonTrainingRecordDto> list = personTrainingRecordService.trainingAndAssessmentRecordsPage(trainingDetailedId, userName);
        return Result.success(list);
    }


    @ApiOperation(value = "培训与考核记录 新增人员")
    @PostMapping("newPersonnelAddedToTrainingRecords")
    public Result<?> newPersonnelAddedToTrainingRecords(@RequestBody List<PersonTrainingRecord> personTrainingRecord) {
        personTrainingRecordService.saveBatch(personTrainingRecord);
        return Result.success();
    }


    @ApiOperation(value = "培训与考核记录 认领")
    @PostMapping("claimOfTrainingAndAssessmentRecords")
    public Result<?> claimOfTrainingAndAssessmentRecords(@RequestParam("claimAndClaim") Boolean claimAndClaim,
                                                         @RequestParam("courseId") Integer courseId) {
        personTrainingRecordService.claimOfTrainingAndAssessmentRecords(claimAndClaim, courseId);
        return Result.success();
    }


    @ApiOperation(value = "培训与考核记录 提交/撤销")
    @PostMapping("trainingAndAssessmentRecordsAdded")
    public Result<?> trainingAndAssessmentRecordsAdded(@RequestBody PersonTrainingRecordSubmitDto personTrainingRecordSubmitDto) {
        personTrainingRecordService.trainingAndAssessmentRecordsAdded(personTrainingRecordSubmitDto);
        return Result.success();
    }

    @ApiOperation(value = "培训与考核记录 评价")
    @PostMapping("trainingAndAssessmentRecordsEvaluate")
    public Result<?> trainingAndAssessmentRecordsEvaluate(@RequestBody PersonTrainingRecordSubmitDto personTrainingRecordSubmitDto) {
        personTrainingDetailedService.update(Wrappers.<PersonTrainingDetailed>lambdaUpdate()
                .eq(PersonTrainingDetailed::getId, personTrainingRecordSubmitDto.getTrainingDetailedId())
                .set(PersonTrainingDetailed::getComprehensiveAssessment, personTrainingRecordSubmitDto.getComprehensiveAssessment())
                .set(PersonTrainingDetailed::getAssessmentDate, personTrainingRecordSubmitDto.getAssessmentDate())
                .set(PersonTrainingDetailed::getState, personTrainingRecordSubmitDto.getState()));
        return Result.success();
    }


    @ApiOperation(value = "培训与考核记录 删除")
    @DeleteMapping("deleteTrainingAndAssessmentRecords")
    public Result<?> deleteTrainingAndAssessmentRecords(String ids) {
        personTrainingRecordService.deleteTrainingAndAssessmentRecords(ids);
        return Result.success();
    }

    @PostMapping("outOfFocusPreservation")
    public Result<?> outOfFocusPreservation(@RequestBody PersonTrainingRecord personTrainingRecord) {
        personTrainingRecordService.updateById(personTrainingRecord);
        return Result.success();
    }

    /**
     * 导出人员培训计划
     * @return
     */

    @ApiOperation(value = "导出人员培训计划")
    @GetMapping("/exportPersonTraining")
    public void exportPersonTraining(Integer id, HttpServletResponse response){
        personTrainingService.exportPersonTraining(id, response);
    }

    /**
     * 导出人员培训与考核记录
     * @return
     */
    @ApiOperation(value = "导出人员培训与考核记录")
    @GetMapping("/exportPersonTrainingRecord")
    public void exportPersonTrainingRecord(Integer id, HttpServletResponse response){
        personTrainingService.exportPersonTrainingRecord(id, response);
    }

    /**
     * 人员培训详情附件新增
     * @param trainingDetailedId
     * @param file
     * @return
     */
    @ApiOperation(value = "人员培训详情附件新增")
    @PostMapping("/uploadTrainingDetailedFile")
    public Result<?> uploadTrainingDetailedFile(Integer trainingDetailedId, MultipartFile file) {
        return Result.success(personTrainingService.uploadTrainingDetailedFile(trainingDetailedId, file));
    }


    /**
     * 人员培训详情附件列表
     * @return
     */
    @ApiOperation(value = "人员培训详情附件列表")
    @GetMapping("/getTrainingDetailedFileList")
    public Result<List<PersonTrainingDetailedFile>> getTrainingDetailedFileList(Integer trainingDetailedId){
        return Result.success(personTrainingDetailedFileMapper.selectList(Wrappers.<PersonTrainingDetailedFile>lambdaQuery()
                .eq(PersonTrainingDetailedFile::getTrainingDetailedId, trainingDetailedId)));
    }

    /**
     * 人员培训详情附件删除
     * @return
     */
    @ApiOperation(value = "人员培训详情附件删除")
    @DeleteMapping("/delTrainingDetailedFileList")
    public Result delTrainingDetailedFileList(Integer detailedFileId){
        return Result.success(personTrainingDetailedFileMapper.deleteById(detailedFileId));
    }

    /**
     * 查询今年人员培训信息
     * @return
     */
    @ApiOperation(value = "查询今年人员培训信息")
    @GetMapping("/getThisYearTrainingDetailed")
    public Result<List<PersonTrainingDetailed>> getThisYearTrainingDetailed(){
        return Result.success(personTrainingService.getThisYearTrainingDetailed());
    }
}

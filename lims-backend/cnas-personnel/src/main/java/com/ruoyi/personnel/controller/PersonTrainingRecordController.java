package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.personnel.dto.PersonTrainingRecordListDto;
import com.ruoyi.personnel.dto.TrainingRecordPersonDetailedDto;
import com.ruoyi.personnel.service.PersonTrainingRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 培训计划 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-11 01:11:49
 */
@Api(tags = "人员 - 培训记录")
@RestController
@RequestMapping("/personTrainingRecord")
public class PersonTrainingRecordController {

    @Autowired
    private PersonTrainingRecordService personTrainingRecordService;

    @ApiOperation(value = "查询人员 培训记录")
    @GetMapping("trainingSelectTrainingRecord")
    public Result<IPage<PersonTrainingRecordListDto>> trainingSelectTrainingRecord(Page page, String userName, Integer userId, Integer departmentId) {
        IPage<PersonTrainingRecordListDto> iPage = personTrainingRecordService.personnelTrainingPersonnel(page, userName, userId, departmentId);
        return Result.success(iPage);
    }


    @ApiOperation(value = "查询人员明细 培训记录")
    @GetMapping("queryPersonnelDetails")
    public Result<IPage<TrainingRecordPersonDetailedDto>> queryPersonnelDetails(Page page, Integer userId, Integer trainingDate) {
        IPage<TrainingRecordPersonDetailedDto> iPage = personTrainingRecordService.queryPersonnelDetailsOfUserIdAndYear(page, userId, trainingDate); // 新增根据年份查询
        return Result.success(iPage);
    }

    /**
     * 导出人员培训记录
     * @return
     */
    @ApiOperation(value = "导出人员培训记录")
    @GetMapping("/exportTrainingRecord")
    public void exportTrainingRecord(Integer userId, Integer trainingDate, HttpServletResponse response){
        personTrainingRecordService.exportTrainingRecordAddTrainingDate(userId, trainingDate, response);// 新增根据年份查询

    }
}

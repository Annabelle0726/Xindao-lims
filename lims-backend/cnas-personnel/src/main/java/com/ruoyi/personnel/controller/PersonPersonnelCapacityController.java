package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.personnel.dto.PersonPersonnelCapacityDto;
import com.ruoyi.personnel.pojo.PersonPersonnelCapacity;
import com.ruoyi.personnel.service.PersonPersonnelCapacityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * <p>
 * 人员能力 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-10 11:26:18
 */
@Api(tags = "人员 - 人员能力")
@RestController
@RequestMapping("/personPersonnelCapacity")
public class PersonPersonnelCapacityController {

    @Autowired
    private PersonPersonnelCapacityService personPersonnelCapacityService;


    @ApiOperation(value = "新增/更新 人员能力")
    @PostMapping("addOrUpdatePersonPersonnelCapacity")
    public Result<?> addOrUpdatePersonPersonnelCapacity(@RequestBody PersonPersonnelCapacity personPersonnelCapacity) {
        personPersonnelCapacityService.saveOrUpdate(personPersonnelCapacity);
        return Result.success();
    }

    @ApiOperation(value = "删除 人员能力")
    @DeleteMapping("deletePersonPersonnelCapacity")
    public Result<?> deletePersonPersonnelCapacity(@RequestParam("id") Integer id) {
        // 删除数据
        personPersonnelCapacityService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "查询 人员能力")
    @GetMapping("personPersonnelCapacityPage")
    public Result<IPage<PersonPersonnelCapacityDto>> personPersonnelCapacityPage(Page page,
                                                                                 Integer departmentId,
                                                                                 Integer userId,
                                                                                 String userName) {
        return Result.success(personPersonnelCapacityService.personPersonnelCapacityPage(page, departmentId, userId, userName));
    }

    @ApiOperation(value = "确认 人员能力")
    @PostMapping("confirmPersonnelCapability")
    public Result<?> confirmPersonnelCapability(@RequestBody PersonPersonnelCapacity personPersonnelCapacity) {
        Integer userId = SecurityUtils.getUserId().intValue();
        personPersonnelCapacityService.update(Wrappers.<PersonPersonnelCapacity>lambdaUpdate()
                .eq(PersonPersonnelCapacity::getId, personPersonnelCapacity.getId())
                .set(PersonPersonnelCapacity::getConfirmOperatingPersonnelId, userId)
                .set(PersonPersonnelCapacity::getConfirmDate, personPersonnelCapacity.getConfirmDate()));
        return Result.success();
    }

    /**
     * 导出人员能力
     * @return
     */
    @ApiOperation(value = "导出人员能力")
    @GetMapping("/exportPersonnelCapacity")
    public void exportPersonnelCapacity(Integer id, HttpServletResponse response){
        personPersonnelCapacityService.exportPersonnelCapacity(id, response);
    }

    /**
     * 提交确认人员能力
     * @param personPersonnelCapacity
     * @return
     */
    @ApiOperation(value = "提交")
    @PostMapping("submitConfirmPersonnelCapability")
    public Result<?> submitConfirmPersonnelCapability(@RequestBody PersonPersonnelCapacity personPersonnelCapacity) {
        personPersonnelCapacityService.submitConfirmPersonnelCapability(personPersonnelCapacity);
        return Result.success();
    }
}

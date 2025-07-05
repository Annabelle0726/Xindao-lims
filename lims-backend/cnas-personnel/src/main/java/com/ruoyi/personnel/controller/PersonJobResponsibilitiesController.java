package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.personnel.dto.PersonJobResponsibilitiesDto;
import com.ruoyi.personnel.pojo.PersonJobResponsibilities;
import com.ruoyi.personnel.service.PersonJobResponsibilitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 岗位职责 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-09 02:07:49
 */
@Api(tags = "人员 - 岗位职责")
@RestController
@RequestMapping("/personJobResponsibilities")
public class PersonJobResponsibilitiesController {
    @Autowired
    private PersonJobResponsibilitiesService personJobResponsibilitiesService;

    @ApiOperation(value = "新增/更新 岗位职责")
    @PostMapping("/personJobResponsibilitiesSave")
    public Result<?> personJobResponsibilitiesSave(@RequestBody PersonJobResponsibilities personJobResponsibilities) {
        personJobResponsibilitiesService.saveOrUpdate(personJobResponsibilities);
        return Result.success();
    }


    @ApiOperation(value = "删除 岗位职责")
    @DeleteMapping("/personJobResponsibilitiesDelete")
    public Result<?> personJobResponsibilitiesDelete(@RequestParam("id") Integer id) {
        personJobResponsibilitiesService.removeById(id);
        return Result.success();
    }


    @ApiOperation(value = "查询 岗位职责")
    @GetMapping("/personJobResponsibilitiesSelect")
    public Result personJobResponsibilitiesSelect(Page page, String userId, String departmentId, String userName) {
        IPage<PersonJobResponsibilitiesDto> iPage = personJobResponsibilitiesService.personJobResponsibilitiesSelect(page, userId, departmentId, userName);
        return Result.success(iPage);
    }


    @ApiOperation(value = "导出任职岗位说明说")
    @PostMapping("/exportPersonJobResponsibilities")
    public void exportPersonJobResponsibilities(Integer id, HttpServletResponse response){
        personJobResponsibilitiesService.exportPersonJobResponsibilities(id,response);
    }
}

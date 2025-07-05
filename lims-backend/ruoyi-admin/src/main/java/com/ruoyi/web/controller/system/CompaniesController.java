package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.UserService;
import com.ruoyi.web.controller.api.Company;
import com.ruoyi.web.controller.api.PersonDto;
import com.ruoyi.web.controller.api.PersonnelHeaderApi;
import com.ruoyi.web.controller.api.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/companies")
@Api(tags = "用户模块")
public class CompaniesController {

    private PersonnelHeaderApi personnelHeaderApi;

    private UserService userService;

    @PostMapping("/getSampleUser")
    @ApiOperation(value = "查看指定部门的人事系统组织架构")
    public Result<?> getSampleUser() {
        return Result.success();
    }


    @ApiOperation(value = "查看人事系统组织架构")
    @GetMapping(value = "/selectCompaniesList")
    public Result<List<Company>> selectCompaniesList() {
        return Result.success(personnelHeaderApi.companyUrl());

    }

    @ApiOperation("获取人事系统组织下的人员")
    @PostMapping(value = "/selectSimpleList")
    public Result<?> selectSimpleList(String companyId) {
        return Result.success(personnelHeaderApi.userUrl(companyId));
    }

    @ApiOperation("将人事系统勾选的内容转移到本系统")
    @PostMapping(value = "/addPersonUser")
    public Result<?> addPersonUser(@RequestBody PersonDto personDto) {
        return Result.success(personnelHeaderApi.addPersonUser(personDto));
    }

    @ApiOperation("通过员工号获取员工")
    @PostMapping(value = "/selectPersonUser")
    public Result<?> selectPersonUser(String code) {
        Person person = personnelHeaderApi.selectPersonUser(code);
        return Result.success(person);
    }

}

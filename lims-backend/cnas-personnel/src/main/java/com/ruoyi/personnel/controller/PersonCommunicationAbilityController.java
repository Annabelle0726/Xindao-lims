package com.ruoyi.personnel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.personnel.dto.PersonCommunicationAbilityDto;
import com.ruoyi.personnel.pojo.PersonCommunicationAbility;
import com.ruoyi.personnel.service.PersonCommunicationAbilityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 沟通记录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-09 12:00:57
 */
@Api(tags = "人员 - 沟通记录")
@RestController
@RequestMapping("/personCommunicationAbility")
public class PersonCommunicationAbilityController {

    @Autowired
    private PersonCommunicationAbilityService personCommunicationAbilityService;

    @ApiOperation(value = "新增/更新 沟通记录")
    @PostMapping("addOrUpdatePersonCommunicationAbility")
    public Result<?> addOrUpdatePersonCommunicationAbility(@RequestBody PersonCommunicationAbility personCommunicationAbility) {
        personCommunicationAbilityService.saveOrUpdate(personCommunicationAbility);
        return Result.success();
    }

    @ApiOperation(value = "删除 沟通记录")
    @DeleteMapping("deletePersonCommunicationAbility")
    public Result<?> deletePersonCommunicationAbility(@RequestParam("id") Integer id) {
        personCommunicationAbilityService.removeById(id);
        return Result.success();
    }


    @ApiOperation(value = "查询 沟通记录")
    @GetMapping("personPersonCommunicationAbilityPage")
    public Result<IPage<PersonCommunicationAbilityDto>> personPersonCommunicationAbilityPage(Page page,
                                                                                             Integer departLimsId,
                                                                                             Integer userId,
                                                                                             String userName) {
        return Result.success(personCommunicationAbilityService.personPersonCommunicationAbilityPage(page, departLimsId, userId, userName));
    }

    @ApiOperation(value = "导出沟通记录")
    @GetMapping("exportPersonCommunicationAbility")
    public void exportPersonCommunicationAbility(Integer id, HttpServletResponse response) throws Exception{
        personCommunicationAbilityService.exportPersonCommunicationAbility(id,response);
    }
}

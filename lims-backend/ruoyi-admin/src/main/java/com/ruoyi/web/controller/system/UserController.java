package com.ruoyi.web.controller.system;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.system.service.UserService;
import com.ruoyi.web.controller.dto.UpdateUserDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 用户信息表
 *
 * @author zhuo
 * @since 2025-02-13
 */
@RestController
@RequestMapping("/system/newUser")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * todo:  type : 1: 获取检测人员信息
              type : 2: 获取当前登录用户部门下的所有用户
     * @param user
     * @param type
     * @return
     */
    @ApiOperation(value = "根据条件获取用户列表")
    @GetMapping("/selectUserCondition")
    public Result selectUserCondition(User user, String type){
        return Result.success(userService.selectUserCondition(user, type));
    }

    /**
     * 获取当前登录的客户信息
     * @return
     */
    @GetMapping("/getUserNow")
    @ApiOperation(value = "获取当前登录的客户信息")
    public Result<?> getUserNow(){
        return Result.success(userService.getUserNow());
    }

    /**
     * 获取当前登录用户部门
     * @return
     */
    @GetMapping ("/selectUserDepartmentLimsName")
    @ApiOperation(value = "获取当前登录用户部门")
    public Result<?> selectUserDepartmentLimsName(){
        return Result.success(userService.selectUserDepartmentLimsName());
    }

    @PostMapping("/upUserDepardLimsId")
    @ApiOperation(value = "修改人员明细所在组织架构")
    public Result<?> upUserDepardLimsId(@RequestBody UpdateUserDto dto){
        return Result.success(userService.upUserDepardLimsId(dto.getIds(), dto.getId()));
    }

    @DeleteMapping("/delUserDepardLimsId")
    @ApiOperation(value = "删除人员明细所在组织架构")
    public Result<?> delUserDepardLimsId(Integer id){
        return Result.success(userService.delUserDepardLimsId(id));
    }


}


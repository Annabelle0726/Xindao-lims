package com.ruoyi.manage.controller;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.manage.service.ManageReviewProgramFileService;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-09 04:15:47
 */
@Api(tags = "管理评审计划文件")
@RestController
@RequestMapping("/manageReviewProgramFile")
public class ManageReviewProgramFileController {

    @Resource
    private ManageReviewProgramFileService manageReviewProgramFileService;

    @ApiOperation(value = "查询评审计划文件")
    @GetMapping("/selectReviewProgramFile")
    public Result selectReviewProgramFile(Integer id){
        return Result.success(manageReviewProgramFileService.selectFile(id));
    }


    @ApiOperation(value = "新增评审计划文件")
    @PostMapping("/addReviewProgramFile")
    public Result addReviewProgramFile(MultipartFile file,Integer id){
        manageReviewProgramFileService.addFile(file,id);
        return Result.success();
    }
}

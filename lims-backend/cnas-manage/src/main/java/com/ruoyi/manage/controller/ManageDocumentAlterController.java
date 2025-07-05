package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;

import com.deepoove.poi.data.style.*;

import com.ruoyi.manage.pojo.ManageDocumentAlter;
import com.ruoyi.manage.service.ManageDocumentAlterService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件变更 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-11 11:04:01
 */
@Api(tags = "管理体系文件的控制")
@RestController
@RequestMapping("/manageDocumentAlter")
public class ManageDocumentAlterController {

    @Resource
    private ManageDocumentAlterService manageDocumentAlterService;

    @ApiOperation(value = "分页查询文件变更")
    @GetMapping("/pageManageDocumentAlter")
    public Result pageManageDocumentAlter(Page page,ManageDocumentAlter manageDocumentAlter) throws Exception {
        return Result.success(manageDocumentAlterService.pageManageDocumentAlter(page, manageDocumentAlter));
    }

    @ApiOperation(value = "删除文件变更")
    @DeleteMapping("/delManageDocumentAlter")
    public Result delManageDocumentAlter(Integer id){
        return Result.success(manageDocumentAlterService.delManageDocumentAlter(id));
    }


    @ApiOperation(value = "查看文件变更")
    @GetMapping("/getManageDocumentAlter")
    public Result getManageDocumentAlter(Integer id){
        return Result.success(manageDocumentAlterService.getManageDocumentAlter(id));
    }

    @ApiOperation(value = "新增文件变更")
    @PostMapping("/addManageDocumentAlter")
    public Result addManageDocumentAlter(ManageDocumentAlter manageDocumentAlter){
        return Result.success(manageDocumentAlterService.addManageDocumentAlter(manageDocumentAlter));
    }


    @ApiOperation(value = "编辑文件变更")
    @PostMapping("/doManageDocumentAlter")
    public Result doManageDocumentAlter(ManageDocumentAlter manageDocumentAlter){
        return Result.success(manageDocumentAlterService.doManageDocumentAlter(manageDocumentAlter));
    }


    @ApiOperation(value = "审核文件变更")
    @PostMapping("/checkManageDocumentAlter")
    public Result checkManageDocumentAlter(ManageDocumentAlter manageDocumentAlter){
        return Result.success(manageDocumentAlterService.checkManageDocumentAlter(manageDocumentAlter));
    }


    @ApiOperation(value = "审核查看附件")
    @GetMapping("/checkManageDocumentAlterPdf")
    public void checkManageDocumentAlterPdf(Long id, HttpServletResponse response)throws Exception {
        manageDocumentAlterService.checkManageDocumentAlterPdf(id,response);
    }

    @ApiOperation(value = "导出文件变更")
    @GetMapping("/exportManageDocumentAlter")
    public void exportManageDocumentAlter(ManageDocumentAlter manageDocumentAlter,HttpServletResponse response) throws Exception {
        manageDocumentAlterService.exportManageDocumentAlter(manageDocumentAlter,response);
    }

}

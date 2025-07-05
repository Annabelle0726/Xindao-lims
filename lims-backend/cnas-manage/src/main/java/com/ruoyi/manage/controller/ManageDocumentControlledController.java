package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageDocumentControlled;
import com.ruoyi.manage.service.ManageDocumentControlledService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 文件受控 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-08 02:54:44
 */
@Api(tags = "管理体系文件的控制")
@RestController
@RequestMapping("/manageDocumentControlled")
public class ManageDocumentControlledController {

    @Resource
    private ManageDocumentControlledService manageDocumentControlledService;

    @ApiOperation(value = "分页查询文件受控")
    @GetMapping("/pageManageDocumentControlled")
    public Result pageManageDocumentControlled(Page page,ManageDocumentControlled manageDocumentControlled) throws Exception {
        return Result.success(manageDocumentControlledService.pageManageDocumentControlled(page, manageDocumentControlled));
    }

    @ApiOperation(value = "新增文件受控")
    @PostMapping("/addManageDocumentControlled")
    public Result addManageDocumentControlled(ManageDocumentControlled manageDocumentControlled){
        return Result.success(manageDocumentControlledService.addManageDocumentControlled(manageDocumentControlled));
    }


    @ApiOperation(value = "查看文件受控")
    @GetMapping("/getManageDocumentControlled")
    public Result getManageDocumentControlled(Long id){
        return Result.success(manageDocumentControlledService.getManageDocumentControlled(id));
    }


    @ApiOperation(value = "编辑文件受控")
    @PostMapping("/doManageDocumentControlled")
    public Result doManageDocumentControlled(ManageDocumentControlled manageDocumentControlled){
        return Result.success(manageDocumentControlledService.doManageDocumentControlled(manageDocumentControlled));
    }

    @ApiOperation(value = "删除文件受控")
    @DeleteMapping("/delManageDocumentControlled")
    public Result delManageDocumentControlled(Long id){
        return Result.success(manageDocumentControlledService.delManageDocumentControlled(id));
    }


    @ApiOperation(value = "审核文件受控")
    @PostMapping("/checkManageDocumentControlled")
    public Result checkManageDocumentControlled(ManageDocumentControlled manageDocumentControlled){
        return Result.success(manageDocumentControlledService.checkManageDocumentControlled(manageDocumentControlled));
    }


    @ApiOperation(value = "审核查看附件")
    @GetMapping("/checkManageDocumentControlledPdf")
    public void checkManageDocumentControlledPdf(Long id, HttpServletResponse response)throws Exception {
        manageDocumentControlledService.checkManageDocumentControlledPdf(id,response);
    }

}

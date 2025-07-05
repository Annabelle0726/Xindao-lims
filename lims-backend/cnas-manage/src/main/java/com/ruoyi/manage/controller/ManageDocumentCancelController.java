package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageDocumentCancel;
import com.ruoyi.manage.service.ManageDocumentCancelService;
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
 * 文件作废 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-09 02:37:35
 */
@Api(tags = "管理体系文件的控制")
@RestController
@RequestMapping("/manageDocumentCancel")
public class ManageDocumentCancelController {

    @Resource
    private ManageDocumentCancelService manageDocumentCancelService;

    @ApiOperation(value = "分页查询文件作废")
    @GetMapping("/pageManageDocumentCancel")
    public Result pageManageDocumentCancel(Page page,ManageDocumentCancel manageDocumentCancel) throws Exception {
        return Result.success(manageDocumentCancelService.pageManageDocumentCancel(page, manageDocumentCancel));
    }

    @ApiOperation(value = "新增文件作废")
    @PostMapping("/addManageDocumentCancel")
    public Result addManageDocumentCancel(@RequestBody ManageDocumentCancel manageDocumentCancel) {
        return Result.success(manageDocumentCancelService.addManageDocumentCancel(manageDocumentCancel));
    }


    @ApiOperation(value = "审核文件作废")
    @PostMapping("/checkManageDocumentCancel")
    public Result checkManageDocumentCancel(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String state = (String) param.get("state");
        return Result.success(manageDocumentCancelService.checkManageDocumentCancel(id, state));
    }

    @ApiOperation(value = "删除文件作废")
    @DeleteMapping("/delManageDocumentCancel")
    public Result delManageDocumentCancel(Integer id) {
        return Result.success(manageDocumentCancelService.delManageDocumentCancel(id));
    }


    @ApiOperation(value = "查看文件作废")
    @GetMapping("/getManageDocumentCancel")
    public Result getManageDocumentCancel(Integer id) {
        return Result.success(manageDocumentCancelService.getManageDocumentCancel(id));
    }


    @ApiOperation(value = "编辑文件作废")
    @PostMapping("/doManageDocumentCancel")
    public Result doManageDocumentCancel(@RequestBody ManageDocumentCancel manageDocumentCancel) {
        return Result.success(manageDocumentCancelService.doManageDocumentCancel(manageDocumentCancel));
    }

    @ApiOperation(value = "导出文件作废")
    @GetMapping("/exportManageDocumentCancel")
    public void exportManageDocumentCancel(ManageDocumentCancel manageDocumentCancel,HttpServletResponse response) throws Exception {
        manageDocumentCancelService.exportManageDocumentCancel(manageDocumentCancel,response);
    }

}

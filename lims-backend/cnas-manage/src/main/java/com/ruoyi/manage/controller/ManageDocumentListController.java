package com.ruoyi.manage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.excel.ManageDocumentListListener;
import com.ruoyi.manage.pojo.ManageDocumentList;
import com.ruoyi.manage.service.ManageDocumentListService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 文件清单
 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-08 11:08:11
 */
@Api(tags = "管理体系文件的控制")
@RestController
@RequestMapping("/manageDocumentList")
public class ManageDocumentListController {

    @Resource
    private ManageDocumentListService manageDocumentListService;

    @ApiOperation(value = "分页查询文件清单")
    @GetMapping("/pageManageDocumentList")
    public Result pageManageDocumentList(Page page,ManageDocumentList manageDocumentList) throws Exception {
        return Result.success(manageDocumentListService.pageManageDocumentList(page, manageDocumentList));
    }

    @ApiOperation(value = "编辑文件清单")
    @PostMapping("/doManageDocumentList")
    public Result doManageDocumentList(@RequestBody ManageDocumentList manageDocumentList) {
        return Result.success(manageDocumentListService.updateById(manageDocumentList));
    }

    @ApiOperation(value = "删除文件清单")
    @DeleteMapping("/delManageDocumentList")
    public Result delManageDocumentList(Integer id) {
        return Result.success(manageDocumentListService.removeById(id));
    }

    @ApiOperation(value = "上传附件-文件清单")
    @PostMapping("/uploadFileManageDocumentList")
    public Result uploadFileManageDocumentList(Integer id, MultipartFile file) {
        return Result.success(manageDocumentListService.uploadFile(id,file));
    }

    @ApiOperation(value = "导入文件清单列表")
    @PostMapping("/exportManageDocumentList")
    public Result exportManageDocumentList(MultipartFile file) {
        try {
            // 表头校验模板是否正确
            EasyExcel.read(file.getInputStream(), ManageDocumentList.class, new ManageDocumentListListener(manageDocumentListService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

}

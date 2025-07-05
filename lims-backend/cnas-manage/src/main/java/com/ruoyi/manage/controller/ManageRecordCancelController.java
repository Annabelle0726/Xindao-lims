package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.deepoove.poi.data.style.*;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordCancel;
import com.ruoyi.manage.service.ManageRecordCancelService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Map;

/**
 * <p>
 * 作废文件销魂记录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-13 01:27:22
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordCancel")
public class ManageRecordCancelController {

    @Resource
    private ManageRecordCancelService manageRecordCancelService;

    @ApiOperation(value = "分页查询作废文件销毁记录")
    @GetMapping("/pageManageRecordCancel")
    public Result pageManageRecordCancel(Page page,ManageRecordCancel manageRecordCancel) throws Exception {
        return Result.success(manageRecordCancelService.pageManageRecordCancel(page, manageRecordCancel));
    }

    @ApiOperation(value = "新增作废文件销毁记录")
    @PostMapping("/addManageRecordCancel")
    public Result addManageRecordCancel(@RequestBody ManageRecordCancel manageRecordCancel){
        manageRecordCancel.setCreateTime(LocalDate.now());
        return Result.success(manageRecordCancelService.save(manageRecordCancel));
    }


    @ApiOperation(value = "编辑作废文件销毁记录")
    @PostMapping("/doManageRecordCancel")
    public Result doManageRecordCancel(@RequestBody ManageRecordCancel manageRecordCancel){
        return Result.success(manageRecordCancelService.updateById(manageRecordCancel));
    }

    @ApiOperation(value = "删除作废文件销毁记录")
    @DeleteMapping("/delManageRecordCancel")
    public Result delManageRecordCancel(Integer id){
        return Result.success(manageRecordCancelService.removeById(id));
    }

    @ApiOperation(value = "批准作废文件销毁记录")
    @PostMapping("/ratifyManageRecordCancel")
    public Result ratifyManageRecordCancel(@RequestBody Map<String, Object> param){
        Integer id = (Integer) param.get("id");
        String ratifyState = (String) param.get("ratifyState");
        return Result.success(manageRecordCancelService.ratifyManageRecordCancel(id,ratifyState));
    }

    @ApiOperation(value = "导出作废文件销毁记录")
    @GetMapping("/exportOutManageRecordCancel")
    public Result exportOutManageRecordCancel(ManageRecordCancel manageRecordCancel, HttpServletResponse response) throws Exception {
        return Result.success(manageRecordCancelService.exportOutManageRecordCancel(manageRecordCancel,response));
    }

    @ApiOperation(value = "导入作废文件销毁记录")
    @PostMapping("/exportInManageRecordCancel")
    public Result exportInManageRecordCancel(MultipartFile file){
        try {
            return Result.success(manageRecordCancelService.exportInManageRecordCancel(file));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
}

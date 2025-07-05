package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordTotal;
import com.ruoyi.manage.service.ManageRecordTotalService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 外来文件确认记录总历史记录表 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-12 10:30:08
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordTotal")
public class ManageRecordTotalController {

    @Resource
    private ManageRecordTotalService manageRecordTotalService;

    @ApiOperation(value = "查询外来文件确认记录历史列表")
    @GetMapping("/pageManageRecordTotal")
    public Result pageManageRecordTotal(Page page, ManageRecordTotal manageRecordTotal) throws Exception {
        return Result.success(manageRecordTotalService.pageManageRecordTotal(page, manageRecordTotal));
    }

    @ApiOperation(value = "提交外来文件确认记录历史列表")
    @PostMapping("/submitManageRecordTotal")
    public Result submitManageRecordTotal(@RequestBody Map<String, Integer> param) {
        Integer id = param.get("id");
        return Result.success(manageRecordTotalService.submitManageRecordTotal(id));
    }

    @ApiOperation(value = "批准外来文件确认记录历史列表")
    @PostMapping("/ratifyManageRecordTotal")
    public Result ratifyManageRecordTotal(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String ratifyState = (String) param.get("ratifyState");
        return Result.success(manageRecordTotalService.ratifyManageRecordTotal(id, ratifyState));
    }

}

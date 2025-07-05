package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.pojo.ManageRecordIntervalsTotal;
import com.ruoyi.manage.service.ManageRecordIntervalsTotalService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 文件定期审查记录总历史记录表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-15 01:12:11
 */
@Api(tags = "记录的控制")
@RestController
@RequestMapping("/manageRecordIntervalsTotal")
public class ManageRecordIntervalsTotalController {

    @Resource
    private ManageRecordIntervalsTotalService manageRecordIntervalsTotalService;

    @ApiOperation(value = "查询文件定期审查记录历史列表")
    @GetMapping("/pageManageRecordIntervalsTotal")
    public Result pageManageRecordIntervalsTotal(Page page,ManageRecordIntervalsTotal manageRecordIntervalsTotal) throws Exception {
        return Result.success(manageRecordIntervalsTotalService.pageManageRecordIntervalsTotal(page, manageRecordIntervalsTotal));
    }

    @ApiOperation(value = "提交文件定期审查记录历史列表")
    @PostMapping("/submitManageRecordIntervalsTotal")
    public Result submitManageRecordIntervalsTotal(@RequestBody Map<String, Integer> param) {
        Integer id = param.get("id");
        return Result.success(manageRecordIntervalsTotalService.submitManageRecordIntervalsTotal(id));
    }

    @ApiOperation(value = "批准文件定期审查记录历史列表")
    @PostMapping("/ratifyManageRecordIntervalsTotal")
    public Result ratifyManageRecordIntervalsTotal(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String ratifyState = (String) param.get("ratifyState");
        return Result.success(manageRecordIntervalsTotalService.ratifyManageRecordIntervalsTotal(id, ratifyState));
    }

}

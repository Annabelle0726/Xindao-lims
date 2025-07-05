package com.ruoyi.personnel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.personnel.dto.PersonRewardPunishmentRecordDto;
import com.ruoyi.personnel.excel.PersonRewardPunishmentRecordExcel;
import com.ruoyi.personnel.pojo.PersonRewardPunishmentRecord;
import com.ruoyi.personnel.service.PersonRewardPunishmentRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 奖惩记录 前端控制器
 * </p>
 *
 * @author
 * @since 2024-10-08 11:25:02
 */
@Api(tags = "人员 - 奖惩记录")
@RestController
@RequestMapping("/personRewardPunishmentRecord")
public class PersonRewardPunishmentRecordController {

    @Autowired
    private PersonRewardPunishmentRecordService personRewardPunishmentRecordService;

    @ApiOperation(value = "新增/更新 奖惩记录")
    @PostMapping("/addOrUpdateRewardPunishment")
    public Result<?> PersonTrainingSave(@RequestBody PersonRewardPunishmentRecord personRewardPunishmentRecord) {
        personRewardPunishmentRecordService.saveOrUpdate(personRewardPunishmentRecord);
        return Result.success();
    }

    @ApiOperation(value = "删除奖惩记录")
    @DeleteMapping("/deleteRewardPunishment")
    public Result<?> deleteRewardPunishment(@RequestParam("id") Integer id) {
        personRewardPunishmentRecordService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "查询 奖惩记录")
    @GetMapping("/rewardPunishmentPage")
    @SneakyThrows
    public Result<IPage<PersonRewardPunishmentRecordDto>> rewardPunishmentPage(Page page,
                                                                               Integer userId,
                                                                               Integer departmentId,
                                                                               String userName,
                                                                               @RequestParam(value = "startTime",required = false) String startTimeStr,
                                                                               @RequestParam(value = "endTime",required = false) String endTimeStr) {
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotEmpty(startTimeStr) || StringUtils.isNotEmpty(endTimeStr)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            startTime = formatter.parse(startTimeStr);
            endTime = formatter.parse(endTimeStr);
        }
        return Result.success(personRewardPunishmentRecordService.rewardPunishmentPage(page, userId, userName, startTime, endTime, departmentId));
    }

    @ApiOperation(value = "奖惩记录导出")
    @GetMapping("/rewardPunishmentExport")
    public void rewardPunishmentExport(Integer userId,
                                       Integer departmentId,
                                       String userName,
                                       @RequestParam(value = "startTime",required = false) String startTimeStr,
                                       @RequestParam(value = "endTime",required = false) String endTimeStr,
                                       HttpServletResponse response) throws Exception {
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotEmpty(startTimeStr) || StringUtils.isNotEmpty(endTimeStr)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            startTime = formatter.parse(startTimeStr);
            endTime = formatter.parse(endTimeStr);
        }
        List<PersonRewardPunishmentRecordExcel> data = personRewardPunishmentRecordService.rewardPunishmentExport(userId, departmentId, userName, startTime, endTime);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("requestType", "excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        // 设置单元格样式
        // 保存到第一个sheet中
        EasyExcel.write(response.getOutputStream())
                .head(PersonRewardPunishmentRecordExcel.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                .sheet()
                .doWrite(data);
    }
}

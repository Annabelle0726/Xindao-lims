package com.ruoyi.performance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.performance.dto.AuxiliaryWorkingHoursDayDto;
import com.ruoyi.performance.dto.HoursDay;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;
import com.ruoyi.performance.service.AuxiliaryWorkingHoursDayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 日工时管理的辅助工时 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 02:22:19
 */
@Api(tags = "日工时管理-辅助工时")
@AllArgsConstructor
@RestController
@RequestMapping("/auxiliaryWorkingHoursDay")
public class AuxiliaryWorkingHoursDayController {

    @Resource
    private AuxiliaryWorkingHoursDayService auxiliaryWorkingHoursDayService;

    @ApiOperation(value = "查询工时统计的辅助工时")
    @GetMapping("/selectAuxiliaryWorkingHoursDay")
    public Result selectAuxiliaryWorkingHoursDay(Page page,AuxiliaryWorkingHoursDayDto entity) throws Exception {
        return Result.success(auxiliaryWorkingHoursDayService.selectAuxiliaryWorkingHoursDay(page, entity));
    }

    @ApiOperation(value = "根据编号查询辅助工时配置信息")
    @GetMapping("/selectAuxiliaryWorkingHoursByNumber")
    public Result selectAuxiliaryWorkingHoursByNumber(String number) {
        return Result.success(auxiliaryWorkingHoursDayService.selectAuxiliaryWorkingHoursByNumber(number));
    }

    @ApiOperation(value = "根据编号当前用户信息查询所在班次")
    @GetMapping("/selectshiftByUser")
    public Result selectshiftByUser(LocalDateTime dateTime) {
        return Result.success(auxiliaryWorkingHoursDayService.selectshiftByUser(dateTime));
    }

    @ApiOperation(value = "录入数据(工时统计的辅助工时)")
    @PostMapping("/insertAuxiliaryWorkingHoursDay")
    public Result insertAuxiliaryWorkingHoursDay(@RequestBody AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay) {
        return Result.success(auxiliaryWorkingHoursDayService.insertAuxiliaryWorkingHoursDay(auxiliaryWorkingHoursDay));
    }

    @ApiOperation(value = "批准工时统计的辅助工时")
    @PostMapping("/approve")
    public Result approve(@RequestBody HoursDay hoursDay ) {
        return Result.success(auxiliaryWorkingHoursDayService.checkOrApprove(hoursDay));
    }

    @ApiOperation(value = "审核工时统计的辅助工时")
    @PostMapping("/check")
    public Result check(@RequestBody HoursDay hoursDay ) {
        return Result.success(auxiliaryWorkingHoursDayService.checkOrApprove(hoursDay));
    }

    @ApiOperation(value = "编辑工时统计的辅助工时")
    @PostMapping("/updateAuxiliaryWorkingHoursDay")
    public Result updateAuxiliaryWorkingHoursDay(@RequestBody AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay) {
        return Result.success(auxiliaryWorkingHoursDayService.updateAuxiliaryWorkingHoursDay(auxiliaryWorkingHoursDay));
    }

    @ApiOperation(value = "删除工时统计的辅助工时")
    @DeleteMapping("/deleteAuxiliaryWorkingHoursDay")
    public Result deleteAuxiliaryWorkingHoursDay(Integer id) {
        return Result.success(auxiliaryWorkingHoursDayService.deleteAuxiliaryWorkingHoursDay(id));
    }

    /**
     * 导出辅助工时
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "导出辅助工时")
    @GetMapping("/exportAssistantHours")
    public void exportAssistantHours(AuxiliaryWorkingHoursDayDto entity, HttpServletResponse response) throws Exception {
        auxiliaryWorkingHoursDayService.exportWorkingHours(entity, response);
    }

}

package com.ruoyi.require.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.excel.FeCalibrationScheduleExport;
import com.ruoyi.require.pojo.FeCalibrationSchedule;
import com.ruoyi.require.service.FeCalibrationScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 
 * @since 2024-11-13 02:53:05
 */
@Api(tags = "量值溯源计划")
@RestController
@RequestMapping("/feCalibrationSchedule")
public class FeCalibrationScheduleController {

    @Resource
    private FeCalibrationScheduleService feCalibrationScheduleService;

    @ApiOperation(value = "量值溯源计划查询")
    @GetMapping("/getPageCalibrationSchedule")
    public Result<IPage<FeCalibrationSchedule>> getPageCalibrationSchedule(Page page, String instrumentName, String managementNumber) {
        IPage<FeCalibrationSchedule> ipage = feCalibrationScheduleService.page(page,instrumentName, managementNumber);
        return Result.success(ipage);
    }

    @ApiOperation(value = "量值溯源计划新增编辑")
    @PostMapping("/addCalibrationSchedule")
    public Result addCalibrationSchedule(@RequestBody FeCalibrationSchedule feCalibrationSchedule) {
        return Result.success(feCalibrationScheduleService.saveOrUpdate(feCalibrationSchedule));
    }

    @ApiOperation(value = "量值溯源计划删除")
    @GetMapping("/removeCalibrationSchedule")
    public Result removeCalibrationSchedule(Integer id) {
        return Result.success(feCalibrationScheduleService.removeById(id));
    }


    @ApiOperation(value = "量值溯源计划导出")
    @PostMapping("exportOfValueTraceabilityPlan")
    public void exportOfValueTraceabilityPlan(String instrumentName, String managementNumber,
                                       HttpServletResponse response) throws Exception {
        IPage<FeCalibrationSchedule> data = feCalibrationScheduleService.page(new Page<>(1, -1),instrumentName, managementNumber);
        List<FeCalibrationScheduleExport> studentList  = JSONObject.parseArray(JSON.toJSONString(data.getRecords()), FeCalibrationScheduleExport.class);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("requestType", "excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        // 设置单元格样式
        // 保存到第一个sheet中
        EasyExcel.write(response.getOutputStream())
                .head(FeCalibrationScheduleExport.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                .sheet("sheet")
                .doWrite(studentList);
    }

    @ApiOperation(value = "importOfValueTraceabilityPlan")
    @PostMapping("/importOfValueTraceabilityPlan")
    public void importOfValueTraceabilityPlan(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return;
        }
        EasyExcel.read(file.getInputStream(), FeCalibrationScheduleExport.class, new PageReadListener<FeCalibrationScheduleExport>(dataList -> {
            List<FeCalibrationSchedule> studentList  = JSONObject.parseArray(JSON.toJSONString(dataList), FeCalibrationSchedule.class);
            feCalibrationScheduleService.saveOrUpdateBatch(studentList);
        })).sheet().doRead();
    }


//    Equipment quantity traceability management
    @ApiOperation(value = "量值溯源计划导出word")
    @GetMapping("/exportWordOfValueTraceabilityPlan")
    public void exportWordOfValueTraceabilityPlan(HttpServletResponse response) throws IOException {
        feCalibrationScheduleService.exportWordOfValueTraceabilityPlan(response);
    }
}

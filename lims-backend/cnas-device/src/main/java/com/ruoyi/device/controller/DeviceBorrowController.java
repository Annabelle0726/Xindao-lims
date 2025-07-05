package com.ruoyi.device.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.pojo.DeviceBorrow;
import com.ruoyi.device.service.DeviceBorrowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 10:53:51
 */
@RestController
@RequestMapping("/deviceBorrow")
public class DeviceBorrowController {

    @Resource
    private DeviceBorrowService deviceBorrowService;


    //分页
    @GetMapping("/deviceBorrowPage")
    public Result deviceBorrowPage(Page page, DeviceBorrow deviceBorrow) throws Exception {
        return Result.success(deviceBorrowService.deviceBorrowPage(page, deviceBorrow));
    }

    //查询
    @GetMapping("/getDeviceBorrow")
    public Result getDeviceBorrow(Integer id) {
        return Result.success(deviceBorrowService.getDeviceBorrow(id));
    }

    //新增
    @PostMapping("/saveDeviceBorrow")
    public Result saveDeviceBorrow(@RequestBody DeviceBorrow deviceBorrow) {
        return Result.success(deviceBorrowService.saveDeviceBorrow(deviceBorrow));
    }

    //删除
    @DeleteMapping("/deleteDeviceBorrow")
    public Result deleteDeviceBorrow(Integer id) {
        return Result.success(deviceBorrowService.removeById(id));
    }

    //导出
    @GetMapping("/deviceBorrowExport")
    public Result deviceBorrowExport(@RequestParam("deviceId") Integer deviceId, HttpServletResponse response) throws Exception {
        List<DeviceBorrow> deviceBorrows = deviceBorrowService.getDeviceBorrowBydeviceId(deviceId);
        response.setHeader("requestType", "excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        // 设置单元格样式
        // 保存到第一个sheet中
        EasyExcel.write(response.getOutputStream())
                .head(DeviceBorrow.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                .sheet()
                .doWrite(deviceBorrows);
        return Result.success();
    }


}

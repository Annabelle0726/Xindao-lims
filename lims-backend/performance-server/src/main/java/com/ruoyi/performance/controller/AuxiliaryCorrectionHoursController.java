package com.ruoyi.performance.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.performance.dto.AuxiliaryCorrectionHoursDto;
import com.ruoyi.performance.excel.AuxiliaryCorrectionHoursListener;
import com.ruoyi.performance.service.AuxiliaryCorrectionHoursService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 工时统计的修正工时 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-29 02:38:19
 */
@Api(tags = "工时统计-修正工时")
@AllArgsConstructor
@RestController
@RequestMapping("/auxiliaryCorrectionHours")
public class AuxiliaryCorrectionHoursController {

    @Resource
    AuxiliaryCorrectionHoursService auxiliaryCorrectionHoursService;

    @ApiOperation(value = "查询修正工时")
    @GetMapping("/selectAuxiliaryCorrectionHours")
    public Result selectAuxiliaryCorrectionHours(Page page,AuxiliaryCorrectionHoursDto entity) throws Exception {
        return Result.success(auxiliaryCorrectionHoursService.selectAuxiliaryCorrectionHours(page, entity));
    }

    /**
     * excel上传
     *
     * @return
     */
    @ApiOperation(value = "导入修正工时")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), AuxiliaryCorrectionHoursDto.class, new AuxiliaryCorrectionHoursListener(auxiliaryCorrectionHoursService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

}

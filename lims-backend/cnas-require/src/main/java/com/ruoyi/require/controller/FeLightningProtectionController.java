package com.ruoyi.require.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.FileSaveUtil;
import com.ruoyi.require.excel.FeLightningProtectionExcel;
import com.ruoyi.require.pojo.FeLightningProtection;
import com.ruoyi.require.service.FeLightningProtectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-防雷检测 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:16:36
 */
@Api(tags = "设施和环境条件要求-防雷检测")
@RestController
@RequestMapping("/feLightningProtection")
public class FeLightningProtectionController {

    @Autowired
    private FeLightningProtectionService feLightningProtectionService;

    @PostMapping("addLightningProtectionDetection")
    @ApiOperation("设施和环境条件要求-防雷检测新增/修改")
    public Result<?> addLightningProtectionDetection(FeLightningProtection feLightningProtection,
                                                     @RequestPart(value = "file", required = false) MultipartFile file) {
        if (ObjectUtils.isNotEmpty(file)) {
            String s = FileSaveUtil.uploadWordFile(file);
            feLightningProtection.setSystemFileName(s);
            feLightningProtection.setFileName(file.getOriginalFilename());
        }
        feLightningProtectionService.saveOrUpdate(feLightningProtection);
        return Result.success();
    }

    @DeleteMapping("deleteLightningProtectionDetection")
    @ApiOperation("设施和环境条件要求-防雷检测删除")
    public Result<?> deleteFeLightningProtection(@RequestParam("lightningProtectionId") Integer lightningProtectionId) {
        feLightningProtectionService.removeById(lightningProtectionId);
        return Result.success();
    }

    @GetMapping("getLightningProtectionDetection")
    @ApiOperation("设施和环境条件要求-防雷检测查询")
    public Result<IPage<FeLightningProtection>> getFeLightningProtection(Page page) {
        IPage<FeLightningProtection> page1 = feLightningProtectionService.page(page);
        return Result.success(page1);
    }

    @ApiOperation(value = "设施和环境条件要求-防雷检测导出")
    @GetMapping("exportOfLightningProtectionDetection")
    public void exportOfLightningProtectionDetection(HttpServletResponse response) throws Exception {
        List<FeLightningProtectionExcel> ipage = feLightningProtectionService.exportOfLightningProtectionDetection();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("requestType", "excel");
        response.setHeader("Access-Control-Expose-Headers", "requestType");
        // 设置单元格样式
        // 保存到第一个sheet中
        EasyExcel.write(response.getOutputStream())
                .head(FeLightningProtectionExcel.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自适应列宽
                .sheet("sheet")
                .doWrite(ipage);
    }
}

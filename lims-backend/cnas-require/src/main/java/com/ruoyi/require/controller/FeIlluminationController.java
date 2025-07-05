package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.dto.FeIlluminationAddDto;
import com.ruoyi.require.dto.FeIlluminationDto;
import com.ruoyi.require.pojo.FeIllumination;
import com.ruoyi.require.pojo.FeIlluminationDetectionArea;
import com.ruoyi.require.service.FeIlluminationDetectionAreaService;
import com.ruoyi.require.service.FeIlluminationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-照度记录表 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:15:57
 */
@RestController
@RequestMapping("/feIllumination")
public class FeIlluminationController {

    @Autowired
    private FeIlluminationService feIlluminationService;

    @Autowired
    private FeIlluminationDetectionAreaService feIlluminationDetectionAreaService;

    @PostMapping("addFeLightningProtection")
    @ApiOperation("设施和环境条件要求-照度记录表-检测区域 新增/修改")
    public Result<?> addFeLightningProtection(@RequestBody FeIlluminationAddDto feIlluminationAddDto) {
        FeIllumination feIllumination = new FeIllumination();
        BeanUtils.copyProperties(feIlluminationAddDto, feIllumination);
        feIlluminationService.saveOrUpdate(feIllumination);
        if (CollectionUtils.isNotEmpty(feIlluminationAddDto.getIlluminationDetectionAreaList())) {
            feIlluminationAddDto.getIlluminationDetectionAreaList().forEach(i -> i.setIntensityIlluminationId(feIllumination.getIntensityIlluminationId()));
            feIlluminationDetectionAreaService.saveOrUpdateBatch(feIlluminationAddDto.getIlluminationDetectionAreaList());
        }
        return Result.success();
    }

    @DeleteMapping("deleteFeLightningProtection")
    @ApiOperation("设施和环境条件要求-照度记录表-检测区域 删除")
    public Result<?> deleteFeLightningProtection(@RequestParam("intensityIlluminationId") Integer intensityIlluminationId) {
        feIlluminationService.removeById(intensityIlluminationId);
        return Result.success();
    }

    @GetMapping("getFeLightningProtection")
    @ApiOperation("照度记录表-检测区域 查询")
    public Result<IPage<FeIlluminationDto>> getFeLightningProtection(Page page) {
        IPage<FeIlluminationDto> page1 = feIlluminationService.getFeLightningProtection(page);
        return Result.success(page1);
    }

    @DeleteMapping("deleteFeIlluminationDetectionArea")
    @ApiOperation("照度记录表-检测区域 删除")
    public Result<?> deleteFeIlluminationDetectionArea(@RequestParam("detectionAreaId") Integer detectionAreaId) {
        return Result.success(feIlluminationDetectionAreaService.removeById(detectionAreaId));
    }

    @GetMapping("getFeIlluminationDetectionArea")
    @ApiOperation("照度记录表-检测区域 根据照度记录查询")
    public Result<?> getFeIlluminationDetectionArea(@RequestParam("intensityIlluminationId") Integer intensityIlluminationId) {
        return Result.success(feIlluminationDetectionAreaService.list(Wrappers.<FeIlluminationDetectionArea>lambdaQuery()
                .eq(FeIlluminationDetectionArea::getIntensityIlluminationId, intensityIlluminationId)));
    }

    /**
     * 导出照度记录
     * @return
     */
    @ApiOperation(value = "导出照度记录")
    @GetMapping("/exportFeIllumination")
    public void exportFeIllumination(Integer intensityIlluminationId, HttpServletResponse response){
        feIlluminationService.exportFeIllumination(intensityIlluminationId, response);
    }
}

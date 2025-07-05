package com.ruoyi.inspect.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.inspect.dto.InsProductDeviationWarningDto;
import com.ruoyi.inspect.dto.SampleOrderDto;
import com.ruoyi.inspect.pojo.InsProductDeviationWarningDetail;
import com.ruoyi.inspect.service.InsProductDeviationWarningDetailService;
import com.ruoyi.inspect.service.InsProductDeviationWarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 检验项偏差预警主表 前端控制器
 * </p>
 *
 * @author
 * @since 2025-03-28 02:18:02
 */
@RestController
@RequestMapping("/insProductDeviationWarning")
@AllArgsConstructor
@Api(tags="检验项偏差预警")
public class InsProductDeviationWarningController {


    private InsProductDeviationWarningService insProductDeviationWarningService;
    private InsProductDeviationWarningDetailService insProductDeviationWarningDetailService;

    /**
     * 查询预警列表
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询预警列表")
    @GetMapping("/selectDeviationWarningPage")
    public Result<IPage<InsProductDeviationWarningDto>> selectDeviationWarningPage(Page page, InsProductDeviationWarningDto deviationWarningDto) {
        return Result.success(insProductDeviationWarningService.selectDeviationWarningPage(page, deviationWarningDto));
    }

    /**
     * 查询预警详情
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询预警详情")
    @GetMapping("/selectDeviationWarning")
    public Result<List<InsProductDeviationWarningDetail>> selectDeviationWarning(Integer deviationWarningId){
        return Result.success(insProductDeviationWarningDetailService.list(Wrappers.<InsProductDeviationWarningDetail>lambdaQuery()
                .eq(InsProductDeviationWarningDetail::getDeviationWarningId, deviationWarningId)));
    }

}

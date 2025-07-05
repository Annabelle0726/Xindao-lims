package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.dto.InconsistentDistributionDto;
import com.ruoyi.process.pojo.InconsistentDistribution;
import com.ruoyi.process.service.InconsistentDistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 不符合项的分布 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-15 09:53:20
 */
@Api(tags = "不符合项的分布")
@AllArgsConstructor
@RestController
@RequestMapping("/inconsistentDistribution")
public class InconsistentDistributionController {

    private InconsistentDistributionService inconsistentDistributionService;

    /**
     * 不符合项的分布分页查询
     * @param
     * @return
     */
    @ApiOperation(value = "不符合项的分布分页查询")
    @GetMapping("/pageInconsistentDistribution")
    public Result<IPage<InconsistentDistributionDto>> pageInconsistentDistribution(Page page, InconsistentDistribution inconsistentDistribution) throws Exception {
        return Result.success(inconsistentDistributionService.pageInconsistentDistribution(page, inconsistentDistribution));
    }

    /**
     * 不符合项的分布新增
     * @return
     */
    @ApiOperation(value = "不符合项的分布新增")
    @PostMapping("/addInconsistentDistribution")
    public Result addInconsistentDistribution(@RequestBody InconsistentDistributionDto inconsistentDistribution){
        return Result.success(inconsistentDistributionService.addInconsistentDistribution(inconsistentDistribution));
    }

    /**
     * 不符合项的分布修改
     * @return
     */
    @ApiOperation(value = "不符合项的分布修改")
    @PostMapping("/updateInconsistentDistribution")
    public Result updateInconsistentDistribution(@RequestBody InconsistentDistributionDto inconsistentDistribution){
        return Result.success(inconsistentDistributionService.updateInconsistentDistribution(inconsistentDistribution));
    }

    /**
     * 不符合项的分布删除
     * @return
     */
    @ApiOperation(value = "不符合项的分布删除")
    @DeleteMapping("/delInconsistentDistribution")
    public Result delInconsistentDistribution(Integer distributionId){
        return Result.success(inconsistentDistributionService.delInconsistentDistribution(distributionId));
    }

    /**
     * 不符合项的分布查看详情
     * @return
     */
    @ApiOperation(value = "不符合项的分布查看详情")
    @GetMapping("/getInconsistentDistributionOne")
    public Result<InconsistentDistributionDto> getInconsistentDistributionOne(Integer distributionId){
        return Result.success(inconsistentDistributionService.getInconsistentDistributionOne(distributionId));
    }

    /**
     * 导出不符合项的分布
     * @return
     */
    @ApiOperation(value = "导出不符合项的分布")
    @GetMapping("/exportInconsistentDistribution")
    public void exportInconsistentDistribution(Integer distributionId, HttpServletResponse response){
        inconsistentDistributionService.exportInconsistentDistribution(distributionId, response);
    }

}

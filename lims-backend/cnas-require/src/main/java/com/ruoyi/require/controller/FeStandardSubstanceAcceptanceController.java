package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.dto.AcceptanceDto;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptanceInspection;
import com.ruoyi.require.service.FeStandardSubstanceAcceptanceInspectionService;
import com.ruoyi.require.service.FeStandardSubstanceAcceptanceService;
import com.ruoyi.require.vo.AcceptanceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 标准物质验收 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-11-14 03:29:41
 */
@Api(tags = "标准物质验收")
@RestController
@RequestMapping("/feStandardSubstanceAcceptance")
public class FeStandardSubstanceAcceptanceController {

    @Autowired
    private FeStandardSubstanceAcceptanceInspectionService inspectionService;
    @Resource
    private FeStandardSubstanceAcceptanceService feStandardSubstanceAcceptanceService;

    @ApiOperation(value = "新增")
    @PostMapping("/addAcceptance")
    @Transactional
    public Result addAcceptance(@RequestBody AcceptanceDto dto) {
        feStandardSubstanceAcceptanceService.addAcceptance(dto);
        return Result.success();
    }

    @ApiOperation(value = "标准物质验收查询")
    @GetMapping("/getPageAcceptance")
    public Result<IPage<AcceptanceVo>> getPageAcceptance(Page page, String name) {
        IPage<AcceptanceVo> ipage = feStandardSubstanceAcceptanceService.getPageAcceptance(page, name);
        return Result.success(ipage);
    }

    @ApiOperation(value = "标准物质验收删除")
    @GetMapping("/deleteAcceptance/{id}")
    public Result deleteAcceptance(@PathVariable("id") Integer id) {
        return Result.success(feStandardSubstanceAcceptanceService.deleteAcceptance(id));
    }

    @ApiOperation(value = "编辑")
    @PostMapping("/updateAcceptance")
    public Result updateAcceptance(@RequestBody AcceptanceDto acceptanceDto) {
        feStandardSubstanceAcceptanceService.updateById(acceptanceDto.getAcceptance());
        for (FeStandardSubstanceAcceptanceInspection v : acceptanceDto.getList()) {
            if (v.getId()== null) {
                inspectionService.save(v);
            }else {
                inspectionService.updateById(v);
            }
        }
        return Result.success();
    }


    @ApiOperation(value = "标准物质验收查询")
    @GetMapping("/getAcceptanceDetails")
    public Result getAcceptanceDetails(Integer id) {
        return Result.success(feStandardSubstanceAcceptanceService.getAcceptanceDetails(id));
    }

    @ApiOperation("导出标准物质验收")
    @GetMapping("/exportFeStandardSubstanceAcceptance")
    public Result exportFeStandardSubstanceAcceptance(HttpServletResponse response) {
        feStandardSubstanceAcceptanceService.exportFeStandardSubstanceAcceptance(response);
        return Result.success();
    }


}

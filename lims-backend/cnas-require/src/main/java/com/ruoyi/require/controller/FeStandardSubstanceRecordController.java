package com.ruoyi.require.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.require.pojo.FeStandardSubstanceRecord;
import com.ruoyi.require.service.FeStandardSubstanceRecordService;
import com.ruoyi.require.vo.SubstanceRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 标准物质清单借用归还记录表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-14 01:49:11
 */
@Api(tags = "标准物质清单借用归还记录")
@RestController
@RequestMapping("/feStandardSubstanceRecord")
public class FeStandardSubstanceRecordController {

    @Resource
    private FeStandardSubstanceRecordService feStandardSubstanceRecordService;

    @ApiOperation(value = "标准物质清单借用")
    @PostMapping("/borrowSubstance")
    @Transactional
    public Result borrowSubstance(@RequestBody FeStandardSubstanceRecord record) {
        feStandardSubstanceRecordService.borrowSubstance(record);
        return Result.success();
    }

    @ApiOperation(value = "标准物质清单归还")
    @PostMapping("/returnSubstance")
    @Transactional
    public Result returnSubstance(@RequestBody FeStandardSubstanceRecord record) {
        feStandardSubstanceRecordService.returnSubstance(record);
        return Result.success();
    }

    @ApiOperation(value = "标准物质清单领用查询")
    @PostMapping("/getSubstanceRecord")
    public Result getSubstanceRecord(Integer id) {
        return Result.success(feStandardSubstanceRecordService.getSubstanceRecord(id));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/getPageSubstanceRecord")
    public Result<IPage<SubstanceRecordVo>> getPageSubstanceRecord(Page page, SubstanceRecordVo vo) {
        IPage<SubstanceRecordVo> ipage = feStandardSubstanceRecordService.getPage(page,vo);
        return Result.success(ipage);
    }
}

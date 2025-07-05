package com.ruoyi.performance.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHours;
import com.ruoyi.performance.service.AuxiliaryWorkingHoursService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 辅助工时 前端控制器
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-09 06:58:31
 */
@Api(tags = "绩效管理-辅助工时设置")
@AllArgsConstructor
@RestController
@RequestMapping("/auxiliaryWorkingHours")
public class AuxiliaryWorkingHoursController {
    @Resource
    private AuxiliaryWorkingHoursService auxiliaryWorkingHoursService;

    @ApiOperation(value="查询辅助工时")
    @GetMapping("/selectAuxiliaryWorkingHours")
    public Result selectAuxiliaryWorkingHours(Page page,AuxiliaryWorkingHours entity) throws Exception {
        return Result.success(auxiliaryWorkingHoursService.selectAuxiliaryWorkingHours(page,entity));
    }

    @ApiOperation(value="删除辅助工时")
    @DeleteMapping("/deleteAuxiliaryWorkingHours")
    public Result deleteAuxiliaryWorkingHours(Integer id){

        return Result.success(auxiliaryWorkingHoursService.deleteAuxiliaryWorkingHours(id));

    }

    @ApiOperation(value="修改辅助工时")
    @PostMapping("/upAuxiliaryWorkingHours")
    public Result upAuxiliaryWorkingHours(@RequestBody AuxiliaryWorkingHours auxiliaryWorkingHours){
        return Result.success(auxiliaryWorkingHoursService.upAuxiliaryWorkingHours(auxiliaryWorkingHours));
    }

    @ApiOperation(value="新增辅助工时")
    @PostMapping("/insertAuxiliaryWorkingHours")
    public  Result insertAuxiliaryWorkingHours(@RequestBody AuxiliaryWorkingHours auxiliaryWorkingHours){
        return Result.success(auxiliaryWorkingHoursService.insertAuxiliaryWorkingHours(auxiliaryWorkingHours));
    }

}

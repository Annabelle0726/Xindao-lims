package com.ruoyi.basic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.basic.pojo.Seal;
import com.ruoyi.basic.service.SealService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 印章管理(SealController)表控制层
 */
@Api(tags = "印章管理")

@RestController
@RequestMapping("/sealScope")
public class SealController {

    @Resource
    private SealService sealService;

    @ApiOperation(value = "添加印章参数")
    @PostMapping("/addSeal")
    public Result addSeal(@RequestBody Seal seal) {
        int i = sealService.addSeal(seal);
        if(i>0){
            Integer labId = seal.getLabId();
            List<Laboratory> laboratory = sealService.Laboratory(labId);
            return Result.success(laboratory);
        }else{
            return Result.fail();
        }

    }
    @ApiOperation(value="查询印章列表")
    @GetMapping("/selectSeal")
    public  Result selectSeal(Page page,Seal seal) {
        return Result.success(sealService.selectSeal(page,seal));
    }
}

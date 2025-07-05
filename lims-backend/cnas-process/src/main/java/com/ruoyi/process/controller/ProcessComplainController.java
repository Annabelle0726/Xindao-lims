package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.process.pojo.ProcessComplain;
import com.ruoyi.process.service.ProcessComplainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 投诉 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-02 09:29:11
 */
@RestController
@RequestMapping("/processComplain")
@Api(tags = "投诉")
public class ProcessComplainController {

    @Resource
    private ProcessComplainService processComplainService;

    @ApiOperation(value = "投诉情况汇总表")
    @GetMapping("/pageProcessComplain")
    public Result pageProcessComplain( ProcessComplain processComplain,Page page) throws Exception {
        return Result.success(processComplainService.pageProcessComplain(page, processComplain));
    }

    @ApiOperation(value = "新增投诉")
    @PostMapping("/addProcessComplain")
    public Result addProcessComplain(@RequestBody ProcessComplain processComplain ) {
        return Result.success(processComplainService.addProcessComplain(processComplain));
    }

    @ApiOperation(value = "删除投诉")
    @DeleteMapping("/delProcessComplain")
    public Result delProcessComplain(Long id) {
        return Result.success(processComplainService.removeById(id));
    }

    @ApiOperation(value = "投诉详情")
    @GetMapping("/getProcessComplain")
    public Result getProcessComplain(Long id) {
        return Result.success(processComplainService.getProcessComplain(id));
    }

    @ApiOperation(value = "处理投诉")
    @PostMapping("/doProcessComplain")
    public Result doProcessComplain(@RequestBody ProcessComplain processComplain) {
        return Result.success(processComplainService.doProcessComplain(processComplain));
    }

    @ApiOperation(value = "导出列表")
    @GetMapping("/exportProcessComplain")
    public void exportProcessComplain(ProcessComplain processComplain,HttpServletResponse response) throws Exception {
        processComplainService.exportProcessComplain(processComplain,response);
    }
}

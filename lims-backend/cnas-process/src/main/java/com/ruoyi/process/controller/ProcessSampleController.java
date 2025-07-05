package com.ruoyi.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.process.pojo.ProcessSample;
import com.ruoyi.process.service.ProcessSampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 样品接收 前端控制器
 * </p>
 *
 * @author
 * @since 2024-12-12 05:02:49
 */
@RestController
@RequestMapping("/processSample")
@Api(tags = "样品接收")
public class ProcessSampleController {

    @Resource
    private ProcessSampleService processSampleService;

    @ApiOperation(value = "查询样品接收详情")
    @GetMapping("/pageProcessSample")
    public Result pageProcessSample(ProcessSample processSample, Page page){
        return Result.success(processSampleService.pageProcessSample(page, processSample));
    }

    @ApiOperation(value = "新增样品接收详情")
    @PostMapping("/addProcessSample")
    public Result addProcessSample(@RequestBody ProcessSample processSample) {
        return Result.success(processSampleService.saveOrUpdate(processSample));
    }

    @ApiOperation(value = "删除样品接收")
    @DeleteMapping("/delProcessSample")
    public Result delProcessSample(Integer id) {
        return Result.success(processSampleService.removeById(id));
    }


    @ApiOperation(value = "导出样品接收")
    @GetMapping("/exportProcessSample")
    public void exportProcessSample(ProcessSample processSample, HttpServletResponse response) {
        processSampleService.exportProcessSample(processSample,response);
    }


}

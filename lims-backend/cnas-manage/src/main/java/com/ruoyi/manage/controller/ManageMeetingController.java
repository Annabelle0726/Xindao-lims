package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.manage.dto.ManageMeetingDto;
import com.ruoyi.manage.service.ManageMeetingService;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-11 09:33:47
 */
@Api(tags = "管理评审会议")
@RestController
@RequestMapping("/manageMeeting")
public class ManageMeetingController {

    @Resource
    private ManageMeetingService manageMeetingService;

    @ApiOperation(value = "管理评审会议记录查询")
    @GetMapping("/getPageMeeting")
    public Result<IPage<ManageMeetingDto>> getPageMeeting(Page page, String startTime, String endTime, String place) throws Exception {
        IPage<ManageMeetingDto> ipage = manageMeetingService.page(page,startTime,endTime,place);
        return Result.success(ipage);
    }

    @ApiOperation(value = "新增会议记录")
    @PostMapping("/addMeeting")
    public Result addMeeting(@RequestBody ManageMeetingDto dto){
        manageMeetingService.addMeeting(dto);
        return Result.success();
    }

    @ApiOperation(value = "编辑会议记录")
    @PostMapping("/modifyMeeting")
    public Result modifyMeeting(@RequestBody ManageMeetingDto manageMeetingDto){
        return Result.success(manageMeetingService.modifyMeeting(manageMeetingDto));
    }

    @ApiOperation(value = "删除会议记录")
    @DeleteMapping("/deleteMeeting")
    public Result deleteMeeting(Integer id){
        return Result.success(manageMeetingService.removeById(id));
    }


    @ApiOperation(value = "下载会议记录")
    @GetMapping("/exportMeeting")
    public void exportMeeting(Integer id, HttpServletResponse response){
        manageMeetingService.exportMeeting(id,response);
    }

}

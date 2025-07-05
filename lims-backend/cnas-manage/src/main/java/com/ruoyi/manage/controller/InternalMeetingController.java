package com.ruoyi.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.style.*;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.JackSonUtil;
import com.ruoyi.manage.dto.InternalMeetingDto;
import com.ruoyi.manage.pojo.InternalMeeting;
import com.ruoyi.manage.service.InternalMeetingService;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 内审会议表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-12 02:50:44
 */
@Api(tags = "内审会议")
@AllArgsConstructor
@RestController
@RequestMapping("/internalMeeting")
public class InternalMeetingController {

    private InternalMeetingService internalMeetingService;

    /**
     * 内审会议分页查询
     * @param
     * @return
     */

    @ApiOperation(value = "内审会议分页查询")
    @GetMapping("/pageInternalMeeting")
    public Result<IPage<InternalMeetingDto>> pageInternalMeeting(Page page,InternalMeeting internalMeeting) throws Exception {
        return Result.success(internalMeetingService.pageInternalMeeting(page, internalMeeting));
    }

    /**
     * 内审会议新增
     * @return
     */

    @ApiOperation(value = "内审会议新增")
    @PostMapping("/addInternalMeeting")
    public Result addInternalMeeting(@RequestBody InternalMeetingDto internalMeeting){
        return Result.success(internalMeetingService.addInternalMeeting(internalMeeting));
    }

    /**
     * 内审会议修改
     * @return
     */

    @ApiOperation(value = "内审会议修改")
    @PostMapping("/updateInternalMeeting")
    public Result updateInternalMeeting(@RequestBody InternalMeetingDto internalMeeting){
        return Result.success(internalMeetingService.updateInternalMeeting(internalMeeting));
    }

    /**
     * 内审会议删除
     * @return
     */

    @ApiOperation(value = "内审会议删除")
    @DeleteMapping("/delInternalMeeting")
    public Result delInternalMeeting(Integer meetingId){
        return Result.success(internalMeetingService.delInternalMeeting(meetingId));
    }

    /**
     * 内审会议查看详情
     * @return
     */

    @ApiOperation(value = "内审会议查看详情")
    @GetMapping("/getInternalMeetingOne")
    public Result<InternalMeetingDto> getInternalMeetingOne(Integer meetingId){
        return Result.success(internalMeetingService.getInternalMeetingOne(meetingId));
    }

    /**
     * 导出内审会议
     * @return
     */

    @ApiOperation(value = "导出内审会议")
    @GetMapping("/exportInternalMeeting")
    public void exportInternalMeeting(Integer meetingId, HttpServletResponse response){
        internalMeetingService.exportInternalMeeting(meetingId, response);
    }

}

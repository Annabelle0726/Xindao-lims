package com.ruoyi.manage.controller;


import com.ruoyi.common.core.domain.Result;
import com.ruoyi.manage.pojo.ManageMeetingParticipants;
import com.ruoyi.manage.service.ManageMeetingParticipantsService;
import com.ruoyi.manage.vo.MeetingParticipantsDetailsVo;
import com.deepoove.poi.data.style.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2024-11-11 09:34:27
 */
@Api(tags = "管理评审会议")
@RestController
@RequestMapping("/manageMeetingParticipants")
public class ManageMeetingParticipantsController {

    @Resource
    private ManageMeetingParticipantsService manageMeetingParticipantsService;


    @ApiOperation(value = "查询会议记录参会人员")
    @GetMapping("/getParticipants")
    public Result<MeetingParticipantsDetailsVo> getParticipants(Integer id){
        return Result.success(manageMeetingParticipantsService.getParticipants(id));
    }

    @ApiOperation(value = "新增会议记录参会人员")
    @PostMapping("/add")
    public Result addParticipants(@RequestBody List<ManageMeetingParticipants> list){
        manageMeetingParticipantsService.saveBatch(list);
        return Result.success();
    }

    @ApiOperation(value = "删除会议记录参会人员")
    @DeleteMapping("/delete")
    public Result deleteParticipants(List<Integer> ids){
        manageMeetingParticipantsService.removeByIds(ids);
        return Result.success();
    }

}

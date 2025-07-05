package com.ruoyi.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceInstructionDto;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.pojo.DeviceInstruction;
import com.ruoyi.device.pojo.OperationInstruction;
import com.ruoyi.device.service.DeviceInstructionService;
import com.ruoyi.device.service.DeviceOperationInstructionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 作业指导书添加受控文件表 前端控制器
 * </p>
 *
 * @author
 * @since 2024-12-04 10:29:18
 */
@RestController
@RequestMapping("/deviceInstruction")
public class DeviceInstructionController {

    @Autowired
    private DeviceInstructionService deviceInstructionService;

    @Autowired
    private DeviceOperationInstructionService deviceOperationInstructionService;


    @ApiOperation(value = "作业指导书分页")
    @GetMapping("/pageByPageQueryOfHomeworkInstructions")
    public Result<IPage<DeviceOperationInstructionDto>> pageByPageQueryOfHomeworkInstructions(Page page, DeviceOperationInstructionDto operationInstructionDto){
        return Result.success(deviceInstructionService.pageByPageQueryOfHomeworkInstructions(page, operationInstructionDto));
    }

    @ApiOperation(value = "作业指导书新增")
    @PostMapping("/newHomeworkGuidebookAdded")
    public Result newHomeworkGuidebookAdded(@RequestBody DeviceInstructionDto instructionDto){
        deviceInstructionService.newHomeworkGuidebookAdded(instructionDto);
        return Result.success();
    }

    @ApiOperation(value = "作业指导书详情")
    @GetMapping("/homeworkGuidebookEditor")
    public Result<Map<String, Object>> homeworkGuidebookEditor(Integer instructionId){
        DeviceInstruction instruction = deviceInstructionService.getById(instructionId);
        List<DeviceOperationInstructionDto> list = deviceOperationInstructionService.homeworkGuidebookEditor(instructionId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("instruction", instruction);
        return Result.success(map);
    }

    @ApiOperation(value = "作业指导书受控文件删除")
    @DeleteMapping("/deleteHomeworkGuidebook")
    public Result deleteHomeworkGuidebook(String ids){
        if (ObjectUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            deviceOperationInstructionService.removeBatchByIds(Arrays.asList(idArray));
        }
        return Result.success();
    }

    @ApiOperation(value = "作业指导书删除")
    @DeleteMapping("/homeworkGuidebook")
    public Result homeworkGuidebook(String id, String instructionId){
        // 删除子表数据
        deviceOperationInstructionService.removeById(id);
        // 如果子表数据为空
        long count = deviceOperationInstructionService.count(Wrappers.<OperationInstruction>lambdaQuery()
                .eq(OperationInstruction::getInstructionId, instructionId));
        // 那么就删除父表数据
        if (count < 1) {
            deviceInstructionService.removeById(id);
        }
        return Result.success();
    }

    @ApiOperation(value = "作业指导书审批")
    @PostMapping("/approvalOfHomeworkInstructionManual")
    public Result approvalOfHomeworkInstructionManual(@RequestBody Map<String,Object> map){
        Integer id =(Integer) map.get("id");
        Boolean status =(Boolean) map.get("status");
        deviceOperationInstructionService.update(Wrappers.<OperationInstruction>lambdaUpdate()
                .eq(OperationInstruction::getId, id)
                .set(OperationInstruction::getStatus, status)
                .set(OperationInstruction::getApproverId, SecurityUtils.getUserId().intValue())
                .set(OperationInstruction::getEntryIntoForceTime, LocalDateTime.now()));
        return Result.success();
    }
}

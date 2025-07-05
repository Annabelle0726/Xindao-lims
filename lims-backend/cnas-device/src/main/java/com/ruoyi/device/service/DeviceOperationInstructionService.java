package com.ruoyi.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.pojo.OperationInstruction;

import java.util.List;

/**
 * <p>
 * 设备 - 作业指导书 添加受控文件 子 服务类
 * </p>
 *
 * @author
 * @since 2024-12-04 10:43:32
 */
public interface DeviceOperationInstructionService extends IService<OperationInstruction> {

    List<DeviceOperationInstructionDto> homeworkGuidebookEditor(Integer instructionId);
}

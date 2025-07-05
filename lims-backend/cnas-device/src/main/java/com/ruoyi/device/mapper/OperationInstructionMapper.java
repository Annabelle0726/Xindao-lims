package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.pojo.OperationInstruction;

import java.util.List;

/**
 * <p>
 * 设备 - 作业指导书 添加受控文件 子 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-12-04 10:43:32
 */
public interface OperationInstructionMapper extends BaseMapper<OperationInstruction> {

    List<DeviceOperationInstructionDto> homeworkGuidebookEditor(Integer instructionId);
}

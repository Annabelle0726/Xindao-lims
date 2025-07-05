package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.mapper.OperationInstructionMapper;
import com.ruoyi.device.pojo.OperationInstruction;
import com.ruoyi.device.service.DeviceOperationInstructionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 设备 - 作业指导书 添加受控文件 子 服务实现类
 * </p>
 *
 * @author
 * @since 2024-12-04 10:43:32
 */
@Service
public class DeviceOperationInstructionServiceImpl extends ServiceImpl<OperationInstructionMapper, OperationInstruction> implements DeviceOperationInstructionService {

    @Override
    public List<DeviceOperationInstructionDto> homeworkGuidebookEditor(Integer instructionId) {
        return baseMapper.homeworkGuidebookEditor(instructionId);
    }
}

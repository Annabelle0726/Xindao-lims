package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.dto.DeviceInstructionDto;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.pojo.DeviceInstruction;

import java.util.Map;

/**
 * <p>
 * 作业指导书添加受控文件表 服务类
 * </p>
 *
 * @author
 * @since 2024-12-04 10:29:18
 */
public interface DeviceInstructionService extends IService<DeviceInstruction> {

    IPage<DeviceOperationInstructionDto> pageByPageQueryOfHomeworkInstructions(Page page, DeviceOperationInstructionDto operationInstructionDto);

    void newHomeworkGuidebookAdded(DeviceInstructionDto instructionDto);
}

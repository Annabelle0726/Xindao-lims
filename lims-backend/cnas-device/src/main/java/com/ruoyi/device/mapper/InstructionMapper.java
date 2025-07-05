package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceOperationInstructionDto;
import com.ruoyi.device.pojo.DeviceInstruction;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 作业指导书添加受控文件表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-12-04 10:29:18
 */
public interface InstructionMapper extends BaseMapper<DeviceInstruction> {


    IPage<DeviceOperationInstructionDto> pageByPageQueryOfHomeworkInstructions(Page page, @Param("ew") QueryWrapper<DeviceOperationInstructionDto> ew);
}

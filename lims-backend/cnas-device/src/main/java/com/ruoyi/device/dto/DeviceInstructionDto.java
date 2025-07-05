package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.DeviceInstruction;
import com.ruoyi.device.pojo.OperationInstruction;
import lombok.Data;

import java.util.List;

@Data
public class DeviceInstructionDto extends DeviceInstruction {

    private List<OperationInstruction> feTempHumRecordList;
}

package com.ruoyi.device.dto;

import com.ruoyi.device.pojo.OperationInstruction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceOperationInstructionDto extends OperationInstruction {
    @ApiModelProperty("申请编号")
    private String applicationNumber;

    @ApiModelProperty("申请部门")
    private String applicationDepartment;

    @ApiModelProperty("责任人")
    private String personLiable;

    @ApiModelProperty("受控申请说明")
    private String controlledApplicationDescription;

    @ApiModelProperty("管理编号")
    private String deviceNumber;

    @ApiModelProperty("型号")
    private String deviceModel;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("上传人")
    private String uploaderName;

    @ApiModelProperty("审批人")
    private String approverName;
}

package com.ruoyi.device.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.device.pojo.Device;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto extends Device {

    @ApiModelProperty(value = "管理人")
    private String equipmentManagerUser;

    @ApiModelProperty(value = "所属部门")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String laboratoryName;

    @ApiModelProperty(value = "检验项目")
    private String insProductItem;

    @ApiModelProperty(value = "管理人姓名")
    private String equipmentManagerName;

    @ApiModelProperty(value = "被授权人姓名")
    private String authorizedPersonName;

    @ApiModelProperty(value = "核准证书编号")
    private String calibrateNo;

    @ApiModelProperty(value = "最近校准日期")
    private LocalDateTime lastCalibrationDateTwo;

    @ApiModelProperty(value = "下次校准日期")
    private LocalDateTime nextCalibrationDateTwo;


}

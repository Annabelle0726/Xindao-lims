package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.ProcessMethodVerifyCalibrationsFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/12
 */
@Data
public class ProcessMethodVerifyCalibrationsFileDto extends ProcessMethodVerifyCalibrationsFile {

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String managementNumber;
}

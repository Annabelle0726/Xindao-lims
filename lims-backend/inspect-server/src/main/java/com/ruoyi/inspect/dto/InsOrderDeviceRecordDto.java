package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsOrderDeviceRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/12/22
 */
@Data
public class InsOrderDeviceRecordDto extends InsOrderDeviceRecord {

    @ApiModelProperty("设备编号")
    private String managementNumber;
}

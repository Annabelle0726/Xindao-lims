package com.ruoyi.device.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.device.pojo.DeviceInspectionRecord;
import com.ruoyi.device.pojo.DeviceInspectionRecordDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: yuan
 * Date: 2024-12-16 星期一 17:23:22
 * Description:
 */
@Data
public class DeviceInspectionRecordDto extends DeviceInspectionRecord {

    @TableField(exist = false)
    @ApiModelProperty("测试详情内容")
    private List<DeviceInspectionRecordDetails> details;

    @ApiModelProperty("测试时间")
    private String testDateString;
}

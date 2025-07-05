package com.ruoyi.device.excel;

import com.ruoyi.device.pojo.DeviceRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-10 星期二 17:19:22
 * Description:
 */
@Data
public class DeviceRecordExportWord extends DeviceRecord {
    @ApiModelProperty("使用前0代表不正常1代表正常")
    private String useBeforeString;

    @ApiModelProperty("使用后0代表不正常1代表正常")
    private String useAfterString;

    @ApiModelProperty("操作时间 String yyyy-MM-dd")
    private String operationDate;

    @ApiModelProperty("使用开始日期 String yyyy-MM-dd \n HH:mm:ss")
    private String useStartDateString;

    @ApiModelProperty("使用结束日期 String yyyy-MM-dd \n HH:mm:ss")
    private String useEndDateString;
}

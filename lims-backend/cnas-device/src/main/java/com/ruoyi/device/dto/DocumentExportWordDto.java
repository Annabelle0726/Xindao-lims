package com.ruoyi.device.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-13 星期五 8:45:34
 * Description: 用于仪器设备档案卡的导出
 */
@Data
public class DocumentExportWordDto {

    // 设备档案左列表
    @ApiModelProperty("序号")
    private Integer index1;

    @ApiModelProperty("档案名称")
    private String name1;

    @ApiModelProperty("份数")
    private Integer quantity1;

    @ApiModelProperty("页码")
    private Integer pageCount1;

    @ApiModelProperty("归档日期")
    private String archiveDateString1;

    // 设备档案右列表
    @ApiModelProperty("序号")
    private Integer index2;

    @ApiModelProperty("档案名称")
    private String name2;

    @ApiModelProperty("份数")
    private Integer quantity2;

    @ApiModelProperty("页码")
    private Integer pageCount2;

    @ApiModelProperty("归档日期")
    private String archiveDateString2;
}

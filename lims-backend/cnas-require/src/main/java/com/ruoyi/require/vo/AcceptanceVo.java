package com.ruoyi.require.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcceptanceVo {

    @ApiModelProperty("检验表id")
    private Integer id;

    @ApiModelProperty("标准物质名称")
    private String name;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("生产厂家")
    private String factoryManufacturer;

    @ApiModelProperty("出场编号")
    private String factoryNum;

    @ApiModelProperty("管理编号")
    private String manageNum;

    @ApiModelProperty("不确定度")
    private String uncertainty;

    @ApiModelProperty("数量")
    private Long quantity;

    @ApiModelProperty("购置日期")
    private LocalDateTime acquisitionDate;

    @ApiModelProperty("有效期")
    private LocalDateTime effectiveDate;

    @ApiModelProperty("文档编号")
    private String fileNum;

    @ApiModelProperty("存放位置")
    private String position;
}

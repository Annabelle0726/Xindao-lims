package com.ruoyi.require.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubstanceRecordVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("标准物质名称")
    private String name;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("出场编号")
    private String factoryNum;

    @ApiModelProperty("数量")
    private Long quantity;

    @ApiModelProperty("领用人")
    private String borrowUser;

    @ApiModelProperty("借出日期")
    private LocalDateTime borrowDate;

    @ApiModelProperty("归还日期")
    private LocalDateTime returnDate;

    @ApiModelProperty("归还人")
    private String returnedPerson;

    @ApiModelProperty("完好性")
    private String returnIntegrity;

    @ApiModelProperty("0：借用 1:归还")
    private String status;

    @ApiModelProperty("备注")
    private String remark;
}

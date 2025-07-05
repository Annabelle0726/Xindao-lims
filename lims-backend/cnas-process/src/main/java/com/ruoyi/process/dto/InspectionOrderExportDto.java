package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.InspectionOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-10 星期二
 * Description:
 */
@Data
public class InspectionOrderExportDto extends InspectionOrder {
    @ApiModelProperty("留样:1 是")
    private String isLeave1 = "□";

    @ApiModelProperty("留样:0 否")
    private String isLeave2 = "□";

    @ApiModelProperty("报告发送方式 1：自取")
    private String send1 = "□";

    @ApiModelProperty("报告发送方式 0：其他")
    private String send0 = "□";

    @ApiModelProperty("样品处理方式 1：实验室处理")
    private String processing1 = "□";

    @ApiModelProperty("样品处理方式 0：委托单位取回")
    private String processing0 = "□";

    @ApiModelProperty("判断规则 1：考虑不确定度")
    private String criterionRule1 = "□";

    @ApiModelProperty("判断规则 0：不考虑不确定度")
    private String criterionRule0 = "□";

    @ApiModelProperty("委托时间 字符串格式：yyyy年MM月dd")
    private String commissionDateString;

    @ApiModelProperty("接收日期 字符串格式：yyyy年MM月dd")
    private String receiptDataString;

    @ApiModelProperty("领样日期 字符串格式：yyyy年MM月dd")
    private String sampleDataString;

    @ApiModelProperty("约定时间 字符串格式：yyyy年MM月dd")
    private String appointedString;


}

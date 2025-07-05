package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.QualityMonitorDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/3/24
 */
@Data
public class QualityMonitorDetailsDto extends QualityMonitorDetails {

    @ApiModelProperty("实施状态, 0: 未开始, 1:待批准, 2:不批准, 3:已批准")
    private Integer detailsRatifyStatus;

    @ApiModelProperty("报告状态, 0: 未开始, 1:待提交, 2:补批准, 3:已批准")
    private Integer reportStatus;

    @ApiModelProperty("评价状态, 0: 未开始, 1:待评价, 2:待批准, 3:已批准")
    private Integer evaluateStatus;

}

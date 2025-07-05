package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.QualitySuperviseDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/7
 */
@Data
public class QualitySuperviseDetailsDto extends QualitySuperviseDetails {

    @ApiModelProperty("是否符合,0 不符合, 1符合")
    private Integer isAccording;

    @ApiModelProperty("2是否纠正处理, 0否, 1是")
    private Integer isCorrect;

    @ApiModelProperty("质量监督详情纠正处理id")
    private Integer superviseDetailsCorrectId;

    @ApiModelProperty("监督原因类型, 1:定期监督, 2:动态监督")
    private Integer causeType;

    @ApiModelProperty("记录状态, 0: 未开始, 1:待批准, 2:不批准, 3:已批准")
    private Integer recordStatus;

    @ApiModelProperty("控制状态, 0: 未开始, 1:待批准, 2:不批准, 3:已批准")
    private Integer accordingStatus;

    @ApiModelProperty("纠正状态, 0: 未开始, 1:待批准, 2:不批准, 3:已批准")
    private Integer correctStatus;
}

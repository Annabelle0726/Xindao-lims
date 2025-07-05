package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonTraining;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PersonTrainingDto对象", description = "培训计划")
public class PersonTrainingDto extends PersonTraining {

    @ApiModelProperty("编制人姓名")
    private String compilerName;

    @ApiModelProperty("审核人姓名")
    private String reviewerName;

    @ApiModelProperty("批准人姓名")
    private String approverName;

    @ApiModelProperty("创建人姓名")
    private String createUserName;
}

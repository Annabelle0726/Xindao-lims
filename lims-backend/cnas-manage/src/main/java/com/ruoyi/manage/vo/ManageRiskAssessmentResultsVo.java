package com.ruoyi.manage.vo;

import com.ruoyi.manage.pojo.ManageRiskAssessmentResults;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManageRiskAssessmentResultsVo extends ManageRiskAssessmentResults {

    @ApiModelProperty("编制姓名")
    private String editorName;

    @ApiModelProperty("审批姓名")
    private String approvalName;

    @ApiModelProperty("批准姓名")
    private String approveName;

    @ApiModelProperty("导出序号")
    private Integer index;
}

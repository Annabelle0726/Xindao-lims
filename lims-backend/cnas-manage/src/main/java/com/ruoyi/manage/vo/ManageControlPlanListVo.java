package com.ruoyi.manage.vo;

import com.ruoyi.manage.pojo.ManageControlPlanList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManageControlPlanListVo extends ManageControlPlanList {
    @ApiModelProperty("编制姓名")
    private String editorName;

    @ApiModelProperty("审批姓名")
    private String approvalName;

    @ApiModelProperty("批准姓名")
    private String approveName;

    @ApiModelProperty("导出")
    private Integer index;
}

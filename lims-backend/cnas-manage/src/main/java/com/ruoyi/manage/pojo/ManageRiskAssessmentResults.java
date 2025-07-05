package com.ruoyi.manage.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 危险因素辨识与风险评价结果一览表
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:51
 */
@Getter
@Setter
@TableName("cnas_manage_risk_assessment_results")
@ApiModel(value = "ManageRiskAssessmentResults对象", description = "危险因素辨识与风险评价结果一览表")
public class ManageRiskAssessmentResults implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    @ApiModelProperty("地点/活动")
    @ExcelProperty(value = "地点/活动", index = 0)
    private String venue;

    @ApiModelProperty("危险因素")
    @ExcelProperty(value = "危险因素", index = 1)
    private String hazard;

    @ApiModelProperty("可能导致的事故")
    @ExcelProperty(value = "可能导致的事故", index = 2)
    private String accidents;

    @ApiModelProperty("对人可能造成的危害")
    @ExcelProperty(value = "对人可能造成的危害", index = 3)
    private String injury;

    @ApiModelProperty("风险评价")
    @ExcelProperty(value = {"风险评价", "L"})
    private String riskL;

    @ApiModelProperty("风险评价")
    @ExcelProperty(value = {"风险评价", "E"})
    private String riskE;

    @ApiModelProperty("风险评价")
    @ExcelProperty(value = {"风险评价", "C"})
    private String riskC;

    @ApiModelProperty("风险评价")
    @ExcelProperty(value = {"风险评价", "D"})
    private String riskD;

    @ApiModelProperty("风险等级")
    @ExcelProperty(value = "风险等级", index = 8)
    private String level;

    @ApiModelProperty("评价结论")
    @ExcelProperty(value = "评价结论", index = 9)
    private String conclusion;

    @ApiModelProperty("控制措施")
    @ExcelProperty(value = "控制措施", index = 10)
    private String measures;

    @ApiModelProperty("编制id")
    private Integer editor;

    @ApiModelProperty("编制日期")
    private LocalDateTime editorDate;

    @ApiModelProperty("审批人id")
    private Integer approval;

    @ApiModelProperty("审批日期")
    private LocalDateTime approvalDate;

    @ApiModelProperty("批准人id")
    private Integer approve;

    @ApiModelProperty("批准日期")
    private LocalDateTime approveDate;

    @ApiModelProperty("批准状态1：通过；2：不通过")
    private Integer approveStatus;

    @ApiModelProperty("审批状态1：通过；2：不通过")
    private Integer approvalStatus;
}

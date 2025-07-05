package com.ruoyi.manage.pojo;

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
 * 重大风险因素分析及控制计划清单
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:30
 */
@Getter
@Setter
@TableName("cnas_manage_control_plan_list")
@ApiModel(value = "ManageControlPlanList对象", description = "重大风险因素分析及控制计划清单")
public class ManageControlPlanList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("作业活动")
    @ExcelProperty("作业活动")
    private String jobActivity;

    @ApiModelProperty("风险因素类别")
    @ExcelProperty("风险因素类别")
    private String category;

    @ApiModelProperty("风险因素描述")
    @ExcelProperty("风险因素描述")
    private String description;

    @ApiModelProperty("可导致的事故")
    @ExcelProperty("可导致的事故")
    private String result;

    @ApiModelProperty("是否不可承受风险")
    @ExcelProperty("是否不可承受风险")
    private String intolerable;

    @ApiModelProperty("控制计划")
    @ExcelProperty("控制计划")
    private String plan;

    @ApiModelProperty("编制id")
    private Integer editor;

    @ApiModelProperty("日期")
    private LocalDateTime editorDate;

    @ApiModelProperty("审批id")
    private Integer approval;

    @ApiModelProperty("日期")
    private LocalDateTime approvalDate;

    @ApiModelProperty("批准id")
    private Integer approve;

    @ApiModelProperty("批准日期")
    private LocalDateTime approveDate;

    @ApiModelProperty("批准状态1：通过；2：不通过")
    private Integer approveStatus;

    @ApiModelProperty("审批状态1：通过；2：不通过")
    private Integer approvalStatus;
}

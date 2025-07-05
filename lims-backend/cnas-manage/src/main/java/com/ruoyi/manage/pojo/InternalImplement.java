package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 内审实施计划
 *
 * @author zhuo
 * @since 2024-11-11
 */
@Data
@TableName("cnas_internal_implement")
public class InternalImplement {

    @TableId(type = IdType.AUTO)
    private Integer implementId;

    @ApiModelProperty("审核目的")
    private String purposes;

    @ApiModelProperty("审核性质")
    private String nature;

    @ApiModelProperty("审核范围")
    private String scope;

    @ApiModelProperty("审核依据")
    private String basis;

    @ApiModelProperty("审核组长")
    private String teamLeader;

    @ApiModelProperty("内审员")
    private String internalAuditor;

    @ApiModelProperty("审核时间")
    private String reviewDate;

    @ApiModelProperty("审核方法")
    private String auditMethod;

    @ApiModelProperty("会议开始时间")
    private String firstMeetingTime;

    @ApiModelProperty("末次会议时间")
    private String lastMeetingTime;

    @ApiModelProperty("审核报告提交日期")
    private String submitTime;

    @ApiModelProperty("审核报告发放范围")
    private String submitScope;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("编制人id")
    private Integer writeUserId;

    @ApiModelProperty("编制人")
    private String writeUserName;

    @ApiModelProperty("编制时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;

    @ApiModelProperty("批准人id")
    private Integer ratifyUserId;

    @ApiModelProperty("批准人")
    private String ratifyUserName;

    @ApiModelProperty("批准时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("批准状态, 0 不通过, 1通过")
    private Integer ratifyStatus;

    @ApiModelProperty("批准内容")
    private String ratifyRemark;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}


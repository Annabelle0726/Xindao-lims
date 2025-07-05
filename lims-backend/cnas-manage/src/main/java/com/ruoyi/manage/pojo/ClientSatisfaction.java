package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 客户满意度
 *
 * @author zhuo
 * @since 2024-11-09
 */
@Data
@TableName("cnas_client_satisfaction")
public class ClientSatisfaction {

    @TableId(type = IdType.AUTO)
    private Integer clientSatisfactionId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("填写日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fillDate;

    @ApiModelProperty("部门")
    private String department;

    @ApiModelProperty("联系电话")
    private String contactNumber;

    @ApiModelProperty("服务态度, 0:满意, 1:一般, 2:不满意")
    private Integer serviceAttitude;

    @ApiModelProperty("服务态度建议")
    private String serviceAttitudeSuggestion;

    @ApiModelProperty("技术能力, 0:满意, 1:一般, 2:不满意")
    private Integer technicalCompetence;

    @ApiModelProperty("技术能力建议")
    private String technicalCompetenceSuggestion;

    @ApiModelProperty("检测工作, 0:满意, 1:一般, 2:不满意")
    private Integer inspectionWork;

    @ApiModelProperty("检测工作建议")
    private String inspectionWorkSuggestion;

    @ApiModelProperty("收费合理性, 0:满意, 1:一般, 2:不满意")
    private Integer reasonableFees;

    @ApiModelProperty("收费合理性建议")
    private String reasonableFeesSuggestion;

    @ApiModelProperty("确认人")
    private String confirmPerson;

    @ApiModelProperty("确认人Id")
    private Integer confirmPersonId;

    @ApiModelProperty("确认状态, 0:未确认, 1:确认")
    private Integer confirmStatus;

    @ApiModelProperty("备注(对我们的希望)")
    private String remark;

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


package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监督主表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Data
@TableName("cnas_quality_supervise")
@ApiModel(value = "QualitySupervise对象", description = "质量监督主表")
public class QualitySupervise {

    @TableId(type = IdType.AUTO)
    private Integer superviseId;

    @ApiModelProperty("监督名称")
    private String superviseName;

    @ApiModelProperty("年份")
    private String superviseYear;

    @ApiModelProperty("监督人id(多个)")
    private String recordUserIds;

    @ApiModelProperty("外键：用户id（编制人）")
    private Integer writeUserId;

    @ApiModelProperty("编制人")
    private String writeUserName;

    @ApiModelProperty("编制时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;

    @ApiModelProperty("外键：用户id（批准人）")
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


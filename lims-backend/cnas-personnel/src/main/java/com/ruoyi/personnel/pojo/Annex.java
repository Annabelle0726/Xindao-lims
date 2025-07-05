package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("cnas_annex")
@ApiModel("人员基本信息附件表")
public class Annex implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "user表id")
    private Integer userId;

    @ApiModelProperty(value = "证件号")
    private String idNumber;

    @ApiModelProperty(value = "发证单位")
    private String issueUnit;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "级别")
    private String level;

    @ApiModelProperty(value = "有效期")
    private String periodValidity;

    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "复印件")
    private String copy;

    @ApiModelProperty(value = "原件")
    private String original;
}

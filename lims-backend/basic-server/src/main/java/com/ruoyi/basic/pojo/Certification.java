package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName(value = "certification")
@Data
public class Certification implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "资质名称")
    private String name;

    @ApiModelProperty(value = "资质编码")
    private String code;

    @ApiModelProperty(value = "颁发机构")
    private String organization;

    @ApiModelProperty(value = "资质说明")
    private String explanation;

    @ApiModelProperty(value = "首次颁发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstIssuanceDate;

    @ApiModelProperty(value = "最近颁发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestIssuanceDate;



    @ApiModelProperty(value = "到期颁发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;


    @ApiModelProperty(value = "颁布日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfIssuance;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;


    @ApiModelProperty(value = "更新人")
    private String createUserName;

    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "资质图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "资质附件地址")
    private String fileUrl;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

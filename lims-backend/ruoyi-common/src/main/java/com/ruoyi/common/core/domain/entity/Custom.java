package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("custom")
public class Custom implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户名称")
    private String company;

    @ApiModelProperty(value = "单位地址")
    private String address;

    @ApiModelProperty(value = "工厂域")
    private String code;

    @ApiModelProperty(value = "客户编号")
    private String code2;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建日期")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "更新用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "客户单位EN")
    private String companyEn;

    @ApiModelProperty(value = "单位地址EN")
    private String addressEn;

    @ApiModelProperty(value = "加急额度")
    private Integer num;

    @ApiModelProperty(value = "单位电话")
    private String phone;
}

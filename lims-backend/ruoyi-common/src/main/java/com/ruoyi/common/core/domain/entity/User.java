package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 *
 * @author zhuo
 * @since 2025-02-13
 */
@Data
@TableName("user")
public class User {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "用户昵称")
    private String name;

    @ApiModelProperty(value = "英文名")
    private String nameEn;

    @ApiModelProperty(value = "用户类型（00系统用户）")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "签名地址")
    private String signatureUrl;

    @ApiModelProperty(value = "头像地址")
    private String pictureUrl;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginDate;

    @ApiModelProperty(value = "cnas实验室id")
    private String departLimsId;

    @ApiModelProperty(value = "单位id")
    private String company;

    @ApiModelProperty(value = "是否客户 0: 否, 1:是")
    private Integer isCustom;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}


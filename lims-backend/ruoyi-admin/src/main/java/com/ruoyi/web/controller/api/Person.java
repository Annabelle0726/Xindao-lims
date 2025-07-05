package com.ruoyi.web.controller.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员信息
 */
@Data
public class Person {

    private String name;

    //人员编号，全局唯一
    private String employeeID;
    //岗位
    private String position;
    //岗位编码
    private String positionCode;
    //手机号码
    private String phoneNumber;

    //性别 1:男 2:女
    private Integer gender;

    //组织状态 enable:在职 disabled:离职
    private String status;
    //所属组织编码
    private String companyId;
    //公司邮箱(可用于邮箱系统)
    private String companyEmail;
    //入职公司时间
    private String dateOfJoiningTheCompany;
    //组织内的部门编码
    private String departmentCode;

    @ApiModelProperty(">1：存在 =0：不存在")
    private Long isLive;

    private String department;
}

package com.ruoyi.framework.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * zhuo
 */
@Data
public class SsoUserInfoModel {
    private String avatar;

    @JSONField(name="nick_name")
    private String nickName;

    private String openid;

    @JSONField(name="org_id")
    private String orgId;

    @JSONField(name="org_name")
    private String orgName;

    @JSONField(name="org_role")
    private String orgRole;

    @JSONField(name="phone_number")
    private String phoneNumber;

    private String sid;


    private String sub;

    @JSONField(name="employee_id")
    private String employeeId;

    @JSONField(name="department_code")
    private String departmentCode;
}

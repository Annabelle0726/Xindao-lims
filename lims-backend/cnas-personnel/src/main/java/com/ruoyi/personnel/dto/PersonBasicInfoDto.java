package com.ruoyi.personnel.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ruoyi.personnel.pojo.PersonBasicInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ExcelIgnoreUnannotated
public class PersonBasicInfoDto extends PersonBasicInfo {
    @ApiModelProperty(value = "账号")
    @ExcelProperty("员工编号")
    private String account;

    @ApiModelProperty(value = "姓名")
    @ExcelProperty("姓名")
    private String name;

    @ApiModelProperty(value = "姓名(英文)")
    private String nameEn;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "电话号码")
    @ExcelProperty("手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "lims组织架构")
    private String departLimsId;

    @ApiModelProperty(value = "签名照片地址")
    private String signatureUrl;

    @ApiModelProperty(value = "自身照片地址")
    private String pictureUrl;

}

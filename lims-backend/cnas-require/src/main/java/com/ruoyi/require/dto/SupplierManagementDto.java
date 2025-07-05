package com.ruoyi.require.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class SupplierManagementDto {

    @ApiModelProperty("供应商")
    @ExcelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("编号")
    @ExcelProperty("编号")
    private String supplierRef;

    @ApiModelProperty("供应商物品服务名称")
    @ExcelProperty("供应商物品服务名称")
    private String supplierItemServiceName;

    @ApiModelProperty("邮编")
    @ExcelProperty("邮编")
    private String postalCode;

    @ApiModelProperty("地址")
    @ExcelProperty("地址")
    private String adress;

    @ApiModelProperty("联系人")
    @ExcelProperty("联系人")
    private String contacts;

    @ApiModelProperty("联系电话")
    @ExcelProperty("联系电话")
    private String phone;

    @ApiModelProperty("户名")
    @ExcelProperty("户名")
    private String householdName;

    @ApiModelProperty("传真")
    @ExcelProperty("传真")
    private String fax;

    @ApiModelProperty("开户行")
    @ExcelProperty("开户行")
    private String openingName;

    @ApiModelProperty("网址")
    @ExcelProperty("网址")
    private String website;

    @ApiModelProperty("账号")
    @ExcelProperty("账号")
    private String accountName;

    @ApiModelProperty("email")
    @ExcelProperty("email")
    private String email;

    @ApiModelProperty("备注")
    @ExcelProperty("备注")
    private String remarks;
}

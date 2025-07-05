package com.ruoyi.require.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-11-15 02:46:45
 */
@Getter
@Setter
@TableName("cnas_supplier_management")
@ApiModel(value = "SupplierManagement对象", description = "")
public class SupplierManagement implements Serializable {


    @ApiModelProperty("主表Id")
    @TableId(value = "supplier_management_id", type = IdType.AUTO)
    private Integer supplierManagementId;

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

    @ApiModelProperty("logo")
    private String logo;

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

    @ApiModelProperty("附件")
    private String enclosure;

    @ApiModelProperty("状态")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("父id")
    private Integer parentId;
}

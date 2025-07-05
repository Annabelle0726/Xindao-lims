package com.ruoyi.require.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 标准物质验收
 * </p>
 *
 * @author
 * @since 2024-11-14 03:29:41
 */
@Getter
@Setter
@TableName("cnas_fe_standard_substance_acceptance")
@ApiModel(value = "FeStandardSubstanceAcceptance对象", description = "标准物质验收")
public class FeStandardSubstanceAcceptance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("清单id")
    @ExcelProperty("清单id")
    private Integer substanceId;

    @ApiModelProperty("到货日期")
    @ExcelProperty("到货日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arriveDate;

    @ApiModelProperty("维修单位")
    @ExcelProperty("维修单位")
    private String maintenanceUnit;

    @ApiModelProperty("参数")
    @ExcelProperty("参数")
    private String perameters;

    @ApiModelProperty("安装调试情况")
    @ExcelProperty("安装调试情况")
    private String installation;

    @ApiModelProperty("验收情况")
    @ExcelProperty("验收情况")
    private String situation;

    @ApiModelProperty("接受签字")
    @ExcelProperty("接受签字")
    private String signature;

    @ApiModelProperty("厂家代表")
    @ExcelProperty("厂家代表")
    private String producer;

    @ApiModelProperty("接收人")
    @ExcelProperty("接收人")
    private String recipient;

    @ApiModelProperty("附件")
    @ExcelProperty("附件")
    private String file;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

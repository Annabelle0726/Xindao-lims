package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 检验报告发放登记表
 * </p>
 *
 * @author
 * @since 2024-11-05 08:58:39
 */
@Getter
@Setter
@TableName("cnas_process_report")
@ApiModel(value = "ProcessReport对象", description = "检验报告发放登记表")
public class ProcessReport  implements Serializable {

    @TableField(select = false,exist = false)
    //导出序号
    private Integer indexs;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("检验报告编号")
    private String insReportCode;

    @ApiModelProperty("页数")
    private String pages;

    @ApiModelProperty("发送份数")
    private String number;

    @ApiModelProperty("发往何处")
    private String send;

    @ApiModelProperty("发送方式")
    private String method;

    @ApiModelProperty("发送日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sendTime;

    @ApiModelProperty("发送人")
    private Integer sendUser;
    @ApiModelProperty("签收人")
    private String signatory;

    @ApiModelProperty("备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("委托单id")
    private Integer inspectionOrderId;
}

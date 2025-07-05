package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;


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
 * 样品接收
 * </p>
 *
 * @author
 * @since 2024-12-12 05:02:49
 */
@Getter
@Setter
@TableName("cnas_process_sample")
@ApiModel(value = "ProcessSample对象", description = "样品接收")
public class ProcessSample  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("来样单位")
    private String sampleSupplier;

    @ApiModelProperty("样品数量")
    private Integer num;

    @ApiModelProperty("样品状态")
    private String sampleState;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("委托单id")
    private Integer inspectionOrderId;

    @ApiModelProperty("收样日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiveDate;

    @ApiModelProperty("留样日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaveDate;

    @ApiModelProperty("退样签收/处理日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dealTime;
}

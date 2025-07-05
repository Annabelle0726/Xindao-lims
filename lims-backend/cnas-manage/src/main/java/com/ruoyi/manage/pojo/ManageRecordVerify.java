package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 外来文件确认记录
 * </p>
 *
 * @author 
 * @since 2024-11-12 10:29:44
 */
@Getter
@Setter
@TableName("cnas_manage_record_verify")
@ApiModel(value = "ManageRecordVerify对象", description = "外来文件确认记录")
public class ManageRecordVerify  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("外来文件名称")

    private String documentName;

    @ApiModelProperty("文件编号")

    private String documentCode;

    @ApiModelProperty("标准规范名称")
    private String standardName;

    @ApiModelProperty("标准号")
    private String standardCode;

    @ApiModelProperty("生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @ApiModelProperty("作废日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate cancelDate;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("外键关联,外来文件确认历史记录id")
    private Integer manageRecordTotalId;
}

package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 所有文件(内、外部文件)的发放与回收记录
 * </p>
 *
 * @author 
 * @since 2024-11-13 09:11:05
 */
@Getter
@Setter
@TableName("cnas_manage_record_issue_recycle")
@ApiModel(value = "ManageRecordIssueRecycle对象", description = "所有文件(内、外部文件)的发放与回收记录")
public class ManageRecordIssueRecycle  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件编号")
    private String documentCode;

    @ApiModelProperty("文件名称")
    private String documentName;

    @ApiModelProperty("版号")
    private String documentVersion;

    @ApiModelProperty("份数")
    private String pages;

    @ApiModelProperty("文件类别")
    private String documentType;

    @ApiModelProperty("分发号")
    private String number;

    @ApiModelProperty("接收部门")
    private String departLims;

    @ApiModelProperty("接受人")
    private Integer receiveUser;

    @TableField(select = false,exist = false)
    private String receiveUserName;

    @ApiModelProperty("接受日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiveDate;

    @ApiModelProperty("签收人")
    private Integer signedUser;

    @TableField(select = false,exist = false)
    private String signedUserName;

    @ApiModelProperty("签收日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate signedDate;
}

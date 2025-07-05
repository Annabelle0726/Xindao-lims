package com.ruoyi.manage.pojo;

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
 * 作废文件销魂记录
 * </p>
 *
 * @author 
 * @since 2024-11-13 01:27:22
 */
@Getter
@Setter
@TableName("cnas_manage_record_cancel")
@ApiModel(value = "ManageRecordCancel对象", description = "作废文件销魂记录")
public class ManageRecordCancel  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件编号")

    private String documentCode;

    @ApiModelProperty("文件名称")

    private String documentName;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("销毁原因")
    private String reason;

    @ApiModelProperty("申请人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(select = false,exist = false)
    private String createUserName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("批准人")
    private Integer ratifyUser;

    @TableField(select = false,exist = false)
    private String ratifyUserName;

    private String ratifyState;

    @ApiModelProperty("批准日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ratifyTime;

    @ApiModelProperty("备注")
    private String remark;
}

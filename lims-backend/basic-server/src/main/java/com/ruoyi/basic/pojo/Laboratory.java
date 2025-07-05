package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实验室管理(Laboratory)表对象
 */
@TableName(value = "laboratory")
@Data
@NoArgsConstructor
public class Laboratory  implements Serializable {
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "实验室名称")
    private String laboratoryName;

    @ApiModelProperty(value = "场所编码")
    private String laboratoryNumber;

    @ApiModelProperty(value = "实验室代号")
    private String laboratoryCode;

    @ApiModelProperty(value = "负责人电话")
    private String phoneNumber;

    @ApiModelProperty(value = "负责人")
    @TableField(fill = FieldFill.INSERT)
    private String head;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "创建人")
    @TableField(select = false,exist = false)
    private String createUserName;

    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

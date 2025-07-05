package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 印章管理(Laboratory)表对象
 */

@TableName(value = "seal")
@Data
public class Seal  implements Serializable {
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "实验室id")
    private Integer labId;

    @TableField(exist=false)
    @ApiModelProperty(value = "实验室名称")
    private String laboratoryName;

    @ApiModelProperty(value = "印章图片")
    private String address;

    @ApiModelProperty(value = "印章类型")
    private String type;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}


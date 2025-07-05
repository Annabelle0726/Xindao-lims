package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @TableName ins_order_state
 */
@TableName(value ="ins_order_state")
@Data
public class InsOrderState implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 外键：ins_order表id
     */
    @TableField(value = "ins_order_id")
    private Integer insOrderId;

    /**
     * 实验室
     */
    @TableField(value = "laboratory")
    private String laboratory;

    /**
     * 检验状态(0：待检验1:检验中 2:已检验3：待复核4：复核未通过 5：复核通过)
     */
    @TableField(value = "ins_state")
    private Integer insState;

    @ApiModelProperty("检验时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("复核人")
    private Integer verifyUser;

    @ApiModelProperty("复核理由")
    private String verifyTell;
}

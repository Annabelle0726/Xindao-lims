package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 不合格复测检验项目的结果
 *
 * @author zhuo
 * @since 2024-09-03
 */
@TableName(value = "ins_unqualified_retest_result")
@Data
public class InsUnqualifiedRetestResult implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    //外键：不合格检验项目id
    private Integer retestProductId;
    //检验值
    private String insValue;
    //计算值
    private String comValue;
    //设备编码
    private String equipValue;
    //设备名称
    private String equipName;

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

}


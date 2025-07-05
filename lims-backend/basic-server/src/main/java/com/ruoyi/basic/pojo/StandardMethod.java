package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准方法
 * @TableName standard_method
 */
@TableName(value ="standard_method")
@Data
public class StandardMethod implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String field;

    @ApiModelProperty("检验对象")
    private String structureTestObjectId;

    /**
     * 标准编号
     */
    @ApiModelProperty(value = "标准编号")
    private String code;

    /**
     * 标准方法
     */
    @ApiModelProperty(value = "标准描述")
    private String name;

    @ApiModelProperty(value = "标准描述EN")
    private String nameEn;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "资质")
    private String qualificationId;

    @ApiModelProperty(value = "是否产品标准")
    private Integer isProduct;

    @ApiModelProperty(value = "是否启用")
    private Integer isUse;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建人")
    @TableField(exist = false,select = false)
    private String createUserName;

    @ApiModelProperty(value = "更新人")
    @TableField(exist = false,select = false)
    private String updateUserName;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

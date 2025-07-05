package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检测对象(StructureTestObject)表对象
 *
 * @author makejava
 * @since 2024-02-26 17:36:41
 */
@TableName(value ="structure_test_object")
@Data
public class StructureTestObject implements Serializable {
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "场所")
    private Integer laboratoryId;

    @ApiModelProperty(value = "检验对象")
    private String specimenName;

    @ApiModelProperty(value = "检验对象EN")
    private String specimenNameEn;

    @ApiModelProperty(value = "对象代号")
    private String code;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

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

    @ApiModelProperty(value = "排序")
    private Integer sort;

    //"对象类型, 1:原材料, 2:成品, 3:辅材"
    @ApiModelProperty(value = "对象类型")
    private String objectType;


}


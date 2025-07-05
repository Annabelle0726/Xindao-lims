package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标准查新
 *
 * @author zhuo
 * @since 2024-11-04
 */
@Data
@TableName("cnas_process_method_search_new")
@ApiModel(value = "标准查新对象", description = "标准查新表")
public class ProcessMethodSearchNew {

    @TableId(type = IdType.AUTO)
    private Integer methodSearchNewId;

    @ApiModelProperty("标准方法验证id")
    private Integer verifyId;

    @ApiModelProperty("标准名称")
    private String methodName;

    @ApiModelProperty("标准号")
    private String standardNo;

    @ApiModelProperty("文件编号")
    private String fileNo;

    @ApiModelProperty("是否是新标准, 0否,1是")
    private Integer isNewStandard;

    @ApiModelProperty("新标准名称")
    private String newMethodName;

    @ApiModelProperty("新标准号")
    private String newStandardNo;

    @ApiModelProperty("查新记录来源, 0,标准网, 1委托情报, 2标准数, 3其他")
    private Integer searchNewSource;

    @ApiModelProperty("备注,0作废, 1替换")
    private Integer remark;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}


package com.ruoyi.manage.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件清单

 * </p>
 *
 * @author
 * @since 2024-11-08 11:08:11
 */
@Getter
@Setter
@TableName("cnas_manage_document_list")
@ApiModel(value = "ManageDocumentList对象", description = "文件清单")
@ExcelIgnoreUnannotated
public class ManageDocumentList  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件编号")
    @ExcelProperty(value = "文件编号")
    private String documentCode;

    @ApiModelProperty("类别")
    @ExcelProperty(value = "类别")
    private String type;

    @ApiModelProperty("名称")
    @ExcelProperty(value = "名称")
    private String name;

    @ApiModelProperty("文件版本")
    @ExcelProperty(value = "文件版本")
    private String version;

    @ApiModelProperty("作者")
    @ExcelProperty(value = "作者")
    private String writer;

    @ApiModelProperty("生效日期")
    @ExcelProperty(value = "生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @ApiModelProperty("文件状态")
    @ExcelProperty(value = "文件状态")
    private String state;

    @ApiModelProperty("附件地址")
    private String url;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

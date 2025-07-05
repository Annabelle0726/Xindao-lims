package com.ruoyi.process.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 标准查新备份表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-25 05:29:02
 */
@Getter
@Setter
@ExcelIgnoreUnannotated
@TableName("cnas_process_method_search_new_backups")
@ApiModel(value = "ProcessMethodSearchNewBackups对象", description = "标准查新备份表")
public class ProcessMethodSearchNewBackups{

    @TableId(value = "backups_id", type = IdType.AUTO)
    private Integer backupsId;

    @ApiModelProperty("存档id")
    private Integer archivedId;

    @ApiModelProperty("标准方法验证id")
    private Integer verifyId;

    @ExcelProperty(value = {"标准名称", "标准名称"}, index = 1)
    @ApiModelProperty("标准名称")
    private String methodName;

    @ExcelProperty(value = {"标准号", "标准号"}, index = 2)
    @ApiModelProperty("标准号")
    private String standardNo;

    @ExcelProperty(value = {"文件编号", "文件编号"}, index = 3)
    @ApiModelProperty("文件编号")
    private String fileNo;

    @ApiModelProperty("是否是新标准, 0否,1是")
    private Integer isNewStandard;

    @ExcelProperty(value = {"新标准名称", "新标准名称"}, index = 5)
    @ApiModelProperty("新标准名称")
    private String newMethodName;

    @ExcelProperty(value = {"新标准号", "新标准号"}, index =6)
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

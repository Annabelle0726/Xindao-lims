package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 质量监控计划详情评价附件表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Data
@TableName("cnas_quality_monitor_details_evaluate_file")
@ApiModel(value = "QualityMonitorDetailsEvaluateFile对象", description = "质量监控计划详情评价附件表")
public class QualityMonitorDetailsEvaluateFile {

    @TableId(type = IdType.AUTO)
    private Integer evaluateFileId;

    @ApiModelProperty("评价id")
    private Integer detailsEvaluateId;

    @ApiModelProperty("类型:1图片/2文件")
    private Integer type;

    @ApiModelProperty("附件路径")
    private String fileUrl;

    @ApiModelProperty("附件名称")
    private String fileName;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}


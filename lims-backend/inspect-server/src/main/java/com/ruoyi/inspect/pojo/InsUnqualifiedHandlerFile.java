package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 不合格处理附件记录表
 * @TableName ins_unqualified_handler_file
 */
@TableName(value ="ins_unqualified_handler_file")
@Data
public class InsUnqualifiedHandlerFile implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 不合格处理id
     */
    @ApiModelProperty(value = "不合格处理id")
    private Long unqualifiedId;

    /**
     * 文件类型(1:图片 2:文件)
     */
    @ApiModelProperty(value = "文件类型(1:图片 2:文件)")
    private Integer type;

    /**
     * 文件上传路径
     */
    @ApiModelProperty(value = "文件上传路径")
    private String fileUrl;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill= FieldFill.INSERT)
    private Integer createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

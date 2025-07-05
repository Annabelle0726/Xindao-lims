package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 内审管理纠正措施附件表
 * </p>
 *
 * @author 
 * @since 2024-11-13 04:00:38
 */
@Data
@TableName("cnas_internal_correct_file")
@ApiModel(value = "InternalCorrectFile对象", description = "内审管理纠正措施附件表")
public class InternalCorrectFile {

    @TableId(type = IdType.AUTO)
    private Integer correctFileId;

    @ApiModelProperty("内审纠正措施id")
    private Integer correctId;

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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件定期审查记录
 * </p>
 *
 * @author
 * @since 2024-11-13 10:54:31
 */
@Getter
@Setter
@TableName("cnas_manage_record_intervals")
@ApiModel(value = "ManageRecordIntervals对象", description = "文件定期审查记录")
public class ManageRecordIntervals  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")
    private String documentName;

    @ApiModelProperty("文件编号")
    private String documentCode;

    @ApiModelProperty("版本号")
    private String documentVersion;

    @ApiModelProperty("修订号")
    private String revision;

    @ApiModelProperty("适宜性")
    private String suitability;

    @ApiModelProperty("备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("外键关联,外来文件确认历史记录id")
    private Integer recordIntervalsTotalId;
}

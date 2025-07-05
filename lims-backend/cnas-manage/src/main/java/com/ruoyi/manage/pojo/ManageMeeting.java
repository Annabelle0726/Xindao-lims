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
 *
 * </p>
 *
 * @author
 * @since 2024-11-11 09:33:47
 */
@Getter
@Setter
@TableName("cnas_manage_meeting")
@ApiModel(value = "ManageMeeting对象", description = "")
public class ManageMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("会议时间")
    private LocalDateTime meetingTime;

    @ApiModelProperty("会议地点")
    private String place;

    @ApiModelProperty("主持人")
    private String compere;

    @ApiModelProperty("会议内容摘要")
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

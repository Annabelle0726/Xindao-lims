package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 内审会议表
 * </p>
 *
 * @author
 * @since 2024-11-12 02:50:44
 */
@Data
@TableName("cnas_internal_meeting")
@ApiModel(value = "InternalMeeting对象", description = "内审会议表")
public class InternalMeeting {

    @TableId(value = "meeting_id", type = IdType.AUTO)
    private Integer meetingId;

    @ApiModelProperty("时间")
    private String meetingDate;

    @ApiModelProperty("主持人")
    private String compere;

    @ApiModelProperty("地点")
    private String place;

    @ApiModelProperty("会议主题")
    private String subject;

    @ApiModelProperty("参加人员")
    private String participant;

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

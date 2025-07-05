package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 内审会议详情表
 * </p>
 *
 * @author
 * @since 2024-11-12 02:56:13
 */
@Data
@TableName("cnas_internal_meeting_detail")
@ApiModel(value = "InternalMeetingDetail对象", description = "内审会议详情表")
public class InternalMeetingDetail {

    @TableId(value = "meeting_detail_id", type = IdType.AUTO)
    private Integer meetingDetailId;

    @ApiModelProperty("会议主表id")
    private Integer meetingId;

    @ApiModelProperty("参加人员id")
    private Integer userId;

    @ApiModelProperty("参加人员")
    private String userName;

    @ApiModelProperty("部门")
    private String department;

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

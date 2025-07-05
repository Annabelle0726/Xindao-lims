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
 * @since 2024-11-11 09:34:27
 */
@Getter
@Setter
@TableName("cnas_manage_meeting_participants")
@ApiModel(value = "ManageMeetingParticipants对象", description = "")
public class ManageMeetingParticipants implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("参会人员")
    private Integer participants;

    @TableField(select = false,exist = false)
    private String userName;

    @ApiModelProperty("部门")
    private String department;

    @ApiModelProperty("会议id")
    private Integer meetingId;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

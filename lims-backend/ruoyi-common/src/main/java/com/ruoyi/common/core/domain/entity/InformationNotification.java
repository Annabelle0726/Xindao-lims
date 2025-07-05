package com.ruoyi.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息通知
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-04-23 02:14:30
 */
@Getter
@Setter
@TableName("information_notification")
@ApiModel(value = "InformationNotification对象", description = "消息通知")
public class InformationNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间/发送时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("待办、接收、审核、批准、预警提醒、通知公告")
    private String messageType;

    @ApiModelProperty("主题")
    private String theme;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("发送人Id")
    private Integer senderId;

    @ApiModelProperty("收件人")
    private Integer consigneeId;

    @ApiModelProperty("查看状态：已读，未读")
    private Boolean viewStatus;

    @ApiModelProperty("消息状态（拒绝、接收）（根据消息类型展示）")
    private Boolean messageStatus;

    @ApiModelProperty("跳转路径")
    private String jumpPath;

    @ApiModelProperty("跳转Id")
    private String jumpId;
}

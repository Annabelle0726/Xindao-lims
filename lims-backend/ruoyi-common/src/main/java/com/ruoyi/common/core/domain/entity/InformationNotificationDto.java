package com.ruoyi.common.core.domain.entity;

import lombok.Data;

@Data
public class InformationNotificationDto extends InformationNotification{
    private String senderUser;

    private String consigneeUser;
}

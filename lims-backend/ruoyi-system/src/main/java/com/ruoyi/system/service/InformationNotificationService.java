package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.InformationNotificationDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息通知 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-04-23 02:14:30
 */
public interface InformationNotificationService extends IService<InformationNotification> {

    IPage<InformationNotificationDto> getPage(Page page, String messageType);

    void markAllInformationReadOrDeleteAllReadMessages(Boolean isMarkAllInformationRead);

    Boolean checkForUnreadData();

    void triggerModificationStatusToRead(Integer id);

    int addInformationNotification(InformationNotification informationNotification);

    Map<String, Object> getNumberFourTypesMessagesHomePage();

    /**
     * 消息通知-滚动查询
     *
     * @return
     */
    IPage<InformationNotificationDto> msgRoll(Page page);
}

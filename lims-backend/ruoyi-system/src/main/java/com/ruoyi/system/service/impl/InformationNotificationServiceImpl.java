package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.InformationNotificationDto;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.InformationNotificationMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息通知 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-04-23 02:14:30
 */
@Service
public class InformationNotificationServiceImpl extends ServiceImpl<InformationNotificationMapper, InformationNotification> implements InformationNotificationService {

    @Override
    public IPage<InformationNotificationDto> getPage(Page page, String messageType) {
        return baseMapper.getPage(page, messageType, SecurityUtils.getUserId().intValue());
    }

    @Override
    public void markAllInformationReadOrDeleteAllReadMessages(Boolean isMarkAllInformationRead) {
        if (isMarkAllInformationRead) {
            baseMapper.update(new InformationNotification(), Wrappers.<InformationNotification>lambdaUpdate()
                    .set(InformationNotification::getViewStatus, true)
                    .eq(InformationNotification::getConsigneeId, SecurityUtils.getUserId().intValue()));
        } else {
            baseMapper.delete(Wrappers.<InformationNotification>lambdaUpdate()
                    .eq(InformationNotification::getConsigneeId, SecurityUtils.getUserId().intValue())
                    .eq(InformationNotification::getViewStatus, true));
        }
    }

    @Override
    public Boolean checkForUnreadData() {
        Map<String, Integer> map1 = null;
        try {
            List<InformationNotification> informationNotifications = baseMapper.selectList(
                    Wrappers.<InformationNotification>lambdaQuery()
                            .eq(InformationNotification::getConsigneeId, SecurityUtils.getUserId().intValue())
                            .eq(InformationNotification::getViewStatus, false)
                            .last("limit 1"));
            return !informationNotifications.isEmpty();
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void triggerModificationStatusToRead(Integer id) {
        baseMapper.update(new InformationNotification(), Wrappers.<InformationNotification>lambdaUpdate()
                .eq(InformationNotification::getId, id)
                .set(InformationNotification::getViewStatus, true));
    }

    @Override
    public int addInformationNotification(InformationNotification informationNotification) {
        informationNotification.setViewStatus(false);
        return baseMapper.insert(informationNotification);
    }

    @Override
    public Map<String, Object> getNumberFourTypesMessagesHomePage() {
        Map<String, Object> map = new HashMap<>();
        int userId = SecurityUtils.getUserId().intValue();
        Long totalNumberOfMessages = baseMapper.selectCount(Wrappers.<InformationNotification>lambdaQuery()
                .eq(InformationNotification::getConsigneeId, userId));
        Long totalNumberOfReadMessages = baseMapper.selectCount(Wrappers.<InformationNotification>lambdaQuery()
                .eq(InformationNotification::getConsigneeId, userId)
                .eq(InformationNotification::getViewStatus, true));
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);
        Long totalNumberOfMessagesInThePastSevenDays = baseMapper.selectCount(Wrappers.<InformationNotification>lambdaQuery()
                .eq(InformationNotification::getConsigneeId, userId)
                .between(InformationNotification::getCreateTime, today, sevenDaysAgo));
        long remainingToDo = totalNumberOfMessages - totalNumberOfReadMessages;
        map.put("totalNumberOfMessages", totalNumberOfMessages);
        map.put("remainingToDo", remainingToDo);
        map.put("totalNumberOfReadMessages", totalNumberOfReadMessages);
        map.put("totalNumberOfMessagesInThePastSevenDays", totalNumberOfMessagesInThePastSevenDays);
        return map;
    }

    /**
     * 消息通知-滚动查询
     *
     * @return
     */
    @Override
    public IPage<InformationNotificationDto> msgRoll(Page page) {
        return baseMapper.msgRoll(page,SecurityUtils.getUserId().intValue());
    }
}

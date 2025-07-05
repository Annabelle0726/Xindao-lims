package com.ruoyi.personnel.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.personnel.mapper.PersonTrainingDetailedMapper;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 培训计划使用提醒记录提醒
 */
@Component
public class PersonTrainingSchedule {
    @Resource
    private PersonTrainingDetailedMapper personTrainingDetailedMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 提醒填写人员培训记录
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 9 1,15 * *") // 每月一号执行
    public void task1() {
        // 查询当月培训计划
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M");
        String format = LocalDateTime.now().format(formatter);
        List<PersonTrainingDetailed> personTrainingDetaileds = personTrainingDetailedMapper.selectList(Wrappers.<PersonTrainingDetailed>lambdaQuery()
                .eq(PersonTrainingDetailed::getTrainingDate, format));

        for (PersonTrainingDetailed personTrainingDetailed : personTrainingDetaileds) {
            threadPoolTaskExecutor.execute(() -> {
                // 查询培训讲师
                User user = userMapper.selectById(personTrainingDetailed.getTrainingLecturerId());
                // 企业微信通知培训
                String message = "";
                message += "人员培训计划提醒通知";
                message += "\n培训目标: " + personTrainingDetailed.getTrainingObjectives();
                message += "\n培训内容: " + personTrainingDetailed.getTrainingContent();
                message += "\n参加对象: " + personTrainingDetailed.getParticipants();
                message += "\n培训日期: " + personTrainingDetailed.getTrainingDate();
                message += "\n计划当月进行培训";

                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(user.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}

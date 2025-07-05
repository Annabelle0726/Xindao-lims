package com.ruoyi.process.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.process.mapper.QualitySuperviseDetailsMapper;
import com.ruoyi.process.mapper.QualitySuperviseDetailsRecordMapper;
import com.ruoyi.process.mapper.QualitySuperviseManagementReviewMapper;
import com.ruoyi.process.pojo.QualitySuperviseDetails;
import com.ruoyi.process.pojo.QualitySuperviseDetailsRecord;
import com.ruoyi.process.pojo.QualitySuperviseManagementReview;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 质量监督填写提醒
 */
@Component
public class QualitySuperviseSchedule {
    @Resource
    private QualitySuperviseDetailsMapper qualitySuperviseDetailsMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private QualitySuperviseManagementReviewMapper qualitySuperviseManagementReviewMapper;
    @Resource
    private QualitySuperviseDetailsRecordMapper qualitySuperviseDetailsRecordMapper;

    /**
     * 质量监督填写提醒
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 9 1 * *") // 每月1号执行
    public void task1() {
        // 查询当月监督计划
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M");
        String format = LocalDateTime.now().format(formatter);
        List<QualitySuperviseDetails> qualitySuperviseDetails = qualitySuperviseDetailsMapper.selectList(Wrappers.<QualitySuperviseDetails>lambdaQuery()
                .eq(QualitySuperviseDetails::getSuperviseTime, format));

        for (QualitySuperviseDetails qualitySuperviseDetail : qualitySuperviseDetails) {
            threadPoolTaskExecutor.execute(() -> {
                // 查询被监督人信息
                User user = userMapper.selectById(qualitySuperviseDetail.getSupervisedUserId());
                // 企业微信通知培训
                String message = "";
                message += "质量监督计划提醒通知";
                message += "\n监督日期: " + qualitySuperviseDetail.getSuperviseTime();
                message += "\n监督目的: " + qualitySuperviseDetail.getSupervisePurpose();
                message += "\n监督项目: " + qualitySuperviseDetail.getSuperviseProject();
                message += "\n被监督人: " + qualitySuperviseDetail.getSupervisee();
                message += "\n计划当月进行监督计划";

                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(user.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    /**
     * 每月28号自动生成质量监督总结
     */
//    @Scheduled(cron = "0/5 * * * * *")
    @Scheduled(cron = "0 0 9 28 * *") // 每月28号执行
    public void task2() {
        LocalDateTime now = LocalDateTime.now();
        // 查询当月监督计划
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M");
        String format = now.format(formatter);
        List<QualitySuperviseDetails> qualitySuperviseDetails = qualitySuperviseDetailsMapper.selectList(Wrappers.<QualitySuperviseDetails>lambdaQuery()
                .eq(QualitySuperviseDetails::getSuperviseTime, format));

        // 根据定期监督和动态监督区分
        Map<String, List<QualitySuperviseDetails>> superviseReasonMap = qualitySuperviseDetails.stream().collect(Collectors.groupingBy(QualitySuperviseDetails::getSuperviseReason));

        // 获取当月日期
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy年MM月");
        String format2 = now.format(formatter2);

        // 获取计划内容
        String implementationSupervisee = "";
                List<QualitySuperviseDetails> implementationList = superviseReasonMap.get("定期监督");
        if (CollectionUtils.isNotEmpty(implementationList)) {
            // 获取定期监督人员信息
            List<String> collect = implementationList.stream().map(QualitySuperviseDetails::getSupervisee).collect(Collectors.toList());
            implementationSupervisee = CollUtil.join(collect, ",");
        }
        String implementationContent = "工作中严格按照年度质量监督工作计划进行执行。{}对{}进行一次定期监督。在监督过程中，没有发现不符合的现象。检验员能熟练的按照规定的要求进行设备的操作，对于标准能熟记，所使用的设备均在校准周期内，能清楚完整的的记录原始数据并出具报告；在试验环境方面，能够严格的按照标准执行。";
        String formatImplementationContent = StrUtil.format(implementationContent, format2, implementationSupervisee);

        //获取动态内容
        String dynamicSupervisee = "";
        List<QualitySuperviseDetails> dynamicList = superviseReasonMap.get("动态监督");
        if (CollectionUtils.isNotEmpty(dynamicList)) {
            List<String> collect = dynamicList.stream()
                    .map(details ->  StrUtil.format("对{}动态监督1次在监督过程中", details.getSupervisee())).collect(Collectors.toList());
            dynamicSupervisee = CollUtil.join(collect, "；");
        }
        String dynamicContent = "在保证质量监督计划的同时，也坚持按照领导要求加强不定期监督动作。{}份{}，没有发现不符合的现象。检验员能熟练的按照规定的要求进行设备的操作，对于标准能熟记，所使用的设备均在校准周期内，能清楚完整的的记录原始数据并出具报告；在试验环境方面，能够严格的按照标准执行。";
        String formatdynamicContent = StrUtil.format(dynamicContent, format2, dynamicSupervisee);

        // 获取监督员信息
        QualitySuperviseDetails superviseDetails = qualitySuperviseDetails.get(0);
        QualitySuperviseDetailsRecord detailsRecord = qualitySuperviseDetailsRecordMapper.selectOne(Wrappers.<QualitySuperviseDetailsRecord>lambdaQuery()
                .eq(QualitySuperviseDetailsRecord::getSuperviseDetailsId, superviseDetails.getSuperviseDetailsId())
                .last("limit 1"));
        String supervisor = detailsRecord.getSupervisor();

        // 创建评审对象
        QualitySuperviseManagementReview managementReview = new QualitySuperviseManagementReview();
        String yearDate = now.format(DateTimeFormatter.ofPattern("yyyy年"));
        String monthDate = now.format(DateTimeFormatter.ofPattern("MM月"));
        managementReview.setFileName(StrUtil.format("{}管理评审输入材料（{}）_{}", yearDate, supervisor, monthDate));
        managementReview.setImplementationContent(formatImplementationContent);
        managementReview.setDynamicContent(formatdynamicContent);
        managementReview.setSupervisor(supervisor);
        managementReview.setSuperviseDate(LocalDate.now());
        qualitySuperviseManagementReviewMapper.insert(managementReview);
    }

}

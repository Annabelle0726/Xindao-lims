package com.ruoyi.inspect.task;

import com.ruoyi.inspect.service.InsOrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author zhuo
 * @Date 2025/2/20
 */
@Component
public class RawMaterIalSchedule {

    @Resource
    private InsOrderService insOrderService;

    /**
     * 定时任务获取采购订单
     */
    @Scheduled(fixedDelay = 1200000)
    public void getIfsOrderTiming() {
        insOrderService.getIfsOrder();
    }
}

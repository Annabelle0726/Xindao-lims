package com.ruoyi.manage.schedule;

import com.ruoyi.manage.pojo.ManageRecordIntervalsTotal;
import com.ruoyi.manage.pojo.ManageRecordTotal;
import com.ruoyi.manage.service.ManageRecordIntervalsTotalService;
import com.ruoyi.manage.service.ManageRecordTotalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ManageRecordTotalSchedule {

    @Resource
    private ManageRecordTotalService manageRecordTotalService;

    @Resource
    private ManageRecordIntervalsTotalService manageRecordIntervalsTotalService;

    @Scheduled(cron = "0 0 2 1 1 ?") //每年1月1号2点
    public void manageRecordTotal() {
        ManageRecordTotal manageRecordTotal = new ManageRecordTotal();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        String formattedDate = sdf.format(calendar.getTime());
        manageRecordTotal.setYear(formattedDate);
        manageRecordTotalService.save(manageRecordTotal);

        ManageRecordIntervalsTotal manageRecordIntervalsTotal = new ManageRecordIntervalsTotal();
        manageRecordIntervalsTotal.setYear(formattedDate);
        manageRecordIntervalsTotalService.save(manageRecordIntervalsTotal);
    }
}

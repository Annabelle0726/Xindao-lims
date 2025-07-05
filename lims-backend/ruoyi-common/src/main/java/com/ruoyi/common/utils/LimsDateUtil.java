package com.ruoyi.common.utils;

import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;

/**
 * @Author zhuo
 * @Date 2024/9/28
 */
public class LimsDateUtil {


    /**
     * 日期格式到26号月份加一
     * @param localDateTime
     * @return
     */
    public static String resetDate(LocalDateTime localDateTime) {
        // 获取当前日
        int dayOfMonth = localDateTime.getDayOfMonth();

        // 判断是否是26日及以上
        if (dayOfMonth >= 26) {
            // 月份加1
            localDateTime = localDateTime.plusMonths(1);
        }

        // 格式化为"yyMM"格式
        String formattedDate = DateUtil.format(localDateTime, "yyMM");
        return formattedDate;
    }
}

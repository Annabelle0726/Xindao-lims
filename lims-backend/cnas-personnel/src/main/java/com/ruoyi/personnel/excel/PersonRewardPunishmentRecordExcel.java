package com.ruoyi.personnel.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PersonRewardPunishmentRecordExcel {
    @ExcelProperty("员工编号")
    private String account;

    @ExcelProperty("姓名")
    private String userName;

    @ExcelProperty("奖惩级别")
    private String rewardPunishLevel;

    @ExcelProperty("奖惩名称")
    private String rewardPunishName;

    @ExcelProperty("奖惩时间")
    private String rewardPunishTime;

    @ExcelProperty("奖惩单位")
    private String rewardPunishWorkUnit;

    @ExcelProperty("奖惩内容")
    private String rewardPunishContent;

    @ExcelProperty("创建时间")
    private String createTime;

    @ExcelProperty("创建人")
    private String createUserName;
}

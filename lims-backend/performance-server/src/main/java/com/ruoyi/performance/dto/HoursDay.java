package com.ruoyi.performance.dto;

import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;
import lombok.Data;

import java.util.List;

@Data
public class HoursDay {

    // 操作, 1通过, 2退回
    private String operation;

    private List<AuxiliaryWorkingHoursDay> auxiliaryWorkingHoursDays;
}

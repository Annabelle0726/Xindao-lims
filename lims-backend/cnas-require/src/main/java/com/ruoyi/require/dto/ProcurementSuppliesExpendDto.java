package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.ProcurementSuppliesExpends;
import lombok.Data;

@Data
public class ProcurementSuppliesExpendDto extends ProcurementSuppliesExpends {
    private String listName; // 项目耗材名称
    private String enterUserName; // 录入人名称
    private String updateUserName; // 更新人名称
}

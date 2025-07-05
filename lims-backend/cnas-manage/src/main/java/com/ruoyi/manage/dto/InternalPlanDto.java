package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.InternalPlan;
import com.ruoyi.manage.pojo.InternalPlanDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/13
 */
@Data
public class InternalPlanDto extends InternalPlan {

    @ApiModelProperty("计划详情")
    private List<InternalPlanDetail> planDetailList;
}

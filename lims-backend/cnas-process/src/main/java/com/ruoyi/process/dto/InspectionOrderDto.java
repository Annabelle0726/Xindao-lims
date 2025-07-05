package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.InspectionOrder;
import com.ruoyi.process.pojo.InspectionOrderDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/12/9
 */
@Data
public class InspectionOrderDto extends InspectionOrder {

    @ApiModelProperty("委托检验单详情")
    private List<InspectionOrderDetail> orderDetailList;
}

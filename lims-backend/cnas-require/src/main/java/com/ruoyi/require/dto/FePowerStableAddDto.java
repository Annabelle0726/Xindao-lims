package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FeMeasuredQuantity;
import com.ruoyi.require.pojo.FePowerStable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FePowerStableAddDto extends FePowerStable {

    @ApiModelProperty("设施和环境条件要求-电源稳定性-测定量")
    private List<FeMeasuredQuantity> feMeasuredQuantityList;
}

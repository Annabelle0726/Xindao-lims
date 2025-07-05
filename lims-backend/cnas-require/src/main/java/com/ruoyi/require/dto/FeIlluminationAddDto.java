package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.FeIllumination;
import com.ruoyi.require.pojo.FeIlluminationDetectionArea;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FeIlluminationAddDto extends FeIllumination {

    @ApiModelProperty("设施和环境条件要求-照度记录表-检测区域")
    private List<FeIlluminationDetectionArea> illuminationDetectionAreaList;
}

package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.InconsistentDistribution;
import com.ruoyi.process.pojo.InconsistentDistributionDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/15
 */
@Data
public class InconsistentDistributionDto extends InconsistentDistribution {

    @ApiModelProperty("分布详情")
    private List<InconsistentDistributionDetail> distributionDetailList;

    @ApiModelProperty("占比对象")
    private InconsistentDistributionProportionDto distributionProportion;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("修改人名称")
    private String updateUserName;
}

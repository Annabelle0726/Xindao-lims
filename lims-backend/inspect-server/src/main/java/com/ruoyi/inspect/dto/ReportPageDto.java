package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.InsReport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReportPageDto extends InsReport implements Serializable {

    @ApiModelProperty(value = "委托编号")
    private String entrustCode;

    @ApiModelProperty(value = "提交人")
    private String writeUserName;

    @ApiModelProperty(value = "批准人")
    private String ratifyUser;

    @ApiModelProperty(value = "审核人")
    private String examineUser;

    @ApiModelProperty(value = "下单类别")
    private Integer typeSource;

    @ApiModelProperty(value = "检验类别")
    private String orderType;

    @ApiModelProperty(value = "原材料id")
    private Integer ifsInventoryId;

    @ApiModelProperty(value = "是否复核通过")
    private String queryStatus;

    @ApiModelProperty(value = "是否是铜材, 0否, 1是")
    private Integer isCopper;

    @ApiModelProperty(value = "实验室")
    private String sonLaboratory;

    @ApiModelProperty(value = "只看自己标识")
    private Integer createOrderUser;

}

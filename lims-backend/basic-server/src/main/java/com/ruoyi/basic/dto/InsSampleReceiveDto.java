package com.ruoyi.basic.dto;

import com.ruoyi.basic.pojo.InsSample1;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/10/29
 */
@Data
public class InsSampleReceiveDto extends InsSample1 {

    @ApiModelProperty("零件号")
    private String partNo;

    @ApiModelProperty("原材料id")
    private Integer ifsInventoryId;

    @ApiModelProperty("芯数")
    private String cores;

    @ApiModelProperty("型号参数")
    private String modelNum;

    @ApiModelProperty("导体材质")
    private String conductorMaterial;

    @ApiModelProperty("导体类型")
    private String conductorType;

    @ApiModelProperty("是否是电缆配置")
    private String isCableTag;

}

package com.ruoyi.basic.dto;

import com.ruoyi.basic.pojo.StructureTestObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 戴卓
 * @Date 2024/2/26
 */
@Data
public class PageTestObjectDto extends StructureTestObject {

    @ApiModelProperty(value = "产品")
    private String product;

    @ApiModelProperty(value = "创建人")
    private String createUserName;

    @ApiModelProperty(value = "更新人")
    private String updateUserName;

    @ApiModelProperty(value = "零件号")
    private String partNo;
}

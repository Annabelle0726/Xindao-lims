package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonBasicInfoWork;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/1/14
 */
@Data
public class PersonBasicInfoWorkDto extends PersonBasicInfoWork {

    @ApiModelProperty("填充使用")
    private String fill;
}

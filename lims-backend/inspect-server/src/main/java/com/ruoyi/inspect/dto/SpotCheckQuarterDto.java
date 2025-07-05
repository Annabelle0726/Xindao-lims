package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.SpotCheckQuarter;
import com.ruoyi.inspect.pojo.SpotCheckQuarterItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/10/10
 */
@Data
public class SpotCheckQuarterDto extends SpotCheckQuarter {

    @ApiModelProperty("季度抽检详情")
    private List<SpotCheckQuarterItem> quarterItems;

    @ApiModelProperty("创建人")
    private String createUserName;
}

package com.ruoyi.inspect.dto;

import com.ruoyi.inspect.pojo.SpotCheckYear;
import com.ruoyi.inspect.pojo.SpotCheckYearItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/10/10
 */
@Data
public class SpotCheckYearDto extends SpotCheckYear {

    @ApiModelProperty("年度抽样详情")
    private List<SpotCheckYearItem> yearItems;

    @ApiModelProperty("创建人")
    private String createUserName;
}

package com.ruoyi.process.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.process.pojo.ProcessDeal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/4/23
 */
@Data
public class ProcessDealDto extends ProcessDeal {
    @ApiModelProperty("序号")
    private String indexs;

    @ApiModelProperty("时间")
    private String dealTimeStr;
}

package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数字电桥采集
 *
 * @author zhuo
 * @since 2025-02-19
 */
@Data
@TableName("collect_bridge")
public class CollectBridge {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("委托编号")
    private String entrustCode;

    @ApiModelProperty("采集值")
    private String collectValue;

    @ApiModelProperty("时间")
    private LocalDateTime collectDate;

}


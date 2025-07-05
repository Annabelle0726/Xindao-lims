package com.ruoyi.performance.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 绩效管理-班次
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-08 09:12:04
 */
@Data
@TableName("performance_shift")
@ApiModel(value = "PerformanceShift对象", description = "绩效管理-班次")
public class PerformanceShift  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("班次")
    private String shift;

    @ApiModelProperty("员工id")
    private Integer userId;

    @ApiModelProperty("排班日期")
    private LocalDateTime workTime;

    @ApiModelProperty("创建人Id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

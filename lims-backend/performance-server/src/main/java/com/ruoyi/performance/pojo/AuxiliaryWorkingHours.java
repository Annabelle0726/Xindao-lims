package com.ruoyi.performance.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 辅助工时
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-09 06:58:31
 */
@Getter
@Setter
@TableName("auxiliary_working_hours")
@ApiModel(value = "AuxiliaryWorkingHours对象", description = "辅助工时")
public class AuxiliaryWorkingHours  implements Serializable {
    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("编号")
    private String number;
    @ApiModelProperty("辅助项目名称")
    private String auxiliaryProject;
    @ApiModelProperty("核准工时")
    private BigDecimal approvedWorkingHour;
    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("部门")
    private String department;
    @ApiModelProperty("实验室")
    private String laboratory;
    @ApiModelProperty("单位")
    private String unit;
}

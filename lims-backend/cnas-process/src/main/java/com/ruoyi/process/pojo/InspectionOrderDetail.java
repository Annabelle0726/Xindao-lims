package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 检验委托单详情表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-09
 */
@Getter
@Setter
@TableName("cnas_inspection_order_detail")
@ApiModel(value = "InspectionOrderDetail对象", description = "检验委托单详情表")
public class InspectionOrderDetail {

    @TableId(value = "inspection_order_detail_id", type = IdType.AUTO)
    private Integer inspectionOrderDetailId;

    @ApiModelProperty("委托单id")
    private Integer inspectionOrderId;

    @ApiModelProperty("样品编号")
    private String sampleNumber;

    @ApiModelProperty("试验项目")
    private String testItem;

    @ApiModelProperty("试验依据")
    private String testStandard;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 导出使用
    @TableField(select = false, exist = false)
    private Integer index;
}

package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * cnas设备使用记录表(7.1检验委托单)
 * </p>
 *
 * @author
 * @since 2025-04-17 03:51:48
 */
@Getter
@Setter
@TableName("cnas_process_order_device")
@ApiModel(value = "ProcessOrderDevice对象", description = "cnas设备使用记录表(7.1检验委托单)")
public class ProcessOrderDevice implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deviceId;

    @ApiModelProperty("委托单id")
    private Integer inspectionOrderId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("使用前0代表不正常1代表正常")
    private Integer useBefore;

    @ApiModelProperty("使用后0代表不正常1代表正常")
    private Integer useAfter;

    @ApiModelProperty("异常情况")
    private String abnormal;

    @ApiModelProperty("使用开始日期")
    private LocalDateTime useStartDate;

    @ApiModelProperty("使用人id")
    private Integer usePersonId;

    @ApiModelProperty("使用人")
    private String usePerson;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("使用结束时间")
    private LocalDateTime useEndDate;

    @ApiModelProperty("设备名称")
    @TableField(select = false,exist = false)
    private String deviceName;
    @ApiModelProperty("设备编号")
    @TableField(select = false,exist = false)
    private String managementNumber;
}

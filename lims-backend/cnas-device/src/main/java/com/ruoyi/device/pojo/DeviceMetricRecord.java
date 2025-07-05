package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 设备校准 - 校准记录
 * </p>
 *
 * @author
 * @since 2024-09-27 10:20:01
 */
@Getter
@Setter
@TableName("device_metric_record")
@ApiModel(value = "DeviceMetricRecord对象", description = "设备校准 - 校准记录")
public class DeviceMetricRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("记录编号")
    private String processNumber;

    @ApiModelProperty("计量单位")
    private String unitOfMeasure;

    @ApiModelProperty("校准日期")
    private Date calibrationDate;

    @ApiModelProperty("下次校准日期")
    private Date nextCalibrationDate;

    @ApiModelProperty("计算器具")
    private String calculatingApparatus;

    @ApiModelProperty("计算标准量程")
    private String standardRange;

    @ApiModelProperty("计量标准不确定度")
    private String calibrationStandardUncertainty;

    @ApiModelProperty("依据文件")
    private String byDocument;

    @ApiModelProperty("证书编号")
    private String certificateSerialNumber;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("原文件名称")
    private String fileName;

    @ApiModelProperty("系统生成文件名称")
    private String systemFileName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("创建时间 / 登记日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("登记人")
    private String createUser;

    @ApiModelProperty("calibrate：校准；examine：核查")
    private String type;

    @ApiModelProperty("确认时间")
    private Date confirmDate;
}

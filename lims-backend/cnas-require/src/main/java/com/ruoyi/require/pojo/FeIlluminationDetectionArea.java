package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-照度记录表-检测区域
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:16:28
 */
@Getter
@Setter
@TableName("cnas_fe_illumination_detection_area")
@ApiModel(value = "FeIlluminationDetectionArea对象", description = "设施和环境条件-设施和环境条件要求-照度记录表-检测区域")
public class FeIlluminationDetectionArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("检测区域id")
    @TableId(value = "detection_area_id", type = IdType.AUTO)
    private Integer detectionAreaId;

    @ApiModelProperty("检测区域名称")
    private String detectionAreaLabel;

    @ApiModelProperty("检测值-第一次")
    private Integer valueOne;

    @ApiModelProperty("检测值-第二次")
    private Integer valueTwo;

    @ApiModelProperty("检测值-第三次")
    private Integer valueThree;

    @ApiModelProperty("平均值")
    private Integer average;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("照度记录表id")
    private Integer intensityIlluminationId;

    // 导出使用
    @TableField(select = false, exist = false)
    @ApiModelProperty("序号(导出使用)")
    private Integer index;
}

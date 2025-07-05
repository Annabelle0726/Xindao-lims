package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-13 12:23:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("device_data_config")
@ApiModel(value = "DataConfig对象", description = "")
public class DataConfig implements Serializable {

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("公式")
    private String formula;

    @ApiModelProperty("参照X")
    private String referx;

    @ApiModelProperty("参照Y")
    private String refery;

    @ApiModelProperty("x")
    private String x;

    @ApiModelProperty("y")
    private String y;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty("检验项目")
    private String inspectionItem;

    @ApiModelProperty("检验项子项")
    private String inspectionItemSubclass;

    @TableField(exist = false)
    @ApiModelProperty("检验项子项")
    private String insProductItem;

    @ApiModelProperty("检验项id")
    private Integer structureItemParameterId;

    @ApiModelProperty("序号")
    private String serialNumber;

    @ApiModelProperty("别名")
    private String anotherName;

    @ApiModelProperty("匹配名称")
    private String matchingName;

    @TableField(select = false, exist = false)
    private Boolean isDevice;

    @TableField(select = false, exist = false)
    private String sample;

}

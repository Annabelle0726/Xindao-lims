package com.ruoyi.device.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceConfigDtoPage{

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "文件后缀")
    private String fileType;

    @ApiModelProperty(value = "采集地址")
    private String collectUrl;

    @ApiModelProperty(value = "存储地址")
    private String storageUrl;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty(value = "检验项")
    private String inspectionItem;

    @ApiModelProperty(value = "检验项子项")
    private String inspectionItemSubclass;

    @ApiModelProperty(value = "公式")
    private String formula;

    @ApiModelProperty(value = "参照X")
    private String referx;

    @ApiModelProperty(value = "X")
    private String x;

    @ApiModelProperty(value = "参照Y")
    private String refery;


    @ApiModelProperty(value = "Y")
    private String y;

    @ApiModelProperty(value = "别名")
    private String anotherName;

    @ApiModelProperty(value = "匹配名称")
    private String matchingName;

    private Integer id;

    @ApiModelProperty(value = "检验对象")
    private String sample;

    @ApiModelProperty(value = "检验项目id")
    private Integer structureItemParameterId;

    @ApiModelProperty("数采-委托字段")
    private String entrustCode;

    @ApiModelProperty("数采-样品字段")
    private String sampleCode;

    @ApiModelProperty("数采-db，mdb文件名称")
    private String dbFileName;
}

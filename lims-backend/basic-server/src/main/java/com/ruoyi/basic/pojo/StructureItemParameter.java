package com.ruoyi.basic.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检验项目参数(StructureItemParameter)表对象
 *
 * @author makejava
 * @since 2024-02-26 16:21:17
 */
@TableName(value ="structure_item_parameter")
@Data
public class StructureItemParameter implements Serializable {
    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "检验项")
    private String inspectionItem;

    @ApiModelProperty(value = "检验项EN")
    private String inspectionItemEn;

    @ApiModelProperty(value = "检验子项")
    private String inspectionItemSubclass;

    @ApiModelProperty(value = "检验子项EN")
    private String inspectionItemSubclassEn;

    @ApiModelProperty(value = "检验对象")
    private String sample;

    @ApiModelProperty(value = "单价(元)")
    private String price;

    @ApiModelProperty(value = "场所")
    private String laboratory;

    @ApiModelProperty(value = "试验室")
    private String sonLaboratory;

    @ApiModelProperty(value = "要求描述")
    private String askTell;

    @ApiModelProperty(value = "要求值")
    private String ask;

    @ApiModelProperty(value = "计量单位")
    private String unit;

    @ApiModelProperty(value = "试验方法")
    private String method;

    @ApiModelProperty(value = "工时(H)")
    private Double manHour;

    @ApiModelProperty(value = "预计时间(H)")
    private Integer manDay;

    @ApiModelProperty(value = "工时分组")
    private String manHourGroup;

    @ApiModelProperty(value = "检验项类型")
    private String inspectionItemType;

    @ApiModelProperty(value = "检验值类型")
    private String inspectionValueType;

    @ApiModelProperty(value = "检验次数")
    private Integer checkoutNumber;

    @ApiModelProperty(value = "区间")
    private String section;

    @ApiModelProperty(value = "特殊标识")
    private String bsm;

    @ApiModelProperty(value = "原始记录模板")
    private Integer templateId;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "字典类型")
    private String dic;

    @ApiModelProperty(value = "检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty(value = "检验项分类EN")
    private String inspectionItemClassEn;

    @ApiModelProperty(value = "条件")
    private String radiusList;

    @ApiModelProperty(value = "收费标准(元/次)")
    private String rates;

    @ApiModelProperty(value = "设备id")
    private String deviceIds;

    @ApiModelProperty(value = "抽检类型, 1:月度, 2:季度, 3:年度")
    private String spotCheckType;

}


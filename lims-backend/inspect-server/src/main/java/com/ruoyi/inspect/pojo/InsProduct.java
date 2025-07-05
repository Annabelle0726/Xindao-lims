package com.ruoyi.inspect.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 检验项目
 *
 * @TableName ins_product
 */
@TableName(value = "ins_product")
@Data
public class InsProduct implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("项目参数id")
    private Integer structureItemParameterId;

    @TableField(exist = false,select = false)
    private String sampleCode;

    @ApiModelProperty("检验项")
    private String inspectionItem;

    @ApiModelProperty("检验项英文")
    private String inspectionItemEn;

    @ApiModelProperty("检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty("检验项分类EN")
    private String inspectionItemClassEn;

    @ApiModelProperty("检验项子类")
    private String inspectionItemSubclass;

    @ApiModelProperty("检验项子类英文")
    private String inspectionItemSubclassEn;

    @ApiModelProperty("工厂")
    private String factory;

    @ApiModelProperty("实验室")
    private String laboratory;

    @ApiModelProperty("样品分类")
    private String sampleType;

    @ApiModelProperty("样品")
    private String sample;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("方法")
    private String methodS;

    @ApiModelProperty("子实验室")
    private String sonLaboratory;

    @ApiModelProperty("计量单位")
    private String unit;

    @ApiModelProperty("单价(元)")
    private BigDecimal price;

    @ApiModelProperty("工时(H)")
    private BigDecimal manHour;

    @ApiModelProperty("工时分组")
    private String manHourGroup;

    @ApiModelProperty("检验项类型")
    private String inspectionItemType;

    @ApiModelProperty("检验值类型")
    private String inspectionValueType;

    @ApiModelProperty("设备组")
    private String deviceGroup;

    @ApiModelProperty("检验次数")
    private Integer checkoutNumber;

    @ApiModelProperty("区间")
    private String section;

    @ApiModelProperty("取值类型")
    private String valueType;

    @ApiModelProperty("方法")
    private String method;

    @ApiModelProperty("预计时间(h)")
    private Integer manDay;

    @ApiModelProperty("特殊标识")
    private String bsm;

    @ApiModelProperty("要求值")
    private String ask;

    @ApiModelProperty("要求描述")
    private String tell;

    @ApiModelProperty("最终值")
    @TableField("`last_value`")
    private String lastValue;

    @ApiModelProperty("合格状态: 1：合格 0：不合格, 3不判定")
    private Integer insResult;


    @ApiModelProperty("1：有效 0：无效")
    private Integer state;

    @ApiModelProperty("外键：ins_sample表id")
    private Integer insSampleId;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("外键：standard_template表id")
    private Integer templateId;

    @TableField(exist = false)
    @ApiModelProperty("模板内容")
    private List<JSONObject> template;

    @TableField(exist = false)
    @ApiModelProperty("模板内容颜色")
    private Map<String, Object> style;

    @TableField(exist = false)
    @ApiModelProperty("检验结果对象")
    private InsProductResult insProductResult;

    @TableField(exist = false)
    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("字典类型")
    private String dic;

    //温度
    private String temperature;
    //湿度
    private String humidity;

    private Integer insFiberId;

    private Integer insFibersId;

    private Integer insBushId;

    @ApiModelProperty("标准方法id")
    private Integer standardMethodListId;

    //返回的20度常温的计算值(温度循环)
    @TableField(exist = false ,select = false)
    private String complue;

    @ApiModelProperty(value = "条件")
    private String radius;

    @ApiModelProperty(value = "收费标准(元/次)")
    private String rates;

    @ApiModelProperty(value = "电缆配置标识")
    private String cableTag;

    @ApiModelProperty(value = "原材料配置标识")
    private String rawMaterialTag;

    @ApiModelProperty(value = "重复标识(一个检验项可能会存在多次)")
    private String repetitionTag;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    // 绑定值不参加展示, 只参加计算
    @ApiModelProperty(value = "是否是绑定值, 0否, 1是")
    private Integer isBinding;

    @ApiModelProperty(value = "自关联(绑定id)")
    private Integer bindingProductId;

}

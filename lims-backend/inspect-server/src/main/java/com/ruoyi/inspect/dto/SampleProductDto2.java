package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

@Data
public class SampleProductDto2 implements Serializable {

    @ApiModelProperty("样品id")
    private Integer id;

    @ApiModelProperty("检验项id")
    private Integer insProductId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("样品名称")
    private String sample;

    @ApiModelProperty("检验项分类")
    private String inspectionItemClass;

    @ApiModelProperty("检验项分类英文")
    private String inspectionItemClassEN;

    @ApiModelProperty("检验项")
    private String inspectionItem;

    @ApiModelProperty("检验项英文")
    private String inspectionItemEn;

    @ApiModelProperty("检验子项")
    private String inspectionItemSubclass;

    @ApiModelProperty("检验子项英文")
    private String inspectionItemSubclassEn;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("样品型号")
    private String model;

    @ApiModelProperty("样品状态")
    private Integer insState;

    @ApiModelProperty("实验室")
    private String sonLaboratory;

    @ApiModelProperty("检验项类型")
    private String inspectionItemType;

    @ApiModelProperty("检验值类型")
    private String inspectionValueType;

    @ApiModelProperty("要求值")
    private String ask;

    @ApiModelProperty("试验要求")
    private String tell;

    @ApiModelProperty("检验结果")
    private String lastValue;

    @ApiModelProperty("结果判定")
    private Integer insResult;

    @ApiModelProperty("检验人")
    private String checkName;

    @ApiModelProperty("检验日期")
    private String checkTime;

    @ApiModelProperty("设备值")
    private String equipValue;

    @ApiModelProperty("序号")
    private String index;
    @ApiModelProperty("样品名称(整体)")
    private String inspectionName;

    @ApiModelProperty("条件")
    private String radius;

    @ApiModelProperty("检验结果列表")
    private List<String> lastValueList;

    @ApiModelProperty("检验结果列表")
    private List<Integer> insResultList;

    @ApiModelProperty("最终结果")
    private String finalResult;

    @ApiModelProperty("原材料id")
    private Integer ifsInventoryId;

    @ApiModelProperty("检验单类型")
    private String orderType;

    @ApiModelProperty("检验单类型")
    private Integer quantity;

    @ApiModelProperty("检验单类型")
    private Integer sort;

    @ApiModelProperty("电缆标识")
    private String cableTag;

    @ApiModelProperty("唯一结果值")
    private LinkedHashSet<String> tellSet;

    @ApiModelProperty("电缆标识内容集合")
    private TreeMap<String, String> cableTagValueMap;

    @ApiModelProperty("电缆标识结果集合")
    private TreeMap<String, Integer> cableTagResultMap;

}

package com.ruoyi.inspect.dto;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * @Author zhuo
 * @Date 2024/11/30
 */
@Data
public class SampleProductExportDto {

    @ApiModelProperty("样品id")
    private Integer id;

    @ApiModelProperty("产品id")
    private Integer insProductId;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("样品名称")
    private String sample;

    @ApiModelProperty("检验项分类")
    private String inspectionItemClass;
    private String inspectionItemClassEN;

    @ApiModelProperty("检验项")
    private String inspectionItem;
    private String inspectionItemEn;

    @ApiModelProperty("检验子项")
    private String inspectionItemSubclass;
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

    @ApiModelProperty("序号")
    private String index;

    @ApiModelProperty("名称")
    private String inspectionName;

    //条件
    private String radius;

    // 检验结果列表
    private List<String> lastValueList;

    // 检验结果列表
    private List<Integer> insResultList;

    //最终结果
    private String finalResult;

    //原材料id
    private Integer ifsInventoryId;

    //检验单类型
    private String orderType;

    //检验单类型
    private Integer quantity;

    //检验单类型
    private Integer sort;

    //电缆标识
    private String cableTag;

    // 唯一结果值
    private LinkedHashSet<String> tellSet;

    // 电缆标识内容集合
    private TreeMap<String, String> cableTagValueMap;

    // 电缆标识结果集合
    private TreeMap<String, Integer> cableTagResultMap;

    // 检验项和条件合并关联
    public String getInspectionItemRadius() {
        if (StringUtils.isBlank(radius)) {
            return inspectionItem;
        }
        return inspectionItem + radius;
    }

}

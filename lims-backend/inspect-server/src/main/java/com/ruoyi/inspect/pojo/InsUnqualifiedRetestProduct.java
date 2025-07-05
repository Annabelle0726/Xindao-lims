package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 不合格检验项目复测
 *
 * @author zhuo
 * @since 2024-09-03
 */
@TableName(value = "ins_unqualified_retest_product")
@Data
public class InsUnqualifiedRetestProduct implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    //外键：ins_product表id
    private Integer insProductId;

    @ApiModelProperty("项目参数id")
    private Integer structureItemParameterId;
    //检验项
    private String inspectionItem;
    //检验项子类
    private String inspectionItemSubclass;
    //工厂
    private String factory;
    //实验室
    private String laboratory;
    //样品分类
    private String sampleType;
    //样品
    private String sample;
    //型号
    private String model;
    //子实验室
    private String sonLaboratory;
    //计量单位
    private String unit;
    //单价(元)
    private Double price;
    //工时(H)
    private Double manHour;
    //工时分组
    private String manHourGroup;
    //检验项类型
    private String inspectionItemType;
    //检验值类型
    private String inspectionValueType;
    //设备组
    private String deviceGroup;
    //检验次数
    private Integer checkoutNumber;
    //区间
    private String section;
    //取值类型
    private String valueType;
    //方法
    private String method;
    //预计时间(天)
    private Integer manDay;
    //特殊标识
    private String bsm;
    //要求值
    private String ask;
    //要求描述
    private String tell;
    //最终值
    @TableField("`last_value`")
    private String lastValue;
    //1：合格 0：不合格 3: 不判定
    private Integer insResult;
    //1：有效 0：无效
    private Integer state;
    //外键：ins_sample表id
    private Integer insSampleId;

    // 创建人
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    // 修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    //外键：standard_template表id
    private Integer templateId;
    //字典类型
    private String dic;
    //方法
    private String methodS;
    //外键
    private Integer insFiberId;
    //外键
    private Integer insFibersId;
    //检验项EN
    private String inspectionItemEn;
    //检验项小类EN
    private String inspectionItemSubclassEn;
    //检验项分类
    private String inspectionItemClass;
    //检验项分类EN
    private String inspectionItemClassEn;
    //外键：标准方法id
    private Integer standardMethodListId;
    //温度
    private String temperature;
    //湿度
    private String humidity;
    //外键(目前只有松套管项目使用)
    private Integer insBushId;
    //范围
    private String radius;
    //电缆标识
    private String cableTag;
    // 重复标识
    private String repetitionTag;
    //原材料批量标识
    private String rawMaterialTag;
    //复测次数标识
    private String retestTag;
    //排序
    private Integer sort;
    // 绑定值不参加展示, 只参加计算
    @ApiModelProperty(value = "是否是绑定值, 0否, 1是")
    private Integer isBinding;

    @ApiModelProperty(value = "自关联(绑定id)")
    private Integer bindingProductId;
}


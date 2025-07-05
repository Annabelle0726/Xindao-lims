package com.ruoyi.inspect.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* 检验下单
* @TableName ins_order
*/
@Data
@TableName("ins_order")
@ExcelIgnoreUnannotated
public class InsOrder implements Serializable {


    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ExcelProperty(index = 0, value = "委托编号")
    @ApiModelProperty(value = "委托编号")
    private String entrustCode;

    @ApiModelProperty(value = "外部委托编号")
    private String outEntrustCode;

    @ApiModelProperty(value = "检验类别")
    private String orderType;

    @ApiModelProperty("下单客户")
    private String custom;

    @ExcelProperty(index = 1, value = "委托单位")
    @ApiModelProperty("委托单位")
    private String company;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("工厂域")
    private String code;

    @ApiModelProperty("紧急程度 （0普通 1优先 2紧急）")
    private Integer type;

    @ExcelProperty(index = 9, value = "约定时间%")
    @ApiModelProperty("约定时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointed;

    @ApiModelProperty("检验结果, 0 不合格, 1合格")
    private Integer insResult;

    @ApiModelProperty("样品处理方式")
    private Integer processing;

    @ApiModelProperty("是否留样")
    private Integer isLeave;

    @ApiModelProperty("状态:-1:原材料撤回  1：检验处理 0：待审核 2：退回 3：撤销")
    private Integer state;

    @ExcelProperty(index = 7, value = "备注")
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("备注英文")
    private String remarkEn;

    @ApiModelProperty("OTC订单号")
    private String otcCode;

    @ApiModelProperty("下单人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("下单时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("下发时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("下单人id")
    private Integer userId;

    @ApiModelProperty("是否有配套样品")
    private Integer mating;

    @ApiModelProperty("检验状态(0：待检验 1:检验中 2:已检验 3：待复核 4：复核未通过 5：复核通过)")
    private Integer insState;

    @ApiModelProperty("检验工厂")
    private String factory;

    @ApiModelProperty("实验室名称")
    private String laboratory;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("样品名称")
    private String sample;

    @ApiModelProperty("检验时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime insTime;

    @ApiModelProperty("退回理由")
    private String tell;

    @ApiModelProperty("报告发送方式 1: 自取, 0:其他")
    private Integer send;

    @ApiModelProperty("来样方式")
    private String formType;

    @ApiModelProperty("审核时间")
    private LocalDateTime examineTime;

    @ApiModelProperty("工程名称")
    private String engineering;

    @ApiModelProperty("工程名称英文")
    private String engineeringEn;

    @ApiModelProperty("生产单位")
    private String production;

    @ApiModelProperty("生产单位英文")
    private String productionEn;

    @ApiModelProperty("客户单位id")
    private Long companyId;

    @ApiModelProperty("委托人id")
    private Integer prepareUserId;

    @ExcelProperty(index = 12, value = "委托人")
    private String prepareUser;

    @ApiModelProperty(name = "委托人英文")
    private String prepareUserEn;

    @ApiModelProperty(name = "委托人工号")
    private String prepareCode;

    @ApiModelProperty("是否审核撤销")
    private Integer isRevocation;

    @ApiModelProperty("审核撤销的项目id")
    private String revocationInsProductIds;

    @ApiModelProperty("是否为原材料 1 是 0 否")
    private Integer typeSource;

    @ApiModelProperty("原材料id")
    private Integer ifsInventoryId;

    @ApiModelProperty("抽查数量")
    private String testQuantity;

    @ApiModelProperty("零件描述")
    private String partDetail;

    @ApiModelProperty("是否是免检, 0否 1是" )
    private Integer isExemption;

    @ApiModelProperty("温度")
    private String temperature;

    @ApiModelProperty("湿度")
    private String humidity;

    @ApiModelProperty("样品状态")
    private String sampleStatus;

    @ApiModelProperty("季度抽样项目id")
    private Integer quarterItemId;

    @ApiModelProperty("样品名称(报告展示字段)")
    private String sampleView;

    @ApiModelProperty("样品名称引文(报告展示字段)")
    private String sampleViewEn;

    @ApiModelProperty("是否是第一次提交0否1是")
    private Integer isFirstSubmit;

    @ApiModelProperty("第一次提交时间")
    private LocalDateTime firstSubmitDate;

    @ApiModelProperty("样机员(除了其他成品外这个字段必选,用于企业微信推送)")
    @TableField(exist = false)
    private Integer issueUser;

}

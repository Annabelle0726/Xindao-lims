package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 检测或校准物品的处置
 * </p>
 *
 * @author
 * @since 2024-11-02 02:50:19
 */
@Getter
@Setter
@TableName("cnas_process_deal")
@ApiModel(value = "ProcessDeal对象", description = "检测或校准物品的处置")
public class ProcessDeal implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("样品编号")
    private String sampleCode;

    @ApiModelProperty("供样单位")
    private String sampleSupplier;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("处理方式")
    private String dealMethod;

    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dealTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("关联的历史总表id")
    private Integer totaldealId;

    @ApiModelProperty("关联的样品接收id")
    private Integer processSampleId;
}

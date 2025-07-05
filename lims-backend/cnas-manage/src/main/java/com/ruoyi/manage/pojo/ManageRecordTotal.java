package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 外来文件确认记录总历史记录表
 * </p>
 *
 * @author
 * @since 2024-11-12 10:30:08
 */
@Getter
@Setter
@TableName("cnas_manage_record_total")
@ApiModel(value = "ManageRecordTotal对象", description = "外来文件确认记录总历史记录表")
public class ManageRecordTotal  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("批准人")
    private Integer ratifyUser;

    @TableField(select = false,exist = false)
    private String ratifyUserName;

    @ApiModelProperty("批准结果")
    private String ratifyState;

    @ApiModelProperty("批准日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ratifyDate;

    @ApiModelProperty("批准人签名")
    private String ratifyUrl;

    @ApiModelProperty("拟制人")
    private Integer submitUser;

    @TableField(select = false,exist = false)
    private String submitUserName;

    @ApiModelProperty("拟制人签名")
    private String submitUrl;

    @ApiModelProperty("拟制日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate submitDate;

    @ApiModelProperty("年份")

    private String year;

    @ApiModelProperty("总数量")

    private Integer totalNum;

    @ApiModelProperty("生成申请表的路径")
    private String url;
}

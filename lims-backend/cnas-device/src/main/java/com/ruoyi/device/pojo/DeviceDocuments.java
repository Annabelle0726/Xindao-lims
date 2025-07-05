package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 设备档案
 */
@Data
@TableName(value = "device_documents")
public class DeviceDocuments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文档类型（枚举）
     */
    private String documentType;

    /**
     * 名称
     */
    private String name;

    /**
     * 版本号
     */
    private String version;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 页数
     */
    private Integer pageCount;

    /**
     * 提供商
     */
    private String provider;

    /**
     * 提供日期
     */
    private LocalDateTime provideDate;

    /**
     * 备注
     */
    private String comments;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("资产编号")
    private String number;

    @ApiModelProperty("原始文件名称")
    private String systemFileName;

    @ApiModelProperty("系统生成文件名称")
    private String fileName;


}

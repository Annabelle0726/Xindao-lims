package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-防雷检测
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:16:36
 */
@Getter
@Setter
@TableName("cnas_fe_lightning_protection")
@ApiModel(value = "FeLightningProtection对象", description = "设施和环境条件-设施和环境条件要求-防雷检测")
public class FeLightningProtection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "lightning_protection_id", type = IdType.AUTO)
    private Integer lightningProtectionId;

    @ApiModelProperty("原文件名")
    private String fileName;

    @ApiModelProperty("系统生成文件名")
    private String systemFileName;

    @ApiModelProperty("检测日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate detectionDate;

    @ApiModelProperty("有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate termValidity;

    @ApiModelProperty("检测单位")
    private String detectionUnit;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

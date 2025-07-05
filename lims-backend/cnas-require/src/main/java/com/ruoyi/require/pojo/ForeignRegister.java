package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 外来人员登记
 * </p>
 *
 * @author 
 * @since 2024-11-19 07:17:35
 */
@Data
@TableName("cnas_foreign_register")
@ApiModel(value = "ForeignRegister对象", description = "外来人员登记")
public class ForeignRegister {

    @TableId(value = "register_id", type = IdType.AUTO)
    private Integer registerId;

    @ApiModelProperty("登记如期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registerDate;

    @ApiModelProperty("进入区域")
    private String area;

    @ApiModelProperty("进入人员")
    private String personnel;

    @ApiModelProperty("进入原因")
    private String reason;

    @ApiModelProperty("陪同人员id")
    private Integer accompanyingId;

    @ApiModelProperty("陪同人员")
    private String accompanyingName;

    @ApiModelProperty("批准人id")
    private Integer approveId;

    @ApiModelProperty("批准人")
    private String approveName;

    @ApiModelProperty("保密及其他情况")
    private String confidentiality;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

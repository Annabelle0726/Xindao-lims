package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标准物质清单借用归还记录表
 * </p>
 *
 * @author
 * @since 2024-11-14 01:49:11
 */
@Getter
@Setter
@TableName("cnas_fe_standard_substance_record")
@ApiModel(value = "FeStandardSubstanceRecord对象", description = "标准物质清单借用归还记录表")
public class FeStandardSubstanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("物质id")
    private Integer substanceId;

    @ApiModelProperty("借用-完好性")
    private String integrity;

    @ApiModelProperty("借用-借用人")
    private String borrowUser;

    @ApiModelProperty("借用-联系方式")
    private String phone;

    @ApiModelProperty("借用-借出日期")
    private LocalDateTime borrowDate;

    @ApiModelProperty("归还日期")
    private LocalDateTime borrowReturnDate;

    @ApiModelProperty("借出人")
    private String lender;

    @ApiModelProperty("归还-检查人")
    private String rummager;

    @ApiModelProperty("0：借用 1:归还")
    private String status;

    @ApiModelProperty("归还人")
    private String returnedPerson;

    @ApiModelProperty("归还-日期")
    private LocalDateTime returnDate;

    @ApiModelProperty("归还-完好性")
    private String returnIntegrity;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

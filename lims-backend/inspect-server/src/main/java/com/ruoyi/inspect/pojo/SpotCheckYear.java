package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 年度抽样计划
 *
 * @author zhuo
 * @since 2024-10-10
 */
@TableName(value ="spot_check_year")
@Data
public class SpotCheckYear implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer yearId;

    @ApiModelProperty("名称")
    private String yearHead;

    @ApiModelProperty("报告生成地址")
    private String fileUrl;

    @ApiModelProperty("表格内备注")
    private String tableRemark;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("编制人")
    private Integer writeUser;

    @ApiModelProperty("会签人")
    private String countersignUser;

    @ApiModelProperty("批准人")
    private Integer examineUser;

    @ApiModelProperty("批准人")
    private Integer ratifyUser;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}


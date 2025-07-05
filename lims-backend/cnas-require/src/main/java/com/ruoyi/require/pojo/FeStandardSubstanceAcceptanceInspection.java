package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 验收开箱记录
 * </p>
 *
 * @author
 * @since 2024-11-14 03:30:09
 */
@Getter
@Setter
@TableName("cnas_fe_standard_substance_acceptance_inspection")
@ApiModel(value = "FeStandardSubstanceAcceptanceInspection对象", description = "验收开箱记录")
public class FeStandardSubstanceAcceptanceInspection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer acceptanceId;

    private String name;

    private Integer number;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

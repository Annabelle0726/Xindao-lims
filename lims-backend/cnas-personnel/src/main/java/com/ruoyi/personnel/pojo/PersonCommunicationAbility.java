package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 沟通记录
 * </p>
 *
 * @author
 * @since 2024-10-09 12:00:57
 */
@Getter
@Setter
@TableName("cnas_person_communication_ability")
@ApiModel(value = "PersonCommunicationAbility对象", description = "沟通记录")
public class PersonCommunicationAbility implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("沟通人id")
    private String userId;

    @ApiModelProperty("沟通时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime communicationTime;

    @ApiModelProperty("沟通地点")
    private String communicationPlace;

    @ApiModelProperty("沟通内容")
    private String communicationContent;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "更新人id", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

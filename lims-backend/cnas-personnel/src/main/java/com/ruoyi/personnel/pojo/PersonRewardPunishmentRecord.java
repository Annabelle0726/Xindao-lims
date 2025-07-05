package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 奖惩记录
 * </p>
 *
 * @author
 * @since 2024-10-08 11:25:02
 */
@Getter
@Setter
@TableName("cnas_person_reward_punishment_record")
@ApiModel(value = "PersonRewardPunishmentRecord对象", description = "奖惩记录")
public class PersonRewardPunishmentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("奖惩级别")
    private String rewardPunishLevel;

    @ApiModelProperty("奖惩名称")
    private String rewardPunishName;

    @ApiModelProperty("奖惩时间")
    private LocalDateTime rewardPunishTime;

    @ApiModelProperty("奖惩单位")
    private String rewardPunishWorkUnit;

    @ApiModelProperty("奖惩内容")
    private String rewardPunishContent;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;
}

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
 * 人员能力
 * </p>
 *
 * @author
 * @since 2024-10-10 11:26:18
 */
@Getter
@Setter
@TableName("cnas_person_personnel_capacity")
@ApiModel(value = "PersonPersonnelCapacity对象", description = "人员能力")
public class PersonPersonnelCapacity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学历")
    private String academicDegree;

    @ApiModelProperty("学历 符合与否(1：符合；2：不符合；3：不适用)")
    private Integer academicConformNot;

    @ApiModelProperty("学历 备注")
    private String academicRemarks;

    @ApiModelProperty("相关年限")
    private String relatedYears;

    @ApiModelProperty("相关年限  符合与否(1：符合；2：不符合；3：不适用)")
    private Integer relatedYearsConformNot;

    @ApiModelProperty("相关年限 备注")
    private String relatedYearsRemarks;

    @ApiModelProperty("相关培训")
    private String relatedTraining;

    @ApiModelProperty("相关培训 符合与否(1：符合；2：不符合；3：不适用)")
    private Integer relatedTrainingConformNot;

    @ApiModelProperty("相关培训 备注")
    private String relatedTrainingRemarks;

    @ApiModelProperty("相关经验")
    private String relevantExperience;

    @ApiModelProperty("相关经验 符合与否(1：符合；2：不符合；3：不适用)")
    private Integer relevantExperienceConformNot;

    @ApiModelProperty("相关经验 备注")
    private String relevantExperienceRemarks;

    @ApiModelProperty("上岗证")
    private String workLicense;

    @ApiModelProperty("上岗证 符合与否(1：符合；2：不符合；3：不适用)")
    private Integer workLicenseConformNot;

    @ApiModelProperty("上岗证 备注")
    private String workLicenseRemarks;

    @ApiModelProperty("岗位职责")
    private String jobResponsibilities;

    @ApiModelProperty("岗位职责 符合与否(1：符合；2：不符合；3：不适用)")
    private Integer jobResponsibilitiesConformNot;

    @ApiModelProperty("岗位职责 备注")
    private String jobResponsibilitiesRemarks;

    @ApiModelProperty("综合评价")
    private String comprehensiveAssessment;

    @ApiModelProperty("2 确认人 userId主键")
    private Integer confirmOperatingPersonnelId;

    @ApiModelProperty("2 确认人 日期")
    private LocalDateTime confirmDate;

    @ApiModelProperty(value = "创建日期", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新日期", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty(value = "更新人", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "人员姓名 id")
    private Integer userId;

}

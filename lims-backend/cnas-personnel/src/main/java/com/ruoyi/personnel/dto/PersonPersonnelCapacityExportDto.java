package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonPersonnelCapacity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/28
 */
@Data
public class PersonPersonnelCapacityExportDto extends PersonPersonnelCapacity {

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("人员姓名")
    private String userName;

    @ApiModelProperty("学历")
    private String officialAcademicRedentials;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("职称")
    private String professionalTitle;

    @ApiModelProperty("工作经历")
    private String placeWork;

    @ApiModelProperty("学历 符合与否(1：符合)")
    private String academicConformNot1 = "□";
    @ApiModelProperty("学历 符合与否(2：不符合)")
    private String academicConformNot2 = "□";
    @ApiModelProperty("学历 符合与否(3：不适用)")
    private String academicConformNot3 = "□";

    @ApiModelProperty("相关年限  符合与否(1：符合)")
    private String relatedYearsConformNot1 = "□";
    @ApiModelProperty("相关年限  符合与否(2：不符合)")
    private String relatedYearsConformNot2 = "□";
    @ApiModelProperty("相关年限  符合与否(3：不适用)")
    private String relatedYearsConformNot3 = "□";

    @ApiModelProperty("相关培训 符合与否(1：符合)")
    private String relatedTrainingConformNot1 = "□";
    @ApiModelProperty("相关培训 符合与否(2：不符合)")
    private String relatedTrainingConformNot2 = "□";
    @ApiModelProperty("相关培训 符合与否(3：不适用)")
    private String relatedTrainingConformNot3 = "□";

    @ApiModelProperty("相关经验 符合与否(1：符合)")
    private String relevantExperienceConformNot1 = "□";
    @ApiModelProperty("相关经验 符合与否(2：不符合)")
    private String relevantExperienceConformNot2 = "□";
    @ApiModelProperty("相关经验 符合与否(3：不适用)")
    private String relevantExperienceConformNot3 = "□";

    @ApiModelProperty("上岗证 符合与否(1：符合)")
    private String workLicenseConformNot1 = "□";
    @ApiModelProperty("上岗证 符合与否(2：不符合)")
    private String workLicenseConformNot2 = "□";
    @ApiModelProperty("上岗证 符合与否(3：不适用)")
    private String workLicenseConformNot3 = "□";

    @ApiModelProperty("岗位职责1(熟悉本岗位的产品检测样品制备和相关产品基础知识)")
    private String jobResponsibilities1 = "□";
    @ApiModelProperty("岗位职责2(熟悉本岗位样品检测流程)")
    private String jobResponsibilities2 = "□";
    @ApiModelProperty("岗位职责3(正确熟练操作本岗位仪表设备)")
    private String jobResponsibilities3 = "□";
    @ApiModelProperty("岗位职责4(熟悉本岗位相关检测标准)")
    private String jobResponsibilities4 = "□";
    @ApiModelProperty("岗位职责5(熟悉本岗位产品性能及结果判断、分析)")
    private String jobResponsibilities5 = "□";
    @ApiModelProperty("岗位职责6(完成相应的厂验、认证)")
    private String jobResponsibilities6 = "□";
    @ApiModelProperty("岗位职责7(编写相关检测手顺)")
    private String jobResponsibilities7 = "□";
    @ApiModelProperty("岗位职责8(了解仪器设备基本结构与简单维护保养)")
    private String jobResponsibilities8 = "□";
    @ApiModelProperty("岗位职责9(具备技能培训的能力)")
    private String jobResponsibilities9 = "□";
    @ApiModelProperty("岗位职责10(具备检测仪器改造能力)")
    private String jobResponsibilities10 = "□";

    @ApiModelProperty("岗位职责 符合与否(1：符合)")
    private String jobResponsibilitiesConformNot1 = "□";
    @ApiModelProperty("岗位职责 符合与否(2：不符合)")
    private String jobResponsibilitiesConformNot2 = "□";
    @ApiModelProperty("岗位职责 符合与否(3：不适用)")
    private String jobResponsibilitiesConformNot3 = "□";

    @ApiModelProperty("综合评价1(可胜任该岗位)")
    private String comprehensiveAssessment1 = "□";
    @ApiModelProperty("综合评价2(可边培训边上岗)")
    private String comprehensiveAssessment2 = "□";
    @ApiModelProperty("综合评价3(不胜任该岗位)")
    private String comprehensiveAssessment3 = "□";
}

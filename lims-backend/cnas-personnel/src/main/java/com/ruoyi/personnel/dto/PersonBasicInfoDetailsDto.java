package com.ruoyi.personnel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Author: yuan
 * Date: 2024-12-13 星期五 13:52:52
 * Description:
 */
@Data
public class PersonBasicInfoDetailsDto {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("入职时间")
    private String entryTimeStr;

    @ApiModelProperty("实际实习结束")
    private String endPracticalPracticeStr;

    @ApiModelProperty("籍贯")
    private String nativePlace;

    @ApiModelProperty("身份证号")
    private String identityCard;

    @ApiModelProperty("身份证地址")
    private String idAddress;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("毕业院校")
    private String graduatedInstitutions1;

    @ApiModelProperty("专业")
    private String major1;

    @ApiModelProperty("毕业时间1")
    private LocalDateTime graduationTime1;

    @ApiModelProperty("最高学历")
    private String officialAcademicRedentials;

    @ApiModelProperty("最高学位")
    private String highestDegree;

    @ApiModelProperty("职称")
    private String professionalTitle;

    // 职业能力

    @ApiModelProperty("紧急联系人")
    private String emergencyContact;

    @ApiModelProperty("紧急联系人电话")
    private String emergencyContactPhone;
}

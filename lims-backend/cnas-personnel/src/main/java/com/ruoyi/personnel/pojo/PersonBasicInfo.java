package com.ruoyi.personnel.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
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
 *
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-08-30 09:19:57
 */
@Getter
@Setter
@TableName("cnas_person_basic_info")
@ApiModel(value = "PersonBasicInfo对象", description = "")
public class PersonBasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("当前状态")
    private String currentState;

    @ApiModelProperty("职称")
    @ExcelProperty("职称")
    private String professionalTitle;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("人员分类")
    private String personnelClassification;

    @ApiModelProperty("出生日期")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    private LocalDateTime dateBirth;

    @ApiModelProperty("身份证号")
    @ExcelProperty("证件号码")
    private String identityCard;

    @ApiModelProperty("民族")
    private String nation;

    @ApiModelProperty("政治面貌")
    private String politicalStatus;

    @ApiModelProperty("最高学历")
    @ExcelProperty("最高学历")
    private String officialAcademicRedentials;

    @ApiModelProperty("毕业时间1")
    @ExcelProperty("毕业时间")
    private LocalDateTime graduationTime1;

    @ApiModelProperty("毕业院校1")
    @ExcelProperty("毕业院校")
    private String graduatedInstitutions1;

    @ApiModelProperty("专业1")
    @ExcelProperty("所学专业")
    private String major1;

    @ApiModelProperty("毕业时间2")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    private LocalDateTime graduationTime2;

    @ApiModelProperty("毕业院校2")
    private String graduatedInstitutions2;

    @ApiModelProperty("专业2")
    private String major2;

    @ApiModelProperty("手机号")
    private String telephone;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("用户表（user）id")
    private Integer userId;

    @ApiModelProperty("公司名称")
    private String corporateName;

    @ApiModelProperty("岗位名称")
    private String postName;

    @ApiModelProperty("入集团时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    @ExcelProperty("入集团时间")
    private LocalDateTime groupTime;

    @ApiModelProperty("劳动关系")
    private Integer laborRelations;

    @ApiModelProperty("工作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    private LocalDateTime workingTime;

    @ApiModelProperty("合同有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    private LocalDateTime contractLifeTime;

    @ApiModelProperty("籍贯")
    @ExcelProperty("籍贯")
    private String nativePlace;

    @ApiModelProperty("证件有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    private LocalDateTime validityPeriod;

    @ApiModelProperty("婚姻状况")
    private Integer maritalStatus;

    @ApiModelProperty("证件地址")
    @ExcelProperty("证件地址")
    private String idAddress;

    @ApiModelProperty("证件详细地址")
    private String idDetailAddress;

    @ApiModelProperty("入党/团时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.IGNORED) // 为空可以更新
    private LocalDateTime dumplingTime;

    @ApiModelProperty("最高学位")
    @ExcelProperty("最高学位")
    private String highestDegree;

    @ApiModelProperty("最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateTime;



}

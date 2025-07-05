package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.QualitySuperviseDetailsAccording;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-11 星期三 16:26:41
 * Description:
 */
@Data
public class QualitySuperviseDetailsAccordingDto extends QualitySuperviseDetailsAccording {

    @ApiModelProperty("1发现部门-日期")
    private String discovererDateString;

    @ApiModelProperty("3责任部门 日期")
    private String responsibleDepartmentDateString;

    @ApiModelProperty("4纠正措施 日期")
    private String correctiveMeasureDateString;

    @ApiModelProperty("5质量负责人日期")
    private String qualitySupervisorDateString;

    @ApiModelProperty("0被监督时间 日期")
    private String supervisedTimeString;

    @ApiModelProperty("4纠正措施处理单跟踪(是)")
    private String correctiveMeasureFollowTracksYes = "□";
    @ApiModelProperty("4纠正措施处理单跟踪(否)")
    private String correctiveMeasureFollowTracksNo = "□";


    @ApiModelProperty("5是否通知客户(是)")
    private String whetherInformCustomerYes = "□";
    @ApiModelProperty("5是否通知客户(否)")
    private String whetherInformCustomerNo = "□";


    @ApiModelProperty("5是否恢复工作(1：是；2：否)")
    private String whetherResumeWorkYes = "□";
    @ApiModelProperty("5是否恢复工作(1：是；2：否)")
    private String whetherResumeWorkNo = "□";

    @ApiModelProperty("1不符合工作发现途径0(管理评审)")
    private String discoveryApproach0 = "□";
    @ApiModelProperty("1不符合工作发现途径1(内部审核)")
    private String discoveryApproach1 = "□";
    @ApiModelProperty("1不符合工作发现途径2(检测过程控制)")
    private String discoveryApproach2 = "□";
    @ApiModelProperty("1不符合工作发现途径3(内部质量控制)")
    private String discoveryApproach3 = "□";
    @ApiModelProperty("1不符合工作发现途径4(内部监督)")
    private String discoveryApproach4 = "□";
    @ApiModelProperty("1不符合工作发现途径5(外部评审/检查)")
    private String discoveryApproach5 = "□";
    @ApiModelProperty("1不符合工作发现途径6(顾客投诉/意见反馈)")
    private String discoveryApproach6 = "□";
    @ApiModelProperty("1不符合工作发现途径7(其他)")
    private String discoveryApproach7 = "□";
}

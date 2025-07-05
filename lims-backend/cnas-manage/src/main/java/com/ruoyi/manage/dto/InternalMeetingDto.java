package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.InternalMeeting;
import com.ruoyi.manage.pojo.InternalMeetingDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/12
 */
@Data
public class InternalMeetingDto extends InternalMeeting {

    @ApiModelProperty("参加人")
    private List<InternalMeetingDetail> meetingDetailList;

    @ApiModelProperty("参加人信息")
    private String participantName;
}

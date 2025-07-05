package com.ruoyi.manage.vo;

import com.ruoyi.manage.pojo.ManageMeeting;
import com.ruoyi.manage.pojo.ManageMeetingParticipants;
import lombok.Data;

import java.util.List;

@Data
public class MeetingParticipantsDetailsVo {

    private ManageMeeting manageMeeting;

    private List<ManageMeetingParticipants> participantsList;
}

package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.ManageMeeting;
import com.ruoyi.manage.pojo.ManageMeetingParticipants;
import lombok.Data;

import java.util.List;

@Data
public class ManageMeetingDto extends ManageMeeting {

    private List<ManageMeetingParticipants> list;

    private String participant;

}

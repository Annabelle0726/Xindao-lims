package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.ProcessComplain;
import lombok.Data;

@Data
//投诉详情
public class ProcessComplainDto extends ProcessComplain {

    private String phone;

    private String email;

    private String problemRecordsUserName;

    private String dutyOwnershipUserName;

    private String causeAnalysisUserName;

    private String correctiveActionUserName;

    private String correctiveActionConfirmationUserName;
}

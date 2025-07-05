package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonRewardPunishmentRecord;
import lombok.Data;

@Data
public class PersonRewardPunishmentRecordDto extends PersonRewardPunishmentRecord {

    private String userName;

    private String account;

    private String createUserName;
}

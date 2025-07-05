package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonPostAuthorizationRecord;
import lombok.Data;

@Data
public class PersonPostAuthorizationRecordDto extends PersonPostAuthorizationRecord {

    private String userName;

    private String account;

    private String createUserName;
}

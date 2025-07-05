package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonCommunicationAbility;
import lombok.Data;

@Data
public class PersonCommunicationAbilityDto extends PersonCommunicationAbility {

    private String userName;

    private String account;

    private String createUserName;
}

package com.ruoyi.personnel.dto;

import com.ruoyi.personnel.pojo.PersonJobResponsibilities;
import lombok.Data;

@Data
public class PersonJobResponsibilitiesDto extends PersonJobResponsibilities {

    private String incumbentName;

    private String supervisorName;

    private String departLimsName;

    private String account;
}

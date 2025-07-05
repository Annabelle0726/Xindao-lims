package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.ProcessMethodSearchNewArchived;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/12/25
 */
@Data
public class ProcessMethodSearchNewArchivedDto extends ProcessMethodSearchNewArchived {

    @ApiModelProperty("编制人")
    private String writeName;

    @ApiModelProperty("批准人")
    private String ratifyName;
}

package com.ruoyi.personnel.dto;

import com.deepoove.poi.data.PictureRenderData;
import com.ruoyi.personnel.pojo.PersonTrainingRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonTrainingRecordDto extends PersonTrainingRecord {
    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "工号")
    private String account;

    @ApiModelProperty(value = "角色")
    private String roleName;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty("培训人员图片")
    private String signatureUrl;
}

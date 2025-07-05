package com.ruoyi.personnel.dto;

import com.deepoove.poi.data.PictureRenderData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/25
 */
@Data
public class TrainingRecordExportDto {

    @ApiModelProperty("用户名称1")
    private PictureRenderData userNameRender1;

    @ApiModelProperty("部门1")
    private String department1;

    @ApiModelProperty("考核结果1")
    private String examinationResults1;

    @ApiModelProperty("用户名称2")
    private PictureRenderData userNameRender2;

    @ApiModelProperty("部门1")
    private String department2;

    @ApiModelProperty("考核结果1")
    private String examinationResults2;





}

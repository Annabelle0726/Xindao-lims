package com.ruoyi.personnel.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.deepoove.poi.data.PictureRenderData;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonTrainingDetailedDto extends PersonTrainingDetailed {

    @ApiModelProperty("举办部门名称")
    private String holdingDepartmentName;

    @ApiModelProperty("培训讲师名称")
    private String trainingLecturerName;

    @ApiModelProperty("培训老师地址")
    private String trainingLecturerSignatureUrl;

    @ApiModelProperty("培训老师地址流")
    private PictureRenderData trainingLecturerRender;

    @ApiModelProperty("当前登录人是否认领")
    private Boolean whetherClaim;


    @ApiModelProperty("培训日期")
    private String trainingDateString;

    // 导出使用
    @TableField(select = false, exist = false)
    @ApiModelProperty("序号(导出使用)")
    private Integer index;

}

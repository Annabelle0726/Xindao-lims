package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 人员基本信息
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2025-01-09 05:45:04
 */
@Getter
@Setter
@TableName("cnas_person_basic_info_work")
@ApiModel(value = "PersonBasicInfoWork对象", description = "人员基本信息")
public class PersonBasicInfoWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "basic_info_work_id", type = IdType.AUTO)
    private Integer basicInfoWorkId;

    @ApiModelProperty("人员id")
    private Integer userId;

    @ApiModelProperty("工作经历")
    private String workExperience;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

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
 * 任职授权记录
 * </p>
 *
 * @author 
 * @since 2024-10-09 10:48:17
 */
@Getter
@Setter
@TableName("cnas_person_post_authorization_record")
@ApiModel(value = "PersonPostAuthorizationRecord对象", description = "任职授权记录")
public class PersonPostAuthorizationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("证书编号")
    private String certificateNumber;

    @ApiModelProperty("被任职人员id")
    private String userId;

    @ApiModelProperty("任职岗位")
    private String post;

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("原文件名称")
    private String fileName;

    @ApiModelProperty("系统生成文件名称")
    private String systemFileName;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人id",hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty(value = "创建人id", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("理论知识考试成绩")
    private String num1;

    @ApiModelProperty("操作技能考试成绩")
    private String num2;
}

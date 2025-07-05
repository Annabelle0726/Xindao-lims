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
 * 人员基本信息附件
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2025-01-09 05:28:55
 */
@Getter
@Setter
@TableName("cnas_person_basic_info_file")
@ApiModel(value = "PersonBasicInfoFile对象", description = "人员基本信息附件")
public class PersonBasicInfoFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "basic_info_file_id", type = IdType.AUTO)
    private Integer basicInfoFileId;

    @ApiModelProperty("人员基本信息id")
    private Integer userId;

    @ApiModelProperty("类型:1图片/2文件")
    private Integer type;

    @ApiModelProperty("附件路径")
    private String fileUrl;

    @ApiModelProperty("附件名称")
    private String fileName;

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

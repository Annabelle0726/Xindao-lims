package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备验收(装备)附件表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 01:45:26
 */
@Getter
@Setter
@TableName("device_acceptance_file")
@ApiModel(value = "DeviceAcceptanceFile对象", description = "设备验收(装备)附件表")
public class DeviceAcceptanceFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "acceptance_file_id", type = IdType.AUTO)
    private Integer acceptanceFileId;

    @ApiModelProperty("设备验收id")
    private Integer acceptanceId;

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

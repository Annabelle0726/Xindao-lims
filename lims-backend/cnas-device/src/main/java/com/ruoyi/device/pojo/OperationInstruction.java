package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备 - 作业指导书 添加受控文件 子
 * </p>
 *
 * @author
 * @since 2024-12-04 10:43:32
 */
@Getter
@Setter
@TableName("device_operation_instruction")
public class OperationInstruction {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("设备主键id")
    private String deviceId;

    @ApiModelProperty("文件类型")
    private String documentType;

    @ApiModelProperty("文档编号")
    private String documentNumber;

    @ApiModelProperty("文件版本")
    private String documentVersion;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("提交日期")
    private LocalDate submitDate;

    @ApiModelProperty("文档说明")
    private String documentNote;

    @ApiModelProperty("系统生成名称")
    private String fileName;

    @ApiModelProperty("系统生成名称")
    private String fileSystemName;

    @ApiModelProperty("作业指导书id")
    private Integer instructionId;

    @ApiModelProperty("上传人id")
    private Integer uploader;

    @ApiModelProperty("审批人id")
    private Integer approverId;

    @ApiModelProperty("审批状态")
    private Boolean status;

    @ApiModelProperty("生效时间")
    private LocalDateTime entryIntoForceTime;

    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;
}

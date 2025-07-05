package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标准方法验证设备附件表
 *
 * @author zhuo
 * @since 2024-11-05
 */
@Data
@TableName("cnas_process_method_verify_machine_attachment")
public class ProcessMethodVerifyMachineAttachment {

    @TableId(type = IdType.AUTO)
    private Integer machineAttachmentId;

    @ApiModelProperty("方法验证id")
    private Integer methodVerifyId;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("设备名称")
    private String machineName;

    @ApiModelProperty("规格型号")
    private String machineSpecification;

    @ApiModelProperty("策略范围")
    private String machineMeasuringRange;

    @ApiModelProperty("扩展信息")
    private String other;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}


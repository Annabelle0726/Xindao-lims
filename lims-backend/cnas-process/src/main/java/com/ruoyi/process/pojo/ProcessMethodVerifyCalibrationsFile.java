package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标准方法验证设备校准附件表
 *
 * @author zhuo
 * @since 2024-11-12
 */
@Data
@TableName("cnas_process_method_verify_calibrations_file")
public class ProcessMethodVerifyCalibrationsFile {

    @TableId(type = IdType.AUTO)
    private Integer calibrationsFileId;
    @ApiModelProperty("方法验证id")
    private Integer methodVerifyId;

    @ApiModelProperty("类型:1图片/2文件")
    private Integer type;

    @ApiModelProperty("附件路径")
    private String fileUrl;

    @ApiModelProperty("附件名称")
    private String fileName;

    @ApiModelProperty("设备id")
    private Integer deviceId;

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


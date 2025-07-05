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
 * 作业指导书添加受控文件表
 * </p>
 *
 * @author 
 * @since 2024-12-04 10:29:18
 */
@Getter
@Setter
@TableName("device_instruction")
@ApiModel(value = "Instruction对象", description = "作业指导书添加受控文件表")
public class DeviceInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("申请编号")
    private String applicationNumber;

    @ApiModelProperty("申请部门")
    private String applicationDepartment;

    @ApiModelProperty("责任人")
    private String personLiable;

    @ApiModelProperty("受控申请说明")
    private String controlledApplicationDescription;

    @ApiModelProperty("系统生成名称")
    private String fileName;

    @ApiModelProperty("系统生成名称")
    private String fileSystemName;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;
}

package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标准查新存档表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-25 05:28:42
 */
@Getter
@Setter
@TableName("cnas_process_method_search_new_archived")
@ApiModel(value = "ProcessMethodSearchNewArchived对象", description = "标准查新存档表")
public class ProcessMethodSearchNewArchived implements Serializable {

    @TableId(value = "archived_id", type = IdType.AUTO)
    private Integer archivedId;

    @ApiModelProperty("存档名称")
    private String archivedName;

    @ApiModelProperty("编制人")
    private Integer writeUserId;

    @ApiModelProperty("编制时间")
    private LocalDateTime writeTime;

    @ApiModelProperty("批准人")
    private Integer ratifyUserId;

    @ApiModelProperty("批准时间")
    private LocalDateTime ratifyTime;

    @ApiModelProperty("批准状态,0 不通过, 1 通过")
    private Integer ratifyStatus;

    @ApiModelProperty("批准信息")
    private String ratifyRemark;

    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

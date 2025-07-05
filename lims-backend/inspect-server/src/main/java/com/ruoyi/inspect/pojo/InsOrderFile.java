package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 检验单下的附件列表
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-06-06 10:08:21
 */
@Getter
@Setter
@TableName("ins_order_file")
@ApiModel(value = "InsOrderFile对象", description = "检验单下的附件列表")
public class InsOrderFile implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关联 检验单外键id")
    private Integer insOrderId;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("附件路径")
    private String fileUrl;

    @ApiModelProperty("附件名称")
    private String fileName;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("上传人")
    @TableField(exist = false, select = false)
    private String name;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("上传时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

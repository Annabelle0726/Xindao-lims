package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务和供应品采购目录
 * </p>
 *
 * @author
 * @since 2024-12-17 06:14:51
 */
@Getter
@Setter
@TableName("suppliers_directory_contents")
@ApiModel(value = "SuppliersDirectoryContents对象", description = "服务和供应品采购目录")
public class SuppliersDirectoryContents implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("代号")
    private String code;

    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("父节点")
    private Integer parentId;


    @TableField(exist = false)
    private List<SuppliersDirectoryContents> children;
}

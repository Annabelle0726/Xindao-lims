package com.ruoyi.require.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("procurement_supplies_contents")
@ApiModel("服务和供应品采购表")
public class ProcurementSuppliesContents {

    @TableId(type = IdType.AUTO)
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
    private List<ProcurementSuppliesContents> children;



}

package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 货架
 * @TableName warehouse_shelf
 */
@TableName(value ="warehouse_shelf")
@Data
public class WarehouseShelf implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 货架名称
     */
    private String name;

    /**
     * 行
     */
    @TableField("`row`")
    private Integer row;

    /**
     * 列
     */
    @TableField("`col`")
    private Integer col;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 外键：仓库id
     */
    private Integer warehouseId;
}

package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 货架单元格
 * @TableName warehouse_cell
 */
@TableName(value ="warehouse_cell")
@Data
public class WarehouseCell implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 是否有效 1：有效 0：无效
     */
    private Integer state;

    /**
     * 外检：货架id
     */
    private Integer shelfId;
}

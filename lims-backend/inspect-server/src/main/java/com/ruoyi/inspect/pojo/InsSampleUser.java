package com.ruoyi.inspect.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 样品负责人记录
 * @TableName ins_sample_user
 */
@TableName(value ="ins_sample_user")
@Data
@NoArgsConstructor
public class InsSampleUser implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 外键：是订单insOrder表id (????)
     */
    private Integer insSampleId;

    /**
     * 外键：用户id 负责人
     */
    private Integer userId;

    /**
     * 1：确认 0：未确认
     */
    //1是复核人 0是检验人
    private Integer state;

    @TableField(fill = FieldFill.INSERT)
    private Integer createUser;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 子试验室
     * @param insSampleId
     * @param userId
     * @param state
     */
    private String sonLaboratory;

    public InsSampleUser(Integer insSampleId, Integer userId, Integer state,String sonLaboratory) {
        this.insSampleId = insSampleId;
        this.userId = userId;
        this.state = state;
        this.sonLaboratory = sonLaboratory;
    }
}

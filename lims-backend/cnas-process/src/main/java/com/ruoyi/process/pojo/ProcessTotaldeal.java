package com.ruoyi.process.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 检测或校准物品的处置总表(历史)
 * </p>
 *
 * @author
 * @since 2024-11-02 03:59:09
 */
@Getter
@Setter
@TableName("cnas_process_totaldeal")
@ApiModel(value = "ProcessTotaldeal对象", description = "检测或校准物品的处置总表(历史)")
public class ProcessTotaldeal  implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("审核人")
    private Integer examineUser;

    @ApiModelProperty("审核结果")
    private String examineState;

    @TableField(select = false, exist = false)
    private String examineUserName;

    @ApiModelProperty("批准人")
    private Integer ratifyUser;

    @ApiModelProperty("批准结果")
    private String ratifyState;

    @TableField(select = false, exist = false)
    private String ratifyUserName;

    @ApiModelProperty("提交人")
    private Integer submitUser;
    @TableField(select = false, exist = false)
    private String submitUserName;

    @ApiModelProperty("提交结果")
    private String submitState;

    @ApiModelProperty("月份")
    private String month;

}

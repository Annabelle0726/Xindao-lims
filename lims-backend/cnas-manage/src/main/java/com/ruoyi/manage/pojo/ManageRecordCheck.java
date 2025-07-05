package com.ruoyi.manage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 文件审批记录(含修订后再次审批记录)
 * </p>
 *
 * @author
 * @since 2024-11-12 02:31:36
 */
@Getter
@Setter
@TableName("cnas_manage_record_check")
@ApiModel(value = "ManageRecordCheck对象", description = "文件审批记录(含修订后再次审批记录)")
public class ManageRecordCheck  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")

    private String documentName;

    @ApiModelProperty("文件编号")

    private String documentCode;

    @ApiModelProperty("版/次")
    private String documentVersion;

    @ApiModelProperty("编制人")
    private Integer writeUser;

    @TableField(select = false,exist = false)
    private String writeUserName;

    @ApiModelProperty("审核人")
    private Integer checkUser;

    @TableField(select = false,exist = false)
    private String checkUserName;

    @ApiModelProperty("审核状态")
    private String checkState;

    @ApiModelProperty("批准人")
    private Integer ratifyUser;

    @TableField(select = false,exist = false)
    private String ratifyUserName;

    @ApiModelProperty("批准状态")
    private String ratifyState;

    @ApiModelProperty("批准日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ratifyDate;

    @ApiModelProperty("备注")
    private String remark;
}

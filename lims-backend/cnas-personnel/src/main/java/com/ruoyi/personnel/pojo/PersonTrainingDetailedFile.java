package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 人员培训计划详情附件表(CnasPersonTrainingDetailedFile)$desc
 *
 * @author makejava
 * @since 2024-12-25 14:18:22
 */
@Data
@TableName("cnas_person_training_detailed_file")
public class PersonTrainingDetailedFile {

    @TableId(type = IdType.AUTO)
    private Integer detailedFileId;

    @ApiModelProperty("人员你培训计划详情id")
    private Integer trainingDetailedId;

    @ApiModelProperty("类型:1图片/2文件")
    private Integer type;

    @ApiModelProperty("附件路径")
    private String fileUrl;

    @ApiModelProperty("附件名称")
    private String fileName;

    @ApiModelProperty("创建人")
    private Integer createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Integer updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

}


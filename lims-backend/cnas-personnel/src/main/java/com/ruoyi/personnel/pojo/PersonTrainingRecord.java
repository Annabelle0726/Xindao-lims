package com.ruoyi.personnel.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 培训记录
 * </p>
 *
 * @author
 * @since 2024-10-12 04:50:48
 */
@Getter
@Setter
@TableName("cnas_person_training_record")
@ApiModel(value = "PersonTrainingRecord对象", description = "培训记录")
public class PersonTrainingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "training_record_id", type = IdType.AUTO)
    private Integer trainingRecordId;

    @ApiModelProperty("用户表格（user）主键")
    private Integer userId;

    @ApiModelProperty("培训计划详情 - 子 id")
    private Integer courseId;

    @ApiModelProperty("考核结果")
    private String examinationResults;
}

package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 04:27:32
 */
@Getter
@Setter
@TableName("device_inspection_record_details")
@ApiModel(value = "DeviceInspectionRecordDetails对象", description = "")
public class DeviceInspectionRecordDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("点检详情id")
    @TableId(value = "inspection_record_detail_id", type = IdType.AUTO)
    private Integer inspectionRecordDetailId;

    @ApiModelProperty("点检id")
    private Integer inspectionRecordId;

    @ApiModelProperty("测试项目")
    private String testItems;

    @ApiModelProperty("标准值")
    private String standardValue;

    @ApiModelProperty("实测值")
    private String measuredValue;

    @ApiModelProperty("示值误差")
    private String indicationError;

    @ApiModelProperty("允许误差")
    private String allowableError;

    @ApiModelProperty("单项结论")
    private String singleItemConclusion;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("修改人id")
    private Integer updateUserId;
}

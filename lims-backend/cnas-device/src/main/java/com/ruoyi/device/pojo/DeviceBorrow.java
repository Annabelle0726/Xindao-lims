package com.ruoyi.device.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 10:53:51
 */
@Getter
@Setter
@TableName("device_borrow")
@ApiModel(value = "DeviceBorrow对象", description = "设备借用")
@ExcelIgnoreUnannotated
public class DeviceBorrow implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("流程编号")
    @ExcelProperty(value = "流程编号")
    private String processNumber;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("管理编号")
    @ExcelProperty(value = "管理编号")
    private String unifyNumber;

    @ApiModelProperty("借用人")
    @ExcelProperty(value = "借用人")
    private String recipientUser;

    @ExcelProperty(value = "借用人联系方式")
    @ApiModelProperty("借用人联系方式")
    private String borrowerContactInformation;

    @ApiModelProperty("借用时状态")
    @ExcelProperty(value = "借用时状态")
    //0合格;1维修;2停用;3报废
    private Integer recipientState;

    @ApiModelProperty("借用日期")
    @ExcelProperty(value = "提交日期")
    private Date recipientTime;

    @ApiModelProperty("借出人")
    @ExcelProperty(value = "借出人")
    private String submitUser;

    @ApiModelProperty("借出日期")
    @ExcelProperty(value = "借出日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("当前状态")
    @ExcelProperty(value = "当前状态")
    private String nowState;

    @ApiModelProperty("当前责任人")
    @ExcelProperty(value = "当前责任人")
    private String nowUser;

    @ApiModelProperty("附件")
    //路径
    private String url;

    @ApiModelProperty("附件")
    @ExcelProperty(value = "附件")
    //文件名
    private String fileName;

    @ApiModelProperty("下环节责任人")
    private String nextUser;

    @ApiModelProperty("提交操作人")
    private String submitOperationUser;

    @ApiModelProperty("提交操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitOperationTime;

    @ApiModelProperty("归还人")
    private String rebackUser;

    @ApiModelProperty("归还日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rebackTime;

    @ApiModelProperty("接受状态0合格;1维修;2停用;3报废")
    private Integer receiveState;

    @ApiModelProperty("设备责任人")
    private String deviceUser;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("接收操作人")
    private String receiveOperationUser;

    @ApiModelProperty("接收操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveOperationTime;

    @ApiModelProperty("设备名称")
    @TableField(select = false, exist = false)
    @ExcelProperty(value = "设备名称")
    private String deviceName;


    @ApiModelProperty("流程跟踪")
    @TableField(select = false, exist = false)
    private List<DeviceLog> deviceLogs;
}

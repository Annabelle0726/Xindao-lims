package com.ruoyi.device.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("device_log") // 记录
public class DeviceLog implements Serializable {

    @TableId(type= IdType.AUTO)
    private Integer id;
    private String operator;
//    @JsonFormat()
    private LocalDateTime operationTime;
    private String operationType;
    private String operationContent;
    private Integer deviceId;

    //关联的表名
    private String relevanceForm;

    //关联的id
    private Integer relevanceId;
}

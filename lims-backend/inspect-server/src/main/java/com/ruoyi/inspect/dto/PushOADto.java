package com.ruoyi.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提交oa数据传输对象
 */
@Data
public class PushOADto {

    /**
     * 不合格处理主键id
     */
    @ApiModelProperty(value = "不合格处理主键id")
    private Long handlerId;

    /**
     * 附件url
     */
    @ApiModelProperty(value = "附件url")
    private String fileUrl;

}

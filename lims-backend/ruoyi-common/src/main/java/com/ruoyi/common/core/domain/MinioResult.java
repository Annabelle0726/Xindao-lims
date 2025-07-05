package com.ruoyi.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MinioResult {
    @ApiModelProperty("minio中的文件名称")
    private String bucketFileName;

    @ApiModelProperty("源文件名称")
    private String originalName;

    @ApiModelProperty("预览路径")
    private String previewExpiry;
}

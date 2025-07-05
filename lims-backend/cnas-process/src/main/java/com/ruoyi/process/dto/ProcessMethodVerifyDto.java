package com.ruoyi.process.dto;

import com.ruoyi.process.pojo.ProcessMethodVerify;
import com.ruoyi.process.pojo.ProcessMethodVerifyMachineAttachment;
import com.ruoyi.process.pojo.ProcessMethodVerifyMethodFile;
import com.ruoyi.process.pojo.ProcessMethodVerifyWorkFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/5
 */
@Data
public class ProcessMethodVerifyDto extends ProcessMethodVerify {

    @ApiModelProperty("设备附件")
    private List<ProcessMethodVerifyMachineAttachment> machineAttachmentList;

    @ApiModelProperty("原始记录附件")
    private List<ProcessMethodVerifyMethodFile> methodFileList;

    @ApiModelProperty("上岗证")
    private List<ProcessMethodVerifyWorkFile> workFileList;

    @ApiModelProperty("校准证书")
    private List<ProcessMethodVerifyCalibrationsFileDto> calibrationsFileList;
}

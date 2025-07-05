package com.ruoyi.manage.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.ruoyi.manage.pojo.ManageDocumentIssueRecycle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class ManageDocumentIssueRecycleDto extends ManageDocumentIssueRecycle {

    @ApiModelProperty("发放人")
    @ExcelProperty(value = "发放人")
    private String issueUserName;

    @ApiModelProperty("回收人")
    @ExcelProperty(value = "回收人")
    private String recycleUserName;

    @ApiModelProperty("接收人")
    @ExcelProperty(value = "接收人")
    private String receiveUserName;

}

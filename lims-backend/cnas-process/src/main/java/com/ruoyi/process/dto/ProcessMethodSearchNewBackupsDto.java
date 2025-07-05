package com.ruoyi.process.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.process.pojo.ProcessMethodSearchNewBackups;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/4
 */
@Data
@ExcelIgnoreUnannotated
public class ProcessMethodSearchNewBackupsDto extends ProcessMethodSearchNewBackups {

    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("编制人id")
    private Integer writeUserId;

    @ApiModelProperty("批准人id")
    private Integer ratifyUserId;

    @ApiModelProperty("编制人日期")
    private String writeDate;

    @ApiModelProperty("批准人日期")
    private String ratifyDate;

    @ExcelProperty(value = {"是否有更新标准", "是否有更新标准"}, index = 4)
    @ApiModelProperty("是否有更新标准")
    private String isNewStandardString;

    @ExcelProperty(value = {"查新记录", "标准网"}, index = 7)
    @ApiModelProperty("标准网")
    private String standardNet;

    @ExcelProperty(value = {"查新记录", "委托情报所"}, index = 8)
    @ApiModelProperty("委托情报所")
    private String informationOffices;

    @ExcelProperty(value = {"查新记录", "标准书店"}, index = 9)
    @ApiModelProperty("标准书店")
    private String standardBookstore;

    @ExcelProperty(value = {"查新记录", "其他"}, index = 10)
    @ApiModelProperty("其他")
    private String other;

    @ExcelProperty(value = {"备注", "备注"}, index = 11)
    @ApiModelProperty("备注")
    private String remarkString;

    @ExcelProperty(value = {"序号", "序号"}, index = 0)
    @ApiModelProperty("序号")
    private Integer index;
}

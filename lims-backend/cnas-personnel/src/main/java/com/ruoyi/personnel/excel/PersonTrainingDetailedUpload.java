package com.ruoyi.personnel.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PersonTrainingDetailedUpload {

    @ExcelProperty("培训目标")
    private String trainingObjectives;

    @ExcelProperty("培训内容")
    private String trainingContent;

    @ExcelProperty("培训方式")
    private String trainingMode;

    @ExcelProperty("参加对象")
    private String participants;

    @ExcelProperty("举办部门")
    private String holdingDepartment;

    @ExcelProperty("培训讲师")
    private String trainingLecturerName;

    @ExcelProperty("培训时间")
    private String trainingDate;

    @ExcelProperty("课时")
    private Integer classHour;

    @ExcelProperty("备注")
    private String remarks;
}

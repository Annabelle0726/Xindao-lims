package com.ruoyi.manage.vo;

import com.ruoyi.manage.pojo.ManageReviewProgram;
import com.ruoyi.manage.pojo.ManageReviewProgramFile;
import lombok.Data;

import java.util.List;

@Data
public class ReviewProgramDetailsVo {

    private ManageReviewProgram program;

    private List<ManageReviewProgramFile> fileList;
}

package com.ruoyi.process.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.deepoove.poi.data.PictureRenderData;
import com.ruoyi.process.pojo.ProcessReport;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
//导出
public class ProcessReportDto extends ProcessReport {

    private List<Integer> ids;

    @ApiModelProperty("发送人姓名")
    private String sendUserName;

    @ApiModelProperty("发送人地址")
    private String sendUserUrl;

    @ApiModelProperty("发送人签名")
    private PictureRenderData sendUserRender;

    @ApiModelProperty("签收签名")
    private PictureRenderData signatoryRender;


}

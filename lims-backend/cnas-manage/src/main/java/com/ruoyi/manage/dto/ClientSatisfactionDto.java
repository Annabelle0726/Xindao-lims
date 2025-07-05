package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.ClientSatisfaction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: yuan
 * Date: 2024-12-16 星期一 10:38:30
 * Description: 客户满意度调查导出Word
 */
@Data
public class ClientSatisfactionDto extends ClientSatisfaction {

    @ApiModelProperty("服务态度, 0:满意, 1:一般, 2:不满意")
    private String serviceAttitude0;

    @ApiModelProperty("服务态度, 0:满意, 1:一般, 2:不满意")
    private String serviceAttitude1;

    @ApiModelProperty("服务态度, 0:满意, 1:一般, 2:不满意")
    private String serviceAttitude2;

    @ApiModelProperty("技术能力, 0:满意, 1:一般, 2:不满意")
    private String technicalCompetence0;

    @ApiModelProperty("技术能力, 0:满意, 1:一般, 2:不满意")
    private String technicalCompetence1;

    @ApiModelProperty("技术能力, 0:满意, 1:一般, 2:不满意")
    private String technicalCompetence2;

    @ApiModelProperty("检测工作, 0:满意, 1:一般, 2:不满意")
    private String inspectionWork0;

    @ApiModelProperty("检测工作, 0:满意, 1:一般, 2:不满意")
    private String inspectionWork1;

    @ApiModelProperty("检测工作, 0:满意, 1:一般, 2:不满意")
    private String inspectionWork2;

    @ApiModelProperty("收费合理性, 0:满意, 1:一般, 2:不满意")
    private String reasonableFees0;

    @ApiModelProperty("收费合理性, 0:满意, 1:一般, 2:不满意")
    private String reasonableFees1;

    @ApiModelProperty("收费合理性, 0:满意, 1:一般, 2:不满意")
    private String reasonableFees2;
}

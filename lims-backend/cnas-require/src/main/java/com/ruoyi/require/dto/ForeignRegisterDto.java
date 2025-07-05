package com.ruoyi.require.dto;

import com.deepoove.poi.data.PictureRenderData;
import com.ruoyi.require.pojo.ForeignRegister;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2024/11/19
 */
@Data
public class ForeignRegisterDto extends ForeignRegister {

    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("陪同人员签名")
    private PictureRenderData accompanyingRender;

    @ApiModelProperty("批准人员签名")
    private PictureRenderData approveRender;

    @ApiModelProperty("陪同人员签名地址")
    private String accompanyingUrl;

    @ApiModelProperty("陪同人员签名地址")
    private String approveUrl;
}

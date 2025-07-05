package com.ruoyi.require.dto;

import com.ruoyi.require.pojo.InternalWastes;
import com.ruoyi.require.pojo.InternalWastesDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/19
 */
@Data
public class InternalWastesDto extends InternalWastes {

    @ApiModelProperty("三废登记详情")
    private List<InternalWastesDetail> wastesDetailList;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("修改人")
    private String updateUserName;

}

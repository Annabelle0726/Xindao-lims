package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.InternalCheck;
import com.ruoyi.manage.pojo.InternalCheckDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/11
 */
@Data
public class InternalCheckDto extends InternalCheck {

    @ApiModelProperty("检查详情")
    private List<InternalCheckDetail> checkDetailList;
}

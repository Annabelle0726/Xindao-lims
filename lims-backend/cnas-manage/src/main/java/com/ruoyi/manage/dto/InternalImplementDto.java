package com.ruoyi.manage.dto;

import com.ruoyi.manage.pojo.InternalImplement;
import com.ruoyi.manage.pojo.InternalImplementDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zhuo
 * @Date 2024/11/11
 */
@Data
public class InternalImplementDto extends InternalImplement {

    @ApiModelProperty("计划详情")
    private List<InternalImplementDetail> implementDetailList;
}

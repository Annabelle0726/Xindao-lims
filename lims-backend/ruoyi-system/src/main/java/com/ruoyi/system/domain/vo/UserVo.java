package com.ruoyi.system.domain.vo;

import com.ruoyi.common.core.domain.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVo extends User {

    @ApiModelProperty(value = "部门id")
    private Long departId;
}

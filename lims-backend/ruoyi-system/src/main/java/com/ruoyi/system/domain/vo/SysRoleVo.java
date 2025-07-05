package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhuo
 * @Date 2025/2/20
 */
@Data
public class SysRoleVo {

    @ApiModelProperty("权限标识")
    private String perms;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("是否仅看我, 0: 否, 1:是")
    private Integer isRersonal;
}

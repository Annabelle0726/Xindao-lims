package com.ruoyi.personnel.dto;

import com.ruoyi.common.core.domain.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageDto extends User {

    @ApiModelProperty(value = "创建用户")
    private String createUserName;

    @ApiModelProperty(value = "更新用户")
    private String updateUserName;

    @ApiModelProperty(value = "角色")
    private String roleName;


}

package com.ruoyi.framework.web.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserDetailsByOpenIdServiceImpl")
public class UserDetailsByOpenIdServiceImpl implements UserDetailsService {



    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String openId) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(openId);
        if (StringUtils.isNull(user)) {

            throw new UsernameNotFoundException("登录用户：" + openId + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {

            throw new BaseException("对不起，您的账号：" + openId + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {

            throw new BaseException("对不起，您的账号：" + openId + " 已停用");
        }
  		// 和若依的区别 是没有验证密码
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}

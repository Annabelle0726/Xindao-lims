package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.vo.UserVo;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息表
 *
 * @author zhuo
 * @since 2025-02-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据条件获取用户列表
     *      * todo:  type : 1: 获取检测人员信息
     *               type : 2: 获取当前登录用户部门下的所有用户
     * @param user
     * @param type
     * @return
     */
    @Override
    public List<User> selectUserCondition(User user, String type) {
        if (StringUtils.isNotEmpty(type)) {

            switch (type) {
                case "1":
                    return baseMapper.selectQualityUserList();
                case "2":
                    return baseMapper.selectDepartmentLimsUserList(SecurityUtils.getUserId().intValue());
            }

        }

        return baseMapper.selectUserCondition(QueryWrappers.queryWrappers(user), type);
    }

    /**
     * 获取当前登录的客户信息
     * @return
     */
    @Override
    public UserVo getUserNow() {
        return baseMapper.getUserNow(SecurityUtils.getUserId().intValue());
    }

    /**
     * 获取当前登录用户部门
     * @return
     */
    @Override
    public String selectUserDepartmentLimsName() {
        return baseMapper.selectUserDepartmentLimsName(SecurityUtils.getUserId().intValue());
    }

    /**
     * 修改人员明细所在组织架构
     * @param ids
     * @param id
     * @return
     */
    @Override
    public int upUserDepardLimsId(String ids, String id) {
        List<Integer> userIds = JSON.parseArray(ids, Integer.class);
        return baseMapper.update(null, Wrappers.<User>lambdaUpdate().in(User::getId, userIds).set(User::getDepartLimsId, id).set(User::getUpdateTime, LocalDateTime.now()).set(User::getUpdateBy, SecurityUtils.getLoginUser().getUsername()));
    }

    /**
     * 删除人员明细所在组织架构
     * @param id
     * @return
     */
    @Override
    public int delUserDepardLimsId(Integer id) {
        return baseMapper.update(null, Wrappers.<User>lambdaUpdate().eq(User::getId, id).set(User::getDepartLimsId, null).set(User::getUpdateTime, LocalDateTime.now()).set(User::getUpdateBy, SecurityUtils.getUsername()));
    }
}


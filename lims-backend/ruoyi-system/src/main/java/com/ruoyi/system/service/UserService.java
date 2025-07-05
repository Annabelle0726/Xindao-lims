package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.system.domain.vo.UserVo;

import java.util.List;

/**
 * 用户信息表
 *
 * @author zhuo
 * @since 2025-02-13
 */
public interface UserService extends IService<User> {


    /**
     * 根据条件获取用户列表
     * @param user
     * @param type
     * @return
     */
    List<User> selectUserCondition(User user, String type);

    /**
     * 获取当前登录的客户信息
     * @return
     */
    UserVo getUserNow();

    /**
     * 获取当前登录用户部门
     * @return
     */
    String selectUserDepartmentLimsName();

    /**
     * 修改人员明细所在组织架构
     * @param ids
     * @param id
     * @return
     */
    int upUserDepardLimsId(String ids, String id);

    /**
     * 删除人员明细所在组织架构
     * @param id
     * @return
     */
    int delUserDepardLimsId(Integer id);
}


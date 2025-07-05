package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.system.domain.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表
 *
 * @author zhuo
 * @since 2025-02-13
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据条件获取用户列表
     * @param ew
     * @param type
     * @return
     */
    List<User> selectUserCondition(@Param("ew") QueryWrapper<User> ew, @Param("type") String type);

    /**
     * 获取用户部门
     * @param userId
     * @return
     */
    String selectUserDepartmentLimsName(@Param("userId") Integer userId);

    /**
     * 查询用户和部门
     * @param participant
     * @return
     */
    List<Map<String, String>> selectNameAnddepartment(@Param("participant") String participant);

    /**
     * 获取当前登录的客户信息
     * @param userId
     * @return
     */
    UserVo getUserNow(@Param("userId") int userId);

    /**
     * 获取检测人员信息
     * @return
     */
    List<User> selectQualityUserList();

    /**
     * 获取当前登录用户部门下的所有用户
     * @param userId
     * @return
     */
    List<User> selectDepartmentLimsUserList(@Param("userId") Integer userId);


}


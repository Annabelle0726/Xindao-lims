package com.ruoyi.inspect.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: yuan
 * Date: 2024-12-17 星期二 10:35:50
 * Description: User工具类
 */
@Component
public class UserUtils {
    private static UserMapper userMapper;

    private static String imgUrl;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        UserUtils.userMapper = userMapper;
    }

    @Autowired
    public void setImgUrl(@Value("${file.path}") String imgUrl) {
        UserUtils.imgUrl = imgUrl;
    }

    /**
     * 通过人员id获取用户签名地址
     * @param userId 人员id
     * @return 用户签名地址
     */
    public static String getUserSignatureUrl(Integer userId) {
        String userSignatureUrl = null;
        if (userId != null) {
            userSignatureUrl = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                            .eq(User::getId, userId))
                    .getSignatureUrl();
            if (StringUtils.isBlank(userSignatureUrl)) {
                throw new ErrorException("找不到该人员签名");
            }
            return imgUrl + "\\" + userSignatureUrl;
        } else {
            return null;
        }
    }

    /**
     * 通过人员id获取渲染Word用户签名对象
     * @param userId 人员id
     * @return 用户签名对象 or null
     */
    public static PictureRenderData getFinalUserSignatureUrl(Integer userId) {
        String userSignatureUrl = null;
        if (userId != null) {
            userSignatureUrl = userMapper.selectById(userId)
                    .getSignatureUrl();
            if (StringUtils.isBlank(userSignatureUrl)) {
                throw new ErrorException("找不到该人员签名");
            }
        }
        return StringUtils.isNotBlank(userSignatureUrl) ? Pictures.ofLocal(imgUrl + "/" + userSignatureUrl).create() : null;
    }


    /**
     * 通过名字获取渲染Word用户签名对象
     * @param userName 人员名字
     * @return 用户签名对象 or null
     */
    public static PictureRenderData getFinalUserSignatureUrl(String userName) {
        String userSignatureUrl = null;
        if (userName != null) {
            userSignatureUrl = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                            .eq(User::getName, userName))
                    .getSignatureUrl();
            if (StringUtils.isBlank(userSignatureUrl)) {
                throw new ErrorException("找不到该人员签名");
            }
        }
        return StringUtils.isNotBlank(userSignatureUrl) ? Pictures.ofLocal(imgUrl + "/" + userSignatureUrl).create() : null;
    }
}

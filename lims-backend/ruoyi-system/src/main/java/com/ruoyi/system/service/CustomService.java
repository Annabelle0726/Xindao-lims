package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.Custom;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【user(用户id)】的数据库操作Service
* @createDate 2024年3月7日09:21:59
*/
public interface CustomService extends IService<Custom> {

    IPage<Custom> selectCustomPageList(IPage<Custom> page, Custom custom);

    int delCustomById(Long id);

    int addCustom(Custom custom);

    int upCustom(Custom custom);

    List<Custom> selectCustomEnum();

    Custom getCustomId(String company);
}

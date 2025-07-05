package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonCommunicationAbilityDto;
import com.ruoyi.personnel.pojo.PersonCommunicationAbility;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 沟通能力 服务类
 * </p>
 *
 * @author 
 * @since 2024-10-09 12:00:57
 */
public interface PersonCommunicationAbilityService extends IService<PersonCommunicationAbility> {

    IPage<PersonCommunicationAbilityDto> personPersonCommunicationAbilityPage(Page page,
                                                                              Integer departLimsId, Integer userId, String userName);

    void exportPersonCommunicationAbility(Integer id, HttpServletResponse response)throws Exception;
}

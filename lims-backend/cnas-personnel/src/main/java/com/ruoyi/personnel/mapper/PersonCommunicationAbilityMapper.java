package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonCommunicationAbilityDto;
import com.ruoyi.personnel.pojo.PersonCommunicationAbility;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 沟通能力 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-10-09 12:00:57
 */
public interface PersonCommunicationAbilityMapper extends BaseMapper<PersonCommunicationAbility> {

    IPage<PersonCommunicationAbilityDto> personPersonCommunicationAbilityPage(Page page, @Param("departLimsId") Integer departLimsId, @Param("userId") Integer userId, @Param("userName") String userName);
}

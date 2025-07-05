package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonPersonnelCapacityDto;
import com.ruoyi.personnel.dto.PersonPersonnelCapacityExportDto;
import com.ruoyi.personnel.pojo.PersonPersonnelCapacity;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 人员能力 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-10-10 11:26:18
 */
public interface PersonPersonnelCapacityMapper extends BaseMapper<PersonPersonnelCapacity> {

    IPage<PersonPersonnelCapacityDto> personPersonnelCapacityPage(Page page, @Param("departLimsId") Integer departLimsId,@Param("userId") Integer userId,@Param("userName") String userName);

    /**
     * 查询人员能力接口
     * @param id
     * @return
     */
    PersonPersonnelCapacityExportDto selectExportPersonnelCapacity(@Param("id") Integer id);
}

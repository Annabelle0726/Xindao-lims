package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonJobResponsibilitiesDto;
import com.ruoyi.personnel.pojo.PersonJobResponsibilities;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 岗位职责 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-10-09 02:07:49
 */
public interface PersonJobResponsibilitiesMapper extends BaseMapper<PersonJobResponsibilities> {

    IPage<PersonJobResponsibilitiesDto> personJobResponsibilitiesSelect(Page page, @Param("userId") String userId, @Param("departmentId") String departmentId, @Param("userName") String userName);
}

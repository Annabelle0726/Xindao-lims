package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.personnel.dto.PersonTrainingDto;
import com.ruoyi.personnel.pojo.PersonTraining;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 培训计划 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-10-11 01:11:49
 */
public interface PersonTrainingMapper extends BaseMapper<PersonTraining> {

    IPage<PersonTrainingDto> personTrainingSelect(Page page, @Param("compilerName") String compilerName,@Param("departLimsId") String departLimsId);
}

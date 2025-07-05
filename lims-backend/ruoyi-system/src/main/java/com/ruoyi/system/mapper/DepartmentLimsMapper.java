package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.DepartmentDto;
import com.ruoyi.common.core.domain.entity.DepartmentLims;

import java.util.List;

/**
* @author z1292
* @description 针对表【department_lims(部门明细)】的数据库操作Mapper
* @createDate 2024-05-22 14:08:17
* @Entity com.yuanchu.mom.pojo.DepartmentLims
*/
public interface DepartmentLimsMapper extends BaseMapper<DepartmentLims> {

    //获取部门树
    List<DepartmentDto> selectDepartment();

    //根据部门id,查询他的所有子类id
    List<Integer> selectSonById(Integer id);

}





package com.ruoyi.personnel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.entity.DepartmentDto;
import com.ruoyi.personnel.dto.PersonBasicInfoDto;
import com.ruoyi.personnel.pojo.PersonBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-08-30 09:19:57
 */
public interface PersonBasicInfoMapper extends BaseMapper<PersonBasicInfo> {

    List<DepartmentDto> selectLimsUser();

    PersonBasicInfoDto getCNASPersonnelInfo(Integer userId);

    /**
     * 人员基本信息分页查询
     * @param page
     * @param name
     * @param departmentId
     * @return
     */
    IPage<Map<String, Object>> selectPersonBasecInfoAndUser(Page page, @Param("name") String name, @Param("departmentId") Integer departmentId);

    /**
     * 导出查询人员信息
     * @param userId
     * @return
     */
    Map<String, Object> selectexportPersonBasic(Integer userId);
}

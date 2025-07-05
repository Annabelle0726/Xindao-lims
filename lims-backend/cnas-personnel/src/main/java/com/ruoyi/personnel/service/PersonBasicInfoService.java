package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.DepartmentDto;
import com.ruoyi.common.core.domain.entity.DepartmentLims;
import com.ruoyi.personnel.dto.PersonBasicInfoDto;
import com.ruoyi.personnel.dto.UserPageDto;
import com.ruoyi.personnel.pojo.PersonBasicInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-08-30 09:19:57
 */
public interface PersonBasicInfoService extends IService<PersonBasicInfo> {

    List<DepartmentDto> selectCNSAPersonTree();

    Map<String,Object> getCNASPersonnelInfo(Integer userId);

    void saveCNASPersonnelInfo(PersonBasicInfoDto personBasicInfoDto);

    IPage<Map<String, Object>> basicInformationOfPersonnelSelectPage(Page page, String name, Integer departmentId);

    void exportPersonBasicInfo(UserPageDto userPageDto, HttpServletResponse response) throws Exception;

    String exportPersonBasicInfoById(Integer id, HttpServletResponse response);

    /**
     * 人员培训基本信息附件新增
     * @param basicInfoId
     * @param file
     * @return
     */
    boolean uploadBasicInfoFile(Integer basicInfoId, MultipartFile file);

    /**
     * 添加组织架构
     * @param department
     * @return
     */
    int addDepartment(DepartmentLims department);

    /**
     * 删除组织架构
     * @param id
     * @return
     */
    boolean delDepartment(Integer id);
}

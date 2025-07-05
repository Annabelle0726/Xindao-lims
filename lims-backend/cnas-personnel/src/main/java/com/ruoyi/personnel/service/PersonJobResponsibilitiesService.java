package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonJobResponsibilitiesDto;
import com.ruoyi.personnel.pojo.PersonJobResponsibilities;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 岗位职责 服务类
 * </p>
 *
 * @author
 * @since 2024-10-09 02:07:49
 */
public interface PersonJobResponsibilitiesService extends IService<PersonJobResponsibilities> {

    IPage<PersonJobResponsibilitiesDto> personJobResponsibilitiesSelect(Page page,
                                                                        String userId,
                                                                        String departmentId,
                                                                        String userName);

    void exportPersonJobResponsibilities(Integer id, HttpServletResponse response);
}

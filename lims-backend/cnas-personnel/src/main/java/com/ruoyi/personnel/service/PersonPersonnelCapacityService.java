package com.ruoyi.personnel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.personnel.dto.PersonPersonnelCapacityDto;
import com.ruoyi.personnel.pojo.PersonPersonnelCapacity;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 人员能力 服务类
 * </p>
 *
 * @author
 * @since 2024-10-10 11:26:18
 */
public interface PersonPersonnelCapacityService extends IService<PersonPersonnelCapacity> {

    IPage<PersonPersonnelCapacityDto> personPersonnelCapacityPage(Page page, Integer departLimsId, Integer userId, String userName);

    /**
     * 导出人员能力
     * @param id
     * @param response
     */
    void exportPersonnelCapacity(Integer id, HttpServletResponse response);

    /**
     * submitConfirmPersonnelCapability
     * @param personPersonnelCapacity
     */
    void submitConfirmPersonnelCapability(PersonPersonnelCapacity personPersonnelCapacity);
}

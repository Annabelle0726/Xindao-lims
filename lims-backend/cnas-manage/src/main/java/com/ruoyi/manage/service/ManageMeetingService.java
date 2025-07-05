package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.dto.ManageMeetingDto;
import com.ruoyi.manage.pojo.ManageMeeting;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2024-11-11 09:33:47
 */
public interface ManageMeetingService extends IService<ManageMeeting> {

    IPage<ManageMeetingDto> page(Page page, String startTime, String endTime, String place);

    void addMeeting(ManageMeetingDto dto);

    int modifyMeeting(ManageMeetingDto manageMeetingDto);

    void exportMeeting(Integer id, HttpServletResponse response);
}

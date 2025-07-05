package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.dto.InternalMeetingDto;
import com.ruoyi.manage.pojo.InternalMeeting;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 内审会议表 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-12 02:50:44
 */
public interface InternalMeetingService extends IService<InternalMeeting> {

    /**
     * 内审会议分页查询
     * @param page
     * @param internalMeeting
     * @return
     */
    IPage<InternalMeetingDto> pageInternalMeeting(Page page, InternalMeeting internalMeeting);

    /**
     * 内审会议新增
     * @param internalMeeting
     * @return
     */
    boolean addInternalMeeting(InternalMeetingDto internalMeeting);

    /**
     * 内审会议修改
     * @param internalMeeting
     * @return
     */
    boolean updateInternalMeeting(InternalMeetingDto internalMeeting);

    /**
     * 内审会议删除
     * @param meetingId
     * @return
     */
    boolean delInternalMeeting(Integer meetingId);

    /**
     * 内审会议查看详情
     * @param meetingId
     * @return
     */
    InternalMeetingDto getInternalMeetingOne(Integer meetingId);

    /**
     * 导出内审会议
     * @param meetingId
     * @param response
     */
    void exportInternalMeeting(Integer meetingId, HttpServletResponse response);
}

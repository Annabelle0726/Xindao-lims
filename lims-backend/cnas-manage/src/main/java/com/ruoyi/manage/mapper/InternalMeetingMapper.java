package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.InternalMeetingDto;
import com.ruoyi.manage.pojo.InternalMeeting;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 内审会议表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-12 02:50:44
 */
public interface InternalMeetingMapper extends BaseMapper<InternalMeeting> {

    /**
     * 内审会议分页查询
     * @param page
     * @return
     */
    IPage<InternalMeetingDto> pageInternalMeeting(Page page, @Param("ew") QueryWrapper<InternalMeeting> ew);
}

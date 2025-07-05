package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageMeetingParticipants;
import com.ruoyi.manage.vo.MeetingParticipantsDetailsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2024-11-11 09:34:27
 */
public interface ManageMeetingParticipantsService extends IService<ManageMeetingParticipants> {



    MeetingParticipantsDetailsVo getParticipants(Integer id);
}

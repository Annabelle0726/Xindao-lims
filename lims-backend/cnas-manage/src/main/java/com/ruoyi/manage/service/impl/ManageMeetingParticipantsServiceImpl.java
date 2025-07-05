package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.manage.mapper.ManageMeetingMapper;
import com.ruoyi.manage.mapper.ManageMeetingParticipantsMapper;
import com.ruoyi.manage.pojo.ManageMeeting;
import com.ruoyi.manage.pojo.ManageMeetingParticipants;
import com.ruoyi.manage.service.ManageMeetingParticipantsService;
import com.ruoyi.manage.vo.MeetingParticipantsDetailsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-11 09:34:27
 */
@Service
public class ManageMeetingParticipantsServiceImpl extends ServiceImpl<ManageMeetingParticipantsMapper, ManageMeetingParticipants> implements ManageMeetingParticipantsService {


    @Resource
    private ManageMeetingMapper manageMeetingMapper;

    @Override
    public MeetingParticipantsDetailsVo getParticipants(Integer id) {
        ManageMeeting manageMeeting = manageMeetingMapper.selectById(id);
        QueryWrapper<ManageMeetingParticipants> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meeting_id",id);
        List<ManageMeetingParticipants> participantsList = this.baseMapper.selectList(queryWrapper);
        MeetingParticipantsDetailsVo vo = new MeetingParticipantsDetailsVo();
        vo.setManageMeeting(manageMeeting);
        vo.setParticipantsList(participantsList);
        return vo;
    }
}

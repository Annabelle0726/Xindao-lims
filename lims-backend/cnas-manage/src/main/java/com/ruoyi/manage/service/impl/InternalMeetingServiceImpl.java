package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;

import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.manage.dto.InternalMeetingDto;
import com.ruoyi.manage.dto.InternalMeetingParticipantDto;
import com.ruoyi.manage.mapper.InternalMeetingMapper;

import com.ruoyi.manage.pojo.InternalMeeting;
import com.ruoyi.manage.pojo.InternalMeetingDetail;
import com.ruoyi.manage.service.InternalMeetingDetailService;
import com.ruoyi.manage.service.InternalMeetingService;


import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 内审会议表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-12 02:50:44
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InternalMeetingServiceImpl extends ServiceImpl<InternalMeetingMapper, InternalMeeting> implements InternalMeetingService {

    private InternalMeetingDetailService internalMeetingDetailService;

    private UserMapper userMapper;

    /**
     * 内审会议分页查询
     * @param page
     * @param internalMeeting
     * @return
     */
    @Override
    public IPage<InternalMeetingDto> pageInternalMeeting(Page page, InternalMeeting internalMeeting) {
        return baseMapper.pageInternalMeeting(page, QueryWrappers.queryWrappers(internalMeeting));
    }

    /**
     * 内审会议新增
     * @param internalMeeting
     * @return
     */
    @Override
    public boolean addInternalMeeting(InternalMeetingDto internalMeeting) {

        baseMapper.insert(internalMeeting);
//        // 新增详情
//        for (InternalMeetingDetail internalMeetingDetail : internalMeeting.getMeetingDetailList()) {
//            internalMeetingDetail.setMeetingId(internalMeeting.getMeetingId());
//        }
//        internalMeetingDetailService.saveBatch(internalMeeting.getMeetingDetailList());
        return true;
    }

    /**
     * 内审会议修改
     * @param internalMeeting
     * @return
     */
    @Override
    public boolean updateInternalMeeting(InternalMeetingDto internalMeeting) {
        baseMapper.updateById(internalMeeting);

        // 删除之前的详情
//        internalMeetingDetailService.remove(Wrappers.<InternalMeetingDetail>lambdaQuery()
//                .eq(InternalMeetingDetail::getMeetingId, internalMeeting.getMeetingId()));
//
//        // 新增详情
//        for (InternalMeetingDetail internalMeetingDetail : internalMeeting.getMeetingDetailList()) {
//            internalMeetingDetail.setMeetingId(internalMeeting.getMeetingId());
//        }
        internalMeetingDetailService.saveBatch(internalMeeting.getMeetingDetailList());

        return true;
    }

    /**
     * 内审会议删除
     * @param MeetingId
     * @return
     */
    @Override
    public boolean delInternalMeeting(Integer MeetingId) {
        internalMeetingDetailService.remove(Wrappers.<InternalMeetingDetail>lambdaQuery()
                .eq(InternalMeetingDetail::getMeetingId, MeetingId));
        baseMapper.deleteById(MeetingId);
        return true;
    }

    /**
     * 内审会议查看详情
     * @param MeetingId
     * @return
     */
    @Override
    public InternalMeetingDto getInternalMeetingOne(Integer MeetingId) {
        InternalMeeting internalMeeting = baseMapper.selectById(MeetingId);
        InternalMeetingDto internalMeetingDto = new InternalMeetingDto();
        BeanUtils.copyProperties(internalMeeting, internalMeetingDto);

        // 查询详细信息
        internalMeetingDto.setMeetingDetailList(internalMeetingDetailService.list(Wrappers.<InternalMeetingDetail>lambdaQuery()
                .eq(InternalMeetingDetail::getMeetingId, MeetingId)));
        return internalMeetingDto;
    }

    /**
     * 导出内审会议
     * @param meetingId
     * @param response
     */
    @Override
    public void exportInternalMeeting(Integer meetingId, HttpServletResponse response) {
        InternalMeeting internalMeeting = baseMapper.selectById(meetingId);

        // 最大行数
        int max = 11;

        // 查询参加人员
        List<Map<String, String>> mapList = userMapper.selectNameAnddepartment(internalMeeting.getParticipant());

        // 创建空对象
        List<InternalMeetingParticipantDto> participants = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            InternalMeetingParticipantDto participant = new InternalMeetingParticipantDto();
            participants.add(participant);
        }

        int count = 0;
        // 添加参加人员
        for (Map<String, String> stringMap : mapList) {
            // 判断有没有到11行
            if (count >= max * 2) {
                participants.get(count - max * 2).setUserName3(stringMap.get("userName"));
                participants.get(count - max * 2).setDepartment3(stringMap.get("department"));
            } else if (count >= max){
                participants.get(count - max).setUserName2(stringMap.get("userName"));
                participants.get(count - max).setDepartment2(stringMap.get("department"));
            } else {
                participants.get(count).setUserName1(stringMap.get("userName"));
                participants.get(count).setDepartment1(stringMap.get("department"));
            }
            count ++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/internal-meeting.docx");
        Configure configure = Configure.builder()
                .bind("meetingDetails", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("meeting", internalMeeting);
                    put("meetingDetails", participants);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    internalMeeting.getMeetingDate() + "内审会议签到", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileName + ".docx");
            OutputStream os = response.getOutputStream();
            template.write(os);
            os.flush();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }

    }


}

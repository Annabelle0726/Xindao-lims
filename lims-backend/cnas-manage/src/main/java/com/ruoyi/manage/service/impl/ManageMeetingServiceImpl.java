package com.ruoyi.manage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.manage.dto.ManageMeetingDto;
import com.ruoyi.manage.dto.ManageMeetingParticipantsDto;
import com.ruoyi.manage.mapper.ManageMeetingMapper;
import com.ruoyi.manage.mapper.ManageMeetingParticipantsMapper;

import com.ruoyi.manage.pojo.ManageMeeting;
import com.ruoyi.manage.pojo.ManageMeetingParticipants;

import com.ruoyi.manage.service.ManageMeetingService;

import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-11 09:33:47
 */
@Service
public class ManageMeetingServiceImpl extends ServiceImpl<ManageMeetingMapper, ManageMeeting> implements ManageMeetingService {


    @Resource
    private ManageMeetingParticipantsMapper manageMeetingParticipantsMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<ManageMeetingDto> page(Page page, String startTime, String endTime, String place) {
        IPage<ManageMeetingDto> iPage = this.baseMapper.page(page, startTime, endTime, place);
        for (ManageMeetingDto record : iPage.getRecords()) {
            List<ManageMeetingParticipants> manageMeetingParticipants = manageMeetingParticipantsMapper.selectList(Wrappers.<ManageMeetingParticipants>lambdaQuery().eq(ManageMeetingParticipants::getMeetingId, record.getId()));
            String collect = manageMeetingParticipants.stream().map(manageMeetingParticipants1 -> {
                return manageMeetingParticipants1.getParticipants() + "";
            }).collect(Collectors.joining(","));
            record.setParticipant(collect);
        }
        return iPage;
    }

    @Override
    public void addMeeting(ManageMeetingDto dto) {
        this.baseMapper.insert(dto);

        String[] ids = dto.getParticipant().split(",");
        List<ManageMeetingParticipants> list = new ArrayList<>();
        for (String id : ids) {
            User user = userMapper.selectById(id);
            ManageMeetingParticipants participants = new ManageMeetingParticipants();
            participants.setMeetingId(dto.getId());
            participants.setParticipants(Integer.parseInt(id));
            participants.setDepartment(userMapper.selectUserDepartmentLimsName(user.getId()));
            list.add(participants);
        }
        list.forEach(v -> manageMeetingParticipantsMapper.insert(v));
    }

    @Override
    public int modifyMeeting(ManageMeetingDto manageMeetingDto) {
        this.baseMapper.updateById(manageMeetingDto);
        manageMeetingParticipantsMapper.delete(Wrappers.<ManageMeetingParticipants>lambdaQuery().eq(ManageMeetingParticipants::getMeetingId, manageMeetingDto.getId()));
        String[] ids = manageMeetingDto.getParticipant().split(",");
        List<ManageMeetingParticipants> list = new ArrayList<>();
        for (String id : ids) {
            User user = userMapper.selectById(id);
            ManageMeetingParticipants participants = new ManageMeetingParticipants();
            participants.setMeetingId(manageMeetingDto.getId());
            participants.setParticipants(Integer.parseInt(id));
            participants.setDepartment(userMapper.selectUserDepartmentLimsName(user.getId()));
            list.add(participants);
        }
        list.forEach(v -> manageMeetingParticipantsMapper.insert(v));
        return 0;
    }

    @Override
    public void exportMeeting(Integer id, HttpServletResponse response) {
        ManageMeeting meeting = baseMapper.selectById(id);
        // 查询参加人员
        List<ManageMeetingParticipants> manageMeetingParticipants = manageMeetingParticipantsMapper.selectList(Wrappers.<ManageMeetingParticipants>lambdaQuery().eq(ManageMeetingParticipants::getMeetingId, id));

        List<ManageMeetingParticipants> list = manageMeetingParticipants.stream().map(manageMeetingParticipants1 -> {
            manageMeetingParticipants1.setUserName(userMapper.selectById(manageMeetingParticipants1.getParticipants()).getName());
            manageMeetingParticipants1.setDepartment(userMapper.selectUserDepartmentLimsName(manageMeetingParticipants1.getParticipants()));
            return manageMeetingParticipants1;
        }).collect(Collectors.toList());


        // 创建空对象
        List<ManageMeetingParticipantsDto> participants = new ArrayList<>();

        // 添加参加人员
        for (int i = 0; i < list.size(); i++) {
            // 判断有没有到11行
            if (i % 2 == 0) {
                ManageMeetingParticipantsDto manageMeetingParticipantsDto = new ManageMeetingParticipantsDto();
                manageMeetingParticipantsDto.setUserName1(list.get(i).getUserName());
                manageMeetingParticipantsDto.setDepartment1(list.get(i).getDepartment());
                participants.add(manageMeetingParticipantsDto);
            } else {
                participants.get((i-1)/2).setUserName2(list.get(i).getUserName());
                participants.get((i-1)/2).setDepartment2(list.get(i).getDepartment());
            }
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/static/review-meet.docx");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        Configure configure = Configure.builder()
                .bind("meetingDetails", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("meeting", meeting);
                    put("meetingTime", meeting.getMeetingTime().format(formatter));
                    put("meetingDetails", participants);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "管理评审会议记录", "UTF-8");
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

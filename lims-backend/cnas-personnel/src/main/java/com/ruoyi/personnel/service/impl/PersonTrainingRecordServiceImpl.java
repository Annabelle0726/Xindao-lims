package com.ruoyi.personnel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.personnel.dto.PersonTrainingRecordDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordListDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordSubmitDto;
import com.ruoyi.personnel.dto.TrainingRecordPersonDetailedDto;
import com.ruoyi.personnel.mapper.PersonTrainingRecordMapper;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import com.ruoyi.personnel.pojo.PersonTrainingRecord;
import com.ruoyi.personnel.service.PersonTrainingDetailedService;
import com.ruoyi.personnel.service.PersonTrainingRecordService;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 培训记录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-12 04:50:48
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PersonTrainingRecordServiceImpl extends ServiceImpl<PersonTrainingRecordMapper, PersonTrainingRecord> implements PersonTrainingRecordService {

    @Autowired
    private PersonTrainingDetailedService personTrainingDetailedService;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<PersonTrainingRecordDto> trainingAndAssessmentRecordsPage(Integer trainingDetailedId, String userName) {
        return baseMapper.trainingAndAssessmentRecordsPage(trainingDetailedId, userName);
    }

    @Override
    public void deleteTrainingAndAssessmentRecords(String ids) {
        String[] split = ids.split(",");
        if (split.length > 0) {
            for (String s : split) {
                baseMapper.deleteById(s);
            }
        }
    }

    @Override
    public IPage<PersonTrainingRecordListDto> personnelTrainingPersonnel(Page page, String userName, Integer userId, Integer departLimsId) {
        return baseMapper.personnelTrainingPersonnel(page, userName, userId, departLimsId);
    }

    @Override
    public void claimOfTrainingAndAssessmentRecords(Boolean claimAndClaim, Integer courseId) {

    }


    @Override
    public IPage<TrainingRecordPersonDetailedDto> queryPersonnelDetailsOfUserIdAndYear(Page page, Integer userId, Integer year) {
        return baseMapper.queryPersonnelDetailsOfUserIdAndYear(page, userId, year);
    }

    @Override
    public void exportTrainingRecordAddTrainingDate(Integer userId, Integer trainingDate, HttpServletResponse response) {
        // 查询人员人信息
        PersonTrainingRecordListDto trainingRecordListDto = baseMapper.selectUserTraining(userId);

        // 查询培训记录
        List<TrainingRecordPersonDetailedDto> personDetailedDtos = baseMapper.selectPersonDetailedDtosByTrainingDate(userId, trainingDate);


        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/training-record.docx");
        Configure configure = Configure.builder()
                .bind("personnelDetailsLisat", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("traning", trainingRecordListDto);
                    put("personnelDetailsLisat", personDetailedDtos);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "人员培训记录导出", "UTF-8");
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

    /**
     * 培训提交
     * @param personTrainingRecordSubmitDto
     */
    @Override
    public void trainingAndAssessmentRecordsAdded(PersonTrainingRecordSubmitDto personTrainingRecordSubmitDto) {
        personTrainingDetailedService.update(Wrappers.<PersonTrainingDetailed>lambdaUpdate()
                .eq(PersonTrainingDetailed::getId, personTrainingRecordSubmitDto.getTrainingDetailedId())
                .set(PersonTrainingDetailed::getAssessmentMethod, personTrainingRecordSubmitDto.getAssessmentMethod())
                .set(PersonTrainingDetailed::getPlaceTraining, personTrainingRecordSubmitDto.getPlaceTraining())
                .set(PersonTrainingDetailed::getOpeningTime, personTrainingRecordSubmitDto.getOpeningTime())
                .set(PersonTrainingDetailed::getAssessmentUserId, personTrainingRecordSubmitDto.getAssessmentUserId())
                .set(PersonTrainingDetailed::getState, personTrainingRecordSubmitDto.getState()));

        // 发送消息通知
        if (personTrainingRecordSubmitDto.getState().equals(2)) {
            PersonTrainingDetailed personPersonnelCapacity = personTrainingDetailedService.getById(personTrainingRecordSubmitDto.getTrainingDetailedId());

            Integer userId = SecurityUtils.getUserId().intValue();
            User user = userMapper.selectById(userId);
            // 消息发送
            InformationNotification info = new InformationNotification();
            // 发送人
            info.setCreateUser(user.getName());
            info.setMessageType("6");
            info.setTheme("CNAS人员培训计划待评价");
            info.setContent("培训内容:" + personPersonnelCapacity.getTrainingContent() + "的人员培训待评价");
            info.setSenderId(userId);
            // 接收人
            info.setConsigneeId(personTrainingRecordSubmitDto.getAssessmentUserId());
            info.setJumpPath(MenuJumpPathConstants.PERSONNEL);
            informationNotificationService.addInformationNotification(info);

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询接收人
                User personnel = userMapper.selectById(personTrainingRecordSubmitDto.getAssessmentUserId());

                String message = "";
                message += "CNAS人员培训计划待评价";
                message += "\n请去资源管理-人员-人员培训计划";
                message += "\n" + "培训内容:" + personPersonnelCapacity.getTrainingContent() + "的人员培训待评价";
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(personnel.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}

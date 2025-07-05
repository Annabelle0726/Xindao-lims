package com.ruoyi.personnel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.personnel.dto.PersonTrainingDetailedDto;
import com.ruoyi.personnel.dto.PersonTrainingDto;
import com.ruoyi.personnel.dto.PersonTrainingRecordDto;
import com.ruoyi.personnel.dto.TrainingRecordExportDto;
import com.ruoyi.personnel.excel.PersonTrainingDetailedListener;
import com.ruoyi.personnel.excel.PersonTrainingDetailedUpload;
import com.ruoyi.personnel.mapper.PersonTrainingDetailedFileMapper;
import com.ruoyi.personnel.mapper.PersonTrainingDetailedMapper;
import com.ruoyi.personnel.mapper.PersonTrainingMapper;
import com.ruoyi.personnel.mapper.PersonTrainingRecordMapper;
import com.ruoyi.personnel.pojo.PersonTraining;
import com.ruoyi.personnel.pojo.PersonTrainingDetailed;
import com.ruoyi.personnel.pojo.PersonTrainingDetailedFile;
import com.ruoyi.personnel.service.PersonTrainingDetailedService;
import com.ruoyi.personnel.service.PersonTrainingService;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 培训计划 服务实现类
 * </p>
 *
 * @author
 * @since 2024-10-11 01:11:49
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PersonTrainingServiceImpl extends ServiceImpl<PersonTrainingMapper, PersonTraining> implements PersonTrainingService {

    @Autowired
    private PersonTrainingDetailedService personTrainingDetailedService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PersonTrainingDetailedMapper personTrainingDetailedMapper;
    @Autowired
    private PersonTrainingRecordMapper personTrainingRecordMapper;
    @Autowired
    private PersonTrainingDetailedFileMapper personTrainingDetailedFileMapper;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;

    @Override
    public IPage<PersonTrainingDto> personTrainingSelect(Page page, String compilerName, String departmentId) {

        return baseMapper.personTrainingSelect(page, compilerName, departmentId);
    }

    @Override
    public void personTrainingImport(MultipartFile file, PersonTraining training) {

        Integer userId = SecurityUtils.getUserId().intValue();
        // 年度计划父级新增数据
        PersonTraining personSupervisePlan = new PersonTraining();
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        personSupervisePlan.setFileName(fileName);
        personSupervisePlan.setPlanYear(training.getPlanYear());
        personSupervisePlan.setCompilerId(userId);
        personSupervisePlan.setReviewerId(training.getReviewerId());
        personSupervisePlan.setCompilationDate(LocalDateTime.now());
        baseMapper.insert(personSupervisePlan);
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS培训计划审核通知");
        info.setContent("您有一条培训计划待审核");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(training.getApproverId());
        info.setJumpPath(MenuJumpPathConstants.PERSONNEL);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询接收人
            User personnel = userMapper.selectById(training.getApproverId());

            String message = "";
            message += "CNAS培训计划审核通知";
            message += "\n请去资源管理-人员-培训计划填写";
            message += "\n" + fileName + "的培训计划待审核";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(personnel.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // 年度计划详情 新增
        try {
            PersonTrainingDetailedListener personSupervisePlanDetailsListener = new PersonTrainingDetailedListener(personTrainingDetailedService);
            personSupervisePlanDetailsListener.setPlanId(personSupervisePlan.getId());
            EasyExcel.read(file.getInputStream(), PersonTrainingDetailedUpload.class, personSupervisePlanDetailsListener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personTrainingDelete(Integer id) {
        personTrainingDetailedService.remove(Wrappers.<PersonTrainingDetailed>lambdaQuery()
                .eq(PersonTrainingDetailed::getPlanId, id));
        baseMapper.deleteById(id);
    }

    @Override
    public void reviewAnnualPersonnelTraining(PersonTraining training) {
        PersonTraining personTraining = new PersonTraining();
        personTraining.setId(training.getId());
        personTraining.setApproverId(training.getApproverId());// 添加批准人
        personTraining.setAuditDate(LocalDateTime.now());
        personTraining.setAuditRemarks(training.getAuditRemarks());
        personTraining.setReviewerStatus(training.getReviewerStatus());

        PersonTraining old = baseMapper.selectById(training.getId());

        // 消息发送
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS培训计划审核通知");
        info.setContent("您有一条培训计划待批准");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(training.getApproverId());
        info.setJumpPath(MenuJumpPathConstants.PERSONNEL);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询接收人
            User personnel = userMapper.selectById(training.getApproverId());

            String message = "";
            message += "CNAS培训计划批准通知";
            message += "\n请去资源管理-人员-培训计划填写";
            message += "\n" + old.getFileName() + "的培训计划待批准";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(personnel.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        baseMapper.updateById(personTraining);
    }

    @Override
    public void approveAnnualPersonnelTraining(PersonTraining training) {
        LambdaUpdateWrapper<PersonTraining> wrapper = Wrappers.<PersonTraining>lambdaUpdate()
                .eq(PersonTraining::getId, training.getId())
                .set(PersonTraining::getApprovalDate, LocalDateTime.now())
                .set(PersonTraining::getApprovalRemarks, training.getApprovalRemarks())
                .set(PersonTraining::getApprovalStatus, training.getApprovalStatus());
        baseMapper.update(new PersonTraining(), wrapper);
    }

    /**
     * 导出人员培训计划
     * @param id
     * @param response
     */
    @Override
    public void exportPersonTraining(Integer id, HttpServletResponse response) {

        // 查询详情
        PersonTraining personTraining = baseMapper.selectById(id);

        //获取提交人的签名地址
        String writeUrl = userMapper.selectById(personTraining.getCompilerId()).getSignatureUrl();
        if (ObjectUtils.isEmpty(writeUrl) || writeUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取复核人的签名地址
        String examineUrl = null;
        if (personTraining.getReviewerId() != null) {
            examineUrl = userMapper.selectById(personTraining.getReviewerId()).getSignatureUrl();
            if (StringUtils.isBlank(examineUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (personTraining.getApproverId() != null) {
            ratifyUrl = userMapper.selectById(personTraining.getApproverId()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        // 查询详情
        List<PersonTrainingDetailedDto> detailedDtos = personTrainingDetailedMapper.selectTrainingList(id);

        int index = 1;
        for (PersonTrainingDetailedDto detailedDto : detailedDtos) {
            detailedDto.setTrainingDateString(detailedDto.getTrainingDate());
            detailedDto.setIndex(index);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/person-training.docx");
        String finalExamineUrl = examineUrl;
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("trainingDetailedList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("year", personTraining.getPlanYear());
                    put("trainingDetailedList", detailedDtos);
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("examineUrl", StringUtils.isNotBlank(finalExamineUrl) ? Pictures.ofLocal(imgUrl + "/" + finalExamineUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                    put("writeDateUrl", personTraining.getCompilationDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(personTraining.getCompilationDate())).create() : null);
                    put("examineDateUrl", personTraining.getAuditDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(personTraining.getAuditDate())).create() : null);
                    put("ratifyDateUrl", personTraining.getApprovalDate() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(personTraining.getApprovalDate())).create() : null);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "人员培训计划导出", "UTF-8");
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
     * 导出人员培训与考核记录
     * @param id
     * @param response
     */
    @Override
    public void exportPersonTrainingRecord(Integer id, HttpServletResponse response) {
        // 查询人员培训明细
        PersonTrainingDetailedDto detailedDto = personTrainingDetailedMapper.selectTrainingDetail(id);

        // 生成培训老师签名
        detailedDto.setTrainingLecturerRender(StringUtils.isNotBlank(detailedDto.getTrainingLecturerSignatureUrl())
                    ? Pictures.ofLocal(imgUrl + "/" + detailedDto.getTrainingLecturerSignatureUrl()).create() : null);

        // 查询培训的人员
        List<PersonTrainingRecordDto> recordDtos = personTrainingRecordMapper.selectListByTrainingDetailedId(id);

        List<TrainingRecordExportDto> exportDtoList = new ArrayList<>();
        TrainingRecordExportDto exportDto = new TrainingRecordExportDto();

        int count = 0;
        for (PersonTrainingRecordDto recordDto : recordDtos) {
            switch (count) {
                case 0:
                    exportDto.setUserNameRender1(StringUtils.isNotBlank(recordDto.getSignatureUrl())
                            ? Pictures.ofLocal(imgUrl + "/" + recordDto.getSignatureUrl()).create() : null);
                    exportDto.setDepartment1(recordDto.getDepartment());
                    exportDto.setExaminationResults1(recordDto.getExaminationResults());
                    count ++;
                    break;
                case 1:
                    exportDto.setUserNameRender2(StringUtils.isNotBlank(recordDto.getSignatureUrl())
                            ? Pictures.ofLocal(imgUrl + "/" + recordDto.getSignatureUrl()).create() : null);
                    exportDto.setDepartment2(recordDto.getDepartment());
                    exportDto.setExaminationResults2(recordDto.getExaminationResults());
                    exportDtoList.add(exportDto);
                    exportDto = new TrainingRecordExportDto();
                    count = 0;
                    break;
            }
        }
        exportDtoList.add(exportDto);

        // 质量负责人
        String assessmentUserUrl = null;
        if (detailedDto.getAssessmentUserId() != null) {
            assessmentUserUrl = userMapper.selectById(detailedDto.getAssessmentUserId()).getSignatureUrl();
            if (StringUtils.isBlank(assessmentUserUrl)) {
                throw new ErrorException("找不到评价人的签名");
            }
        }


        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/person-training-record.docx");
        Configure configure = Configure.builder()
                .bind("trainingRecordsList", new HackLoopTableRenderPolicy())
                .build();
        String finalAssessmentUserUrl = assessmentUserUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("trainingDetail", detailedDto);
                    put("trainingRecordsList", exportDtoList);
                    put("assessmentUserUrl", StringUtils.isNotBlank(finalAssessmentUserUrl) ? Pictures.ofLocal(imgUrl + "/" + finalAssessmentUserUrl).create() : null);
                }});
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "培训与考核记录导出", "UTF-8");
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
     * 人员培训详情附件新增
     * @param trainingDetailedId
     * @param file
     * @return
     */
    @Override
    public boolean uploadTrainingDetailedFile(Integer trainingDetailedId, MultipartFile file) {
        if (trainingDetailedId == null) {
            throw new ErrorException("缺少验收id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        PersonTrainingDetailedFile detailedFile = new PersonTrainingDetailedFile();
        detailedFile.setTrainingDetailedId(trainingDetailedId);
        detailedFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            detailedFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            detailedFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            detailedFile.setFileUrl(pathName);
            personTrainingDetailedFileMapper.insert(detailedFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return false;
        }
    }

    /**
     * 查询今年人员培训信息
     * @return
     */
    @Override
    public List<PersonTrainingDetailed> getThisYearTrainingDetailed() {
        return personTrainingDetailedMapper.getThisYearTrainingDetailed();
    }
}

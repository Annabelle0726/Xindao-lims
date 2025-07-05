package com.ruoyi.process.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.DateImageUtil;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.pojo.DeviceExamineRecord;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.process.dto.QualitySuperviseDetailsAccordingDto;
import com.ruoyi.process.dto.QualitySuperviseDetailsCorrectDto;
import com.ruoyi.process.dto.QualitySuperviseDetailsDto;
import com.ruoyi.process.excel.QualitySuperviseDetailsUpload;
import com.ruoyi.process.mapper.*;
import com.ruoyi.process.pojo.*;
import com.ruoyi.process.service.QualitySuperviseDetailsService;
import com.ruoyi.process.service.QualitySuperviseService;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.springframework.beans.BeanUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质量监督主表
 *
 * @author zhuo
 * @since 2024-11-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QualitySuperviseServiceImpl extends ServiceImpl<QualitySuperviseMapper, QualitySupervise> implements QualitySuperviseService {

    @Resource
    private QualitySuperviseDetailsService qualitySuperviseDetailsService;
    @Resource
    private QualitySuperviseDetailsMapper qualitySuperviseDetailsMapper;
    @Resource
    private QualitySuperviseDetailsRecordMapper qualitySuperviseDetailsRecordMapper;
    @Resource
    private QualitySuperviseDetailsAccordingMapper qualitySuperviseDetailsAccordingMapper;
    @Resource
    private QualitySuperviseDetailsCorrectMapper qualitySuperviseDetailsCorrectMapper;
    @Resource
    private QualitySuperviseDetailsCorrectFileMapper qualitySuperviseDetailsCorrectFileMapper;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserMapper userMapper;
    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;

    /**
     * 导入监督计划
     * @param file
     * @return
     */
    @Override
    public boolean importQualitySupervise(MultipartFile file, QualitySupervise supervise) {
        if (supervise.getRatifyUserId() == null) {
            throw new ErrorException("缺少批准人");
        }
        User ratifyUser = userMapper.selectById(supervise.getRatifyUserId());

        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);

        // 文件名称
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        QualitySupervise qualitySupervise = new QualitySupervise();
        qualitySupervise.setSuperviseName(fileName);
        qualitySupervise.setSuperviseYear(supervise.getSuperviseYear());
        qualitySupervise.setRecordUserIds(supervise.getRecordUserIds());
        qualitySupervise.setWriteUserId(userId);
        qualitySupervise.setWriteUserName(user.getName());
        qualitySupervise.setWriteTime(LocalDateTime.now());
        qualitySupervise.setRatifyUserId(supervise.getRatifyUserId());
        qualitySupervise.setRatifyUserName(ratifyUser.getName());
        baseMapper.insert(qualitySupervise);

        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS质量监督计划审核通知");
        info.setContent("您有一条质量监督计划待批准");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(supervise.getRatifyUserId());
        info.setJumpPath(MenuJumpPathConstants.QUALITY_SUPERVISE);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询发送人
            User people = userMapper.selectById(supervise.getRatifyUserId());
            String message = "";
            message += "CNAS质量监督计划批准通知";
            message += "\n请去过程要求-质量监督计划";
            message += "\n" + fileName + "质量监督计划待批准";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(people.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        List<QualitySuperviseDetails> detailsUploadList = new ArrayList<>();

        // 导入附件内容
        try {
            // excel解析
            EasyExcel.read(file.getInputStream(), QualitySuperviseDetailsUpload.class, new AnalysisEventListener<QualitySuperviseDetailsUpload>() {
                @Override
                public void invoke(QualitySuperviseDetailsUpload detailsUpload, AnalysisContext analysisContext) {
                    // 判断是否为空
                    if (StringUtils.isBlank(detailsUpload.getSuperviseTime())) {
                        throw new ErrorException("监督日期不能为空");
                    }
                    // 对象复制
                    QualitySuperviseDetails superviseDetails = new QualitySuperviseDetails();
                    BeanUtils.copyProperties(detailsUpload, superviseDetails);
                    superviseDetails.setSuperviseId(qualitySupervise.getSuperviseId());

                    User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                            .eq(User::getName, superviseDetails.getSupervisee()));
                    if (ObjectUtils.isEmpty(user)) {
                        throw new ErrorException("未找到被监督员：" + superviseDetails.getSupervisee());
                    }
                    superviseDetails.setSupervisedUserId(user.getId());
                    // 格式化时间
                    superviseDetails.setSuperviseTime(detailsUpload.getSuperviseTime());

                    detailsUploadList.add(superviseDetails);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).sheet().doRead();
            qualitySuperviseDetailsService.saveBatch(detailsUploadList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 监督计划批准
     * @param qualitySupervise
     * @return
     */
    @Override
    public boolean ratifyQualitySupervise(QualitySupervise qualitySupervise) {
        // 当前登录用户
        baseMapper.update(null, Wrappers.<QualitySupervise>lambdaUpdate()
                .eq(QualitySupervise::getSuperviseId, qualitySupervise.getSuperviseId())
                .set(QualitySupervise::getRatifyRemark, qualitySupervise.getRatifyRemark())
                .set(QualitySupervise::getRatifyStatus, qualitySupervise.getRatifyStatus())
                .set(QualitySupervise::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 监督计划列表
     * @param page
     * @param qualitySupervise
     * @return
     */
    @Override
    public IPage<QualitySupervise> pageQualitySupervise(Page page, QualitySupervise qualitySupervise) {
        return baseMapper.pageQualitySupervise(page, QueryWrappers.queryWrappers(qualitySupervise));
    }

    /**
     * 监督计划详情列表
     * @return
     */
    @Override
    public IPage<QualitySuperviseDetailsDto> pageQualitySuperviseDetail(Page page, QualitySuperviseDetailsDto qualitySuperviseDetails) {
        if (qualitySuperviseDetails.getSuperviseId() == null) {
            return new Page();
        }
        Integer causeType = qualitySuperviseDetails.getCauseType();
        qualitySuperviseDetails.setCauseType(null);
        return qualitySuperviseDetailsMapper.pageQualitySuperviseDetail(page, QueryWrappers.queryWrappers(qualitySuperviseDetails), causeType);
    }

    /**
     * 查询该计划监督员
     * @param superviseDetailsId
     * @return
     */
    @Override
    public List<Map<String, String>> getRecordUser(Integer superviseDetailsId) {
        List<Map<String, String>> recordUser = baseMapper.getRecordUser(superviseDetailsId);
        return baseMapper.getRecordUser(superviseDetailsId);
    }

    /**
     * 导出监督计划
     * @param superviseId
     * @param response
     */
    @Override
    public void exportQualitySupervise(Integer superviseId, HttpServletResponse response) {
        QualitySupervise qualitySupervise = baseMapper.selectById(superviseId);
        //获取提交人的签名地址
        String writeUrl = userMapper.selectById(qualitySupervise.getWriteUserId()).getSignatureUrl();
        if (ObjectUtils.isEmpty(writeUrl) || writeUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (qualitySupervise.getRatifyUserId() != null) {
            ratifyUrl = userMapper.selectById(qualitySupervise.getRatifyUserId()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        // 定义一个集合存放人员签名
        ArrayList<PictureRenderData> recordUserDataList = new ArrayList<>();
        // TODO:确认最多会有5个人
        String recordUserIds = qualitySupervise.getRecordUserIds();
        if (StringUtils.isNotBlank(recordUserIds)) {
            // 对人员id字符串进行分割成数组
            String[] userIds = recordUserIds.split(",");
            // 循环获取人员签名
            for (String userIdStr : userIds) {
                // 转换为int类型
                Integer userId = Integer.valueOf(userIdStr);
                // 获取人员签名对象
                PictureRenderData finalUserSignatureUrl = UserUtils.getFinalUserSignatureUrl(userId);
                // 将人员签名对象添加到集合中
                recordUserDataList.add(finalUserSignatureUrl);
            }
        }

        // 判断集合长度，并补null到2个
        while (recordUserDataList.size() < 2) {
            recordUserDataList.add(null);
        }

        // 查询详情
        List<QualitySuperviseDetails> qualitySuperviseDetails = qualitySuperviseDetailsMapper.selectList(Wrappers.<QualitySuperviseDetails>lambdaQuery()
                .eq(QualitySuperviseDetails::getSuperviseId, superviseId));

        int index = 1;
        for (QualitySuperviseDetails qualitySuperviseDetail : qualitySuperviseDetails) {
            qualitySuperviseDetail.setIndex(index);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/quality-supervise.docx");
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("superviseDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("year", qualitySupervise.getSuperviseYear());
                    put("superviseDetailList", qualitySuperviseDetails);
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                    put("writeDateUrl", qualitySupervise.getWriteTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(qualitySupervise.getWriteTime())).create() : null);
                    put("ratifyDateUrl", qualitySupervise.getRatifyTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(qualitySupervise.getRatifyTime())).create() : null);
                    put("recordUserUrl1", recordUserDataList.get(0));
                    put("recordUserUrl2", recordUserDataList.get(1));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    qualitySupervise.getSuperviseName(), "UTF-8");
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

    /************************************************ 记录 ******************************************************/

    /**
     * 查询监督记录信息
     * @param superviseDetailsId
     * @return
     */
    @Override
    public QualitySuperviseDetailsRecord getSuperviseDetailRecord(Integer superviseDetailsId) {
        QualitySuperviseDetailsRecord detailsRecord;
        detailsRecord = qualitySuperviseDetailsRecordMapper.selectOne(Wrappers.<QualitySuperviseDetailsRecord>lambdaQuery()
                .eq(QualitySuperviseDetailsRecord::getSuperviseDetailsId, superviseDetailsId));

        // 查询上一个月第一条的记录信息, 判断被监督人去另外一个
        if (detailsRecord == null) {
            // 查询详情信息
            QualitySuperviseDetails qualitySuperviseDetails = qualitySuperviseDetailsMapper.selectById(superviseDetailsId);
            // 查询上一个月第一条的记录信息, 判断被监督人去另外一个
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.M");
            YearMonth yearMonth = YearMonth.parse(qualitySuperviseDetails.getSuperviseTime(), formatter);
            YearMonth previousYearMonth = yearMonth.minusMonths(1);

            LocalDate startDate = previousYearMonth.atDay(1);
            LocalDate endDate = previousYearMonth.atEndOfMonth();

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 获取开始时间和结束时间
            String startDateTimeStr = startDateTime.format(outputFormatter);
            String endDateTimeStr = endDateTime.format(outputFormatter);

            QualitySuperviseDetailsRecord laseRecord = qualitySuperviseDetailsRecordMapper.selectOne(Wrappers.<QualitySuperviseDetailsRecord>lambdaQuery()
                    .between(QualitySuperviseDetailsRecord::getCreateTime, startDateTimeStr, endDateTimeStr)
                    .isNotNull(QualitySuperviseDetailsRecord::getSupervisor)
                    .last("limit 1"));

            String supervisor = null;

            if (laseRecord != null) {
                // 查询计划获取另一个监督员
                QualitySupervise qualitySupervise = baseMapper.selectById(qualitySuperviseDetails.getSuperviseId());
                if (StringUtils.isNotBlank(qualitySupervise.getRecordUserIds())) {
                    List<String> recordUserIds = StrUtil.split(qualitySupervise.getRecordUserIds(), ',');
                    List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery()
                            .in(User::getId, recordUserIds));
                    if (CollectionUtils.isNotEmpty(users) && users.size() == 2) {
                        // 判断是否一样, 一样获取另外一个
                        if (users.get(0).getName().equals(laseRecord.getSupervisor())) {
                            supervisor = users.get(1).getName();
                        } else {
                            supervisor = users.get(0).getName();
                        }

                    }
                }
            }

            detailsRecord = new QualitySuperviseDetailsRecord();
            detailsRecord.setSuperviseDetailsId(superviseDetailsId);
            detailsRecord.setTestMember(qualitySuperviseDetails.getSupervisee());
            detailsRecord.setSupervisor(supervisor);

            detailsRecord.setPersonnel(SecurityUtils.getLoginUser().getUser().getNickName() + "有相应检测员的上岗证");
            detailsRecord.setEnvironment("温度：()℃ 湿度：()%");
            detailsRecord.setInspectionRecord("检测人员" + SecurityUtils.getLoginUser().getUser().getNickName() + "进行检测记录，记录内容真实有效");
            detailsRecord.setExaminingReport("由" + SecurityUtils.getLoginUser().getUser().getNickName() + "出示的检测报告符合规范要求");
            detailsRecord.setSupervisionEvaluation("检测按照要求进行，判定为满意");
            detailsRecord.setHandlingAdvice("/");

        }
        // 添加批准人名称
        if (detailsRecord.getRatifyUserId() != null) {
            User user = userMapper.selectById(detailsRecord.getRatifyUserId());
            detailsRecord.setRatifyUserName(user.getName());
        }
        return detailsRecord;
    }

    /**
     * 新增监督记录信息
     * @param qualitySuperviseDetailsRecord
     * @return
     */
    @Override
    public boolean addSuperviseDetailRecord(QualitySuperviseDetailsRecord qualitySuperviseDetailsRecord) {
        if (qualitySuperviseDetailsRecord.getSuperviseDetailsId() == null) {
            throw new ErrorException("缺少监督详细信息id");
        }
        if (qualitySuperviseDetailsRecord.getSuperviseDetailsRecordId() == null) {
            qualitySuperviseDetailsRecordMapper.insert(qualitySuperviseDetailsRecord);
        } else {
            qualitySuperviseDetailsRecordMapper.updateById(qualitySuperviseDetailsRecord);
        }

        if (qualitySuperviseDetailsRecord.getRatifyUserId() != null) {

            // 查询详情信息
            QualitySuperviseDetails superviseDetails = qualitySuperviseDetailsMapper.selectById(qualitySuperviseDetailsRecord.getSuperviseDetailsId());

            Integer userId = SecurityUtils.getUserId().intValue();
            User user = userMapper.selectById(userId);
            // 消息发送
            InformationNotification info = new InformationNotification();
            // 发送人
            info.setCreateUser(user.getName());
            info.setMessageType("6");
            info.setTheme("CNAS质量监督记录审批通知");
            info.setContent("监督项目为: " + superviseDetails.getSuperviseProject() + " 监督记录待审批");
            info.setSenderId(userId);
            // 接收人
            info.setConsigneeId(qualitySuperviseDetailsRecord.getRatifyUserId());
            info.setJumpPath(MenuJumpPathConstants.QUALITY_SUPERVISE);
            informationNotificationService.addInformationNotification(info);

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询发送人
                User people = userMapper.selectById(qualitySuperviseDetailsRecord.getRatifyUserId());
                String message = "";
                message += "CNAS质量监督记录审批通知";
                message += "\n请去过程要求-质量监督计划";
                message += "\n" + "监督项目为: " + superviseDetails.getSuperviseProject() + " 监督记录待审批";
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(people.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // 清空状态
        qualitySuperviseDetailsRecordMapper.update(null, Wrappers.<QualitySuperviseDetailsRecord>lambdaUpdate()
                .eq(QualitySuperviseDetailsRecord::getSuperviseDetailsRecordId, qualitySuperviseDetailsRecord.getSuperviseDetailsRecordId())
                .set(QualitySuperviseDetailsRecord::getIsFinish, null));

        return true;
    }

    /**
     * 监督记录批准
     * @param qualitySuperviseDetailsRecord
     * @return
     */
    @Override
    public boolean addSuperviseRecordOpinion(QualitySuperviseDetailsRecord qualitySuperviseDetailsRecord) {
        if (qualitySuperviseDetailsRecord.getIsFinish() == null) {
            throw new RuntimeException("缺少记录结果状态");
        }

        LambdaUpdateWrapper<QualitySuperviseDetailsRecord> wrapper = Wrappers.<QualitySuperviseDetailsRecord>lambdaUpdate()
                .eq(QualitySuperviseDetailsRecord::getSuperviseDetailsId, qualitySuperviseDetailsRecord.getSuperviseDetailsId())
                .set(QualitySuperviseDetailsRecord::getRatifyOpinion, qualitySuperviseDetailsRecord.getRatifyOpinion())
                .set(QualitySuperviseDetailsRecord::getIsAccording, qualitySuperviseDetailsRecord.getIsAccording())
                .set(QualitySuperviseDetailsRecord::getIsFinish, qualitySuperviseDetailsRecord.getIsFinish());

        // 为0清除审核人
        if (qualitySuperviseDetailsRecord.getIsFinish().equals(0)) {
            wrapper.set(QualitySuperviseDetailsRecord::getRatifyUserId, null)
                    .set(QualitySuperviseDetailsRecord::getRatifyTime, null);
        } else {
            wrapper.set(QualitySuperviseDetailsRecord::getRatifyTime, LocalDateTime.now());
        }

        qualitySuperviseDetailsRecordMapper.update(null, wrapper);


        return true;
    }

    /**
     * 导出监督记录表
     * @param superviseDetailsId
     * @param response
     */
    @Override
    public void exportSuperviseDetailRecord(Integer superviseDetailsId, HttpServletResponse response) {
        QualitySuperviseDetailsRecord recordDto = qualitySuperviseDetailsRecordMapper.selectSuperviseDetailRecord(superviseDetailsId);

        // 查询检测人员
        User tserUser = new User();
        if (StringUtils.isNotBlank(recordDto.getTestMember())) {
            tserUser = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getName, recordDto.getTestMember())
                    .last("limit 1"));
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/supervision-detail-record.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        User finalTserUser = tserUser;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("supervision", recordDto);
                    put("testMemberUrl", UserUtils.getFinalUserSignatureUrl(finalTserUser.getId()));
                    put("supervisoruUrl", UserUtils.getFinalUserSignatureUrl(recordDto.getSupervisor()));
                    put("technicalDirectorUrl", UserUtils.getFinalUserSignatureUrl(recordDto.getRatifyUserId()));
                    put("technicalDirectorDateUrl", recordDto.getRatifyTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(recordDto.getRatifyTime())).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "导出监督记录表", "UTF-8");
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

    /*************************************************  不合格工作控制单 ********************************************************/

    /**
     * 新增监督记录不符合控制信息
     * @param detailsAccording
     * @return
     */
    @Override
    public boolean addSuperviseDetailAccording(QualitySuperviseDetailsAccording detailsAccording) {
        QualitySuperviseDetailsAccording according = new QualitySuperviseDetailsAccording();
        // 当前登录用户信息和部门
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());
        switch (detailsAccording.getFlowType()) {
            // 不符合工作情况记录
            case 0:
                if (detailsAccording.getSuperviseDetailsId() == null) {
                    throw new ErrorException("缺少质量监督详情Id");
                }
                according.setSuperviseDetailsId(detailsAccording.getSuperviseDetailsId());
                according.setOccurrenceDepartment(detailsAccording.getOccurrenceDepartment());//发生部门
                according.setHeadDepartment(detailsAccording.getHeadDepartment());//部门负责人
                according.setFindWay(detailsAccording.getFindWay());//发现途径
                according.setRecordDetail(detailsAccording.getRecordDetail());//不符合记录详细
                according.setRecordAccording(detailsAccording.getRecordAccording());//不合格记录依据

                according.setFoundDepartment(departmentLimsName);//发现部门
                according.setRecordUserId(user.getId());//记录人id
                according.setRecordUserName(user.getName());//记录人
                according.setRecordTime(LocalDate.now());//记录时间

                // 处理人信息
                User actionsUser = userMapper.selectById(detailsAccording.getActionsUserId());
                String actionsDepartmentLims = userMapper.selectUserDepartmentLimsName(actionsUser.getId());

                according.setResponsibleDepartment(actionsDepartmentLims);//责任部门
                according.setActionsUserId(actionsUser.getId());//处理人id
                according.setActionsUserName(actionsUser.getName());//处理人

                according.setSupervisedUserId(detailsAccording.getSupervisedUserId());//被监督人id
                // 被监督人
                User supervisedUser = userMapper.selectById(detailsAccording.getSupervisedUserId());
                according.setSupervisedUserName(supervisedUser.getName());//被监督人
                according.setActionsTime(detailsAccording.getSupervisedTime());//被监督时间
                qualitySuperviseDetailsAccordingMapper.insert(according);
                break;

            // 1处理措施
            case 1:
                according.setSuperviseDetailsAccordingId(detailsAccording.getSuperviseDetailsAccordingId());
                according.setEliminateMeasure(detailsAccording.getEliminateMeasure());//清除不符合措施
                according.setActionsTime(LocalDate.now());//处理时间

                // 纠正负责人信息
                User correctsUser = userMapper.selectById(detailsAccording.getCorrectUserId());

                according.setCorrectUserId(correctsUser.getId());//纠正负责人id
                according.setCorrectUserName(correctsUser.getName());//纠正负责人

                qualitySuperviseDetailsAccordingMapper.updateById(according);
                break;

            // 纠正措施
            case 2:
                according.setSuperviseDetailsAccordingId(detailsAccording.getSuperviseDetailsAccordingId());
                according.setCorrectContent(detailsAccording.getCorrectContent());//纠正措施内容
                according.setIsCorrect(detailsAccording.getIsCorrect());//是否纠正处理
                according.setCorrectTime(LocalDate.now());//纠正填写时间

                // 质量负责人
                User qualityUser = userMapper.selectById(detailsAccording.getQualityManagerUserId());
                according.setQualityManagerUserId(qualityUser.getId());//质量负责人id
                according.setQualityManagerUserName(qualityUser.getName());//质量负责人

                qualitySuperviseDetailsAccordingMapper.updateById(according);
                break;

            //是否通知客户可恢复工作
            case 3:
                according.setSuperviseDetailsAccordingId(detailsAccording.getSuperviseDetailsAccordingId());
                according.setNotifyCustomer(detailsAccording.getNotifyCustomer());//通知客户
                according.setBackToWork(detailsAccording.getBackToWork());//回复工作

                according.setQualityManagerTime(LocalDate.now());//日期
                according.setIsFinish(1);
                qualitySuperviseDetailsAccordingMapper.updateById(according);
                break;
        }
        return true;
    }

    /**
     * (装备流程)新增监督记录不符合控制信息
     * @param detailsAccording
     * @return
     */
    @Override
    public boolean addEquipSuperviseDetailAccording(QualitySuperviseDetailsAccording detailsAccording) {
        if (detailsAccording.getSuperviseDetailsId() == null) {
            throw new ErrorException("缺少质量监督详情Id");
        }

        // 当前登录用户信息和部门
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());
        detailsAccording.setFoundDepartment(departmentLimsName);//发现部门
        detailsAccording.setRecordUserId(user.getId());//记录人id
        detailsAccording.setRecordUserName(user.getName());//记录人
        // 被监督人
        User supervisedUser = userMapper.selectById(detailsAccording.getSupervisedUserId());
        detailsAccording.setSupervisedUserName(supervisedUser.getName());//被监督

        if (detailsAccording.getSuperviseDetailsAccordingId() == null) {
            qualitySuperviseDetailsAccordingMapper.insert(detailsAccording);
        } else {
            qualitySuperviseDetailsAccordingMapper.updateById(detailsAccording);
        }

        if (detailsAccording.getApproverUserId() != null) {
            // 查询详情信息
            QualitySuperviseDetails superviseDetails = qualitySuperviseDetailsMapper.selectById(detailsAccording.getSuperviseDetailsId());

            // 消息发送
            InformationNotification info = new InformationNotification();
            // 发送人
            info.setCreateUser(user.getName());
            info.setMessageType("6");
            info.setTheme("CNAS质量监督不合格控制单填写通知");
            info.setContent("监督项目为: " + superviseDetails.getSuperviseProject() + " 监督不合格控制单待填写");
            info.setSenderId(userId);
            // 接收人
            info.setConsigneeId(detailsAccording.getApproverUserId());
            info.setJumpPath(MenuJumpPathConstants.QUALITY_SUPERVISE);
            informationNotificationService.addInformationNotification(info);

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询发送人
                User people = userMapper.selectById(detailsAccording.getApproverUserId());
                String message = "";
                message += "CNAS质量监督记录审批通知";
                message += "\n请去过程要求-质量监督计划";
                message += "\n" + "监督项目为: " + superviseDetails.getSuperviseProject() + " 监督不合格控制单待填写";
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(people.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // 清空状态
        qualitySuperviseDetailsAccordingMapper.update(null, Wrappers.<QualitySuperviseDetailsAccording>lambdaUpdate()
                .eq(QualitySuperviseDetailsAccording::getSuperviseDetailsAccordingId, detailsAccording.getSuperviseDetailsAccordingId())
                .set(QualitySuperviseDetailsAccording::getIsFinish, null));

        return true;
    }

    /**
     * (装备流程)批准监督记录不符合控制信息
     * 批准完成后统一填写三个流程人id和部门
     * @param detailsAccording
     * @return
     */
    @Override
    public boolean approverEquipSuperviseDetailAccording(QualitySuperviseDetailsAccording detailsAccording) {
        if (detailsAccording.getIsFinish() == null) {
            throw new RuntimeException("缺少批准状态");
        }

        QualitySuperviseDetailsAccording according = new QualitySuperviseDetailsAccording();
        according.setSuperviseDetailsAccordingId(detailsAccording.getSuperviseDetailsAccordingId());

        if (detailsAccording.getIsFinish().equals(1)) {
            // 当前登录用户信息和部门
            User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
            String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());

            according.setResponsibleDepartment(departmentLimsName);//责任部门
            according.setActionsUserId(user.getId());//处理人id
            according.setActionsUserName(user.getName());//处理人

            according.setCorrectUserId(user.getId());//纠正负责人id
            according.setCorrectUserName(user.getName());//纠正负责人

            according.setQualityManagerUserId(user.getId());//质量负责人id
            according.setQualityManagerUserName(user.getName());//质量负责人
        } else {
            qualitySuperviseDetailsAccordingMapper.update(null, Wrappers.<QualitySuperviseDetailsAccording>lambdaUpdate()
                    .eq(QualitySuperviseDetailsAccording::getSuperviseDetailsAccordingId, detailsAccording.getSuperviseDetailsAccordingId())
                    .set(QualitySuperviseDetailsAccording::getResponsibleDepartment, null)
                    .set(QualitySuperviseDetailsAccording::getActionsUserId, null)
                    .set(QualitySuperviseDetailsAccording::getActionsUserName, null)
                    .set(QualitySuperviseDetailsAccording::getCorrectUserId, null)
                    .set(QualitySuperviseDetailsAccording::getCorrectUserName, null)
                    .set(QualitySuperviseDetailsAccording::getQualityManagerUserId, null)
                    .set(QualitySuperviseDetailsAccording::getQualityManagerUserName, null)
                    .set(QualitySuperviseDetailsAccording::getApproverUserId, null));
        }

        according.setIsFinish(detailsAccording.getIsFinish());
        qualitySuperviseDetailsAccordingMapper.updateById(according);
        return true;
    }

    /**
     * 查询监督记录不符合控制信息
     * @param superviseDetailsId
     * @return
     */
    @Override
    public QualitySuperviseDetailsAccording getSuperviseDetailAccording(Integer superviseDetailsId) {
        QualitySuperviseDetailsAccording detailsAccording;

        detailsAccording = qualitySuperviseDetailsAccordingMapper.selectOne(Wrappers.<QualitySuperviseDetailsAccording>lambdaQuery()
                .eq(QualitySuperviseDetailsAccording::getSuperviseDetailsId, superviseDetailsId));

        if (detailsAccording == null) {
            detailsAccording = new QualitySuperviseDetailsAccording();
            // 查询监督计划详情
            QualitySuperviseDetails superviseDetails = qualitySuperviseDetailsMapper.selectById(superviseDetailsId);
            superviseDetails.setSuperviseDetailsId(superviseDetailsId);
            detailsAccording.setSupervisedUserId(superviseDetails.getSupervisedUserId());
            detailsAccording.setSupervisedUserName(superviseDetails.getSupervisee());
        }
        return detailsAccording;
    }

    /**
     * 查询不符合项
     * @param page
     * @param detailsAccording
     * @return
     */
    @Override
    public IPage<QualitySuperviseDetailsAccording> pageSuperviseDetailAccording(Page page, QualitySuperviseDetailsAccording detailsAccording) {
        return qualitySuperviseDetailsAccordingMapper.pageSuperviseDetailAccording(page, QueryWrappers.queryWrappers(detailsAccording));
    }

    /**
     * 导出监督记录不符合控制信息
     * @param superviseDetailAccordingId
     * @param response
     */
    @Override
    public void superviseDetailAccordingExport(Integer superviseDetailAccordingId, HttpServletResponse response) {
        QualitySuperviseDetailsAccordingDto exportDto = qualitySuperviseDetailsAccordingMapper.selectSuperviseDetailsAccording(superviseDetailAccordingId);
        // 发现部门
        String discovererUrl = null;
        if (exportDto.getRecordUserId() != null) {
            discovererUrl = userMapper.selectById(exportDto.getRecordUserId()).getSignatureUrl();
            if (StringUtils.isBlank(discovererUrl)) {
                throw new ErrorException("找不到发现部门人的签名");
            }
        }

        // 处理措施负责人
        String responsibleUrl = null;
        if (exportDto.getCorrectUserId() != null) {
            responsibleUrl = userMapper.selectById(exportDto.getCorrectUserId()).getSignatureUrl();
            if (StringUtils.isBlank(responsibleUrl)) {
                throw new ErrorException("找不到处理措施负责人的签名");
            }
        }

        // 技术负责人
        String correctiveUrl = null;
        if (exportDto.getQualityManagerUserId() != null) {
            correctiveUrl = userMapper.selectById(exportDto.getQualityManagerUserId()).getSignatureUrl();
            if (StringUtils.isBlank(correctiveUrl)) {
                throw new ErrorException("找不到技术负责人的签名");
            }
        }

        // 质量负责人
        String qualityUrl = null;
        if (exportDto.getQualityManagerUserId() != null) {
            qualityUrl = userMapper.selectById(exportDto.getQualityManagerUserId()).getSignatureUrl();
            if (StringUtils.isBlank(qualityUrl)) {
                throw new ErrorException("找不到质量负责人的签名");
            }
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/supervision-detail-according.docx");
        ConfigureBuilder builder = Configure.builder();
        String finalDiscovererUrl = discovererUrl;
        String finalResponsibleUrl = responsibleUrl;
        String finalCorrectiveUrl = correctiveUrl;
        String finalQualityUrl = qualityUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("control", exportDto);
                    put("discovererUrl", StringUtils.isNotBlank(finalDiscovererUrl) ? Pictures.ofLocal(imgUrl + "/" + finalDiscovererUrl).create() : null);
                    put("responsibleUrl", StringUtils.isNotBlank(finalResponsibleUrl) ? Pictures.ofLocal(imgUrl + "/" + finalResponsibleUrl).create() : null);
                    put("correctiveUrl", StringUtils.isNotBlank(finalCorrectiveUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCorrectiveUrl).create() : null);
                    put("qualityUrl", StringUtils.isNotBlank(finalQualityUrl) ? Pictures.ofLocal(imgUrl + "/" + finalQualityUrl).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "监督记录不符合控制信息", "UTF-8");
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

    /*************************************************  纠正措施处理单 ********************************************************/


    @Override
    public boolean addSuperviseDetailCorrect(QualitySuperviseDetailsCorrect detailsCorrect) {
        QualitySuperviseDetailsCorrect correct = new QualitySuperviseDetailsCorrect();
        // 当前登录用户信息和部门
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());

        switch (detailsCorrect.getFlowType()) {
            // 不合格提出
            case 0:
                if (detailsCorrect.getSuperviseDetailsId() == null) {
                    throw new ErrorException("缺少质量监督详情Id");
                }
                correct.setSuperviseDetailsId(detailsCorrect.getSuperviseDetailsId());
                correct.setRaiseResult(detailsCorrect.getRaiseResult());//不合格表述
                correct.setVdeRaiseResult(detailsCorrect.getVdeRaiseResult());//vde专家发现
                correct.setRaiseDepartment(departmentLimsName);//提出部门
                correct.setRaiseUserId(user.getId());//提出人id
                correct.setRaiseUserName(user.getName());// 提出人
                correct.setRaiseTime(LocalDate.now());// 提出时间

                // 原因分析人信息
                User causeUser = userMapper.selectById(detailsCorrect.getCauseUserId());
                String causeDepartmentLims = userMapper.selectUserDepartmentLimsName(causeUser.getId());

                correct.setCauseDepartment(causeDepartmentLims);//原因分析责任部门
                correct.setCauseUserId(causeUser.getId());//1原因分析人id
                correct.setCauseUserName(causeUser.getName());// 1原因分析人
                qualitySuperviseDetailsCorrectMapper.insert(correct);

                break;

            // 原因分析
            case 1:
                correct.setSuperviseDetailsCorrectId(detailsCorrect.getSuperviseDetailsCorrectId());
                correct.setCauseResult(detailsCorrect.getCauseResult());//原因分析
                correct.setCauseTime(LocalDate.now());// 1原因分析时间

                // 纠正人信息
                User correctUser = userMapper.selectById(detailsCorrect.getCorrectUserId());
                String correctUserDepartmentLims = userMapper.selectUserDepartmentLimsName(correctUser.getId());

                correct.setCorrectDepartment(correctUserDepartmentLims);//2纠正责任部门
                correct.setCorrectUserId(correctUser.getId());//2纠正人id
                correct.setCorrectUserName(correctUser.getName());// 2纠正人
                qualitySuperviseDetailsCorrectMapper.updateById(correct);
                break;

            // 纠正措施
            case 2:
                correct.setSuperviseDetailsCorrectId(detailsCorrect.getSuperviseDetailsCorrectId());
                correct.setCorrectResult(detailsCorrect.getCorrectResult());//2纠正措施
                correct.setRaiseDepartmentAffirm(detailsCorrect.getRaiseDepartmentAffirm());//2提出部门确认
                correct.setCorrectTime(LocalDate.now());// 2纠正时间

                // 验证人信息
                User validationUser = userMapper.selectById(detailsCorrect.getValidationUserId());
                String validationUserDepartmentLims = userMapper.selectUserDepartmentLimsName(validationUser.getId());

                correct.setValidationDepartment(validationUserDepartmentLims);//3验证部门
                correct.setValidationUserId(validationUser.getId());//3验证人id
                correct.setValidationUserName(validationUser.getName());// 3验证人
                qualitySuperviseDetailsCorrectMapper.updateById(correct);
                break;

            // 验证结果
            case 3:
                correct.setSuperviseDetailsCorrectId(detailsCorrect.getSuperviseDetailsCorrectId());
                correct.setValidationResult(detailsCorrect.getValidationResult());//3验证结果
                correct.setValidationTime(LocalDate.now());// 3验证时间
                correct.setIsFinish(1);
                qualitySuperviseDetailsCorrectMapper.updateById(correct);
                break;
        }

        return true;
    }

    /**
     * (装备流程)新增监督纠正处理信息
     * @return
     */
    @Override
    public boolean addEquipSuperviseDetailCorrect(QualitySuperviseDetailsCorrect detailsCorrect) {
        if (detailsCorrect.getSuperviseDetailsId() == null) {
            throw new ErrorException("缺少质量监督详情Id");
        }
        // 当前登录用户信息和部门
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());

        detailsCorrect.setRaiseDepartment(departmentLimsName);//提出部门
        detailsCorrect.setRaiseUserId(user.getId());//提出人id
        detailsCorrect.setRaiseUserName(user.getName());// 提出人
        if (detailsCorrect.getSuperviseDetailsCorrectId() == null) {
            qualitySuperviseDetailsCorrectMapper.insert(detailsCorrect);
        } else {
            qualitySuperviseDetailsCorrectMapper.updateById(detailsCorrect);
        }

        if (detailsCorrect.getApproverUserId() != null) {
            // 查询详情信息
            QualitySuperviseDetails superviseDetails = qualitySuperviseDetailsMapper.selectById(detailsCorrect.getSuperviseDetailsId());

            // 消息发送
            InformationNotification info = new InformationNotification();
            // 发送人
            info.setCreateUser(user.getName());
            info.setMessageType("6");
            info.setTheme("CNAS质量监督纠正措施填写通知");
            info.setContent("监督项目为: " + superviseDetails.getSuperviseProject() + " 监督纠正措施待填写");
            info.setSenderId(userId);
            // 接收人
            info.setConsigneeId(detailsCorrect.getApproverUserId());
            info.setJumpPath(MenuJumpPathConstants.QUALITY_SUPERVISE);
            informationNotificationService.addInformationNotification(info);

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询发送人
                User people = userMapper.selectById(detailsCorrect.getApproverUserId());
                String message = "";
                message += "CNAS质量监督纠正措施填写通知";
                message += "\n请去过程要求-质量监督计划";
                message += "\n" + "监督项目为: " + superviseDetails.getSuperviseProject() + " 监督纠正措施待填写";
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(people.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // 清空状态
        qualitySuperviseDetailsCorrectMapper.update(null, Wrappers.<QualitySuperviseDetailsCorrect>lambdaUpdate()
                .eq(QualitySuperviseDetailsCorrect::getSuperviseDetailsCorrectId, detailsCorrect.getSuperviseDetailsCorrectId())
                .set(QualitySuperviseDetailsCorrect::getIsFinish, null));
        return true;
    }

    /**
     * (装备流程)批准监督纠正处理信息
     * @return
     */
    @Override
    public boolean approveEquipSuperviseDetailCorrect(QualitySuperviseDetailsCorrect detailsCorrect) {
        if (detailsCorrect.getIsFinish() == null) {
            throw new RuntimeException("缺少批准状态");
        }

        QualitySuperviseDetailsCorrect correct = new QualitySuperviseDetailsCorrect();
        // 当前登录用户信息和部门
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        String departmentLimsName = userMapper.selectUserDepartmentLimsName(user.getId());
        correct.setSuperviseDetailsCorrectId(detailsCorrect.getSuperviseDetailsCorrectId());

        if (detailsCorrect.getIsFinish().equals(1)) {
            correct.setCauseDepartment(departmentLimsName);//原因分析责任部门
            correct.setCauseUserId(user.getId());//1原因分析人id
            correct.setCauseUserName(user.getName());// 1原因分析人

            correct.setCorrectDepartment(departmentLimsName);//2纠正责任部门
            correct.setCorrectUserId(user.getId());//2纠正人id
            correct.setCorrectUserName(user.getName());// 2纠正人

            correct.setValidationDepartment(departmentLimsName);//3验证部门
            correct.setValidationUserId(user.getId());//3验证人id
            correct.setValidationUserName(user.getName());// 3验证人
        } else {
            qualitySuperviseDetailsCorrectMapper.update(null, Wrappers.<QualitySuperviseDetailsCorrect>lambdaUpdate()
                    .eq(QualitySuperviseDetailsCorrect::getSuperviseDetailsCorrectId, detailsCorrect.getSuperviseDetailsCorrectId())
                    .set(QualitySuperviseDetailsCorrect::getCauseDepartment, null)
                    .set(QualitySuperviseDetailsCorrect::getCauseUserId, null)
                    .set(QualitySuperviseDetailsCorrect::getCauseUserName, null)
                    .set(QualitySuperviseDetailsCorrect::getCorrectDepartment, null)
                    .set(QualitySuperviseDetailsCorrect::getCorrectUserId, null)
                    .set(QualitySuperviseDetailsCorrect::getCorrectUserName, null)
                    .set(QualitySuperviseDetailsCorrect::getValidationDepartment, null)
                    .set(QualitySuperviseDetailsCorrect::getValidationUserId, null)
                    .set(QualitySuperviseDetailsCorrect::getValidationUserName, null)
                    .set(QualitySuperviseDetailsCorrect::getApproverUserId, null)
            );
        }

        correct.setIsFinish(detailsCorrect.getIsFinish());
        qualitySuperviseDetailsCorrectMapper.updateById(correct);

        return true;
    }

    /**
     * 查询监督纠正处理
     * @param superviseDetailsId
     * @return
     */
    @Override
    public QualitySuperviseDetailsCorrect getSuperviseDetailCorrect(Integer superviseDetailsId) {
        QualitySuperviseDetailsCorrect detailsCorrect;

        detailsCorrect = qualitySuperviseDetailsCorrectMapper.selectOne(Wrappers.<QualitySuperviseDetailsCorrect>lambdaQuery()
                .eq(QualitySuperviseDetailsCorrect::getSuperviseDetailsId, superviseDetailsId));

        if (detailsCorrect == null) {
            detailsCorrect = new QualitySuperviseDetailsCorrect();
            detailsCorrect.setSuperviseDetailsId(superviseDetailsId);
        }
        return detailsCorrect;
    }

    /**
     * 查询监督纠正措施列表
     * @param page
     * @param detailsCorrect
     * @return
     */
    @Override
    public IPage<QualitySuperviseDetailsCorrect> pageSuperviseDetailCorrect(Page page, QualitySuperviseDetailsCorrect detailsCorrect) {
        return qualitySuperviseDetailsCorrectMapper.pageSuperviseDetailAccording(page, QueryWrappers.queryWrappers(detailsCorrect));
    }

    /**
     * 新增监督纠正措施附件
     * @param superviseDetailsCorrectId
     * @param file
     * @return
     */
    @Override
    public boolean uploadSuperviseDetailCorrectFile(Integer superviseDetailsCorrectId, MultipartFile file) {
        if (superviseDetailsCorrectId == null) {
            throw new ErrorException("缺少纠正措施id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        QualitySuperviseDetailsCorrectFile superviseDetailsCorrectFile = new QualitySuperviseDetailsCorrectFile();
        superviseDetailsCorrectFile.setSuperviseDetailsCorrectId(superviseDetailsCorrectId);
        superviseDetailsCorrectFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            superviseDetailsCorrectFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            superviseDetailsCorrectFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            superviseDetailsCorrectFile.setFileUrl(pathName);
            qualitySuperviseDetailsCorrectFileMapper.insert(superviseDetailsCorrectFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(e.getMessage());
        }
    }

    /**
     * 查询监督纠正措施附件
     * @param superviseDetailsCorrectId
     * @return
     */
    @Override
    public List<QualitySuperviseDetailsCorrectFile> getSuperviseDetailCorrectFileList(Integer superviseDetailsCorrectId) {
        return qualitySuperviseDetailsCorrectFileMapper.selectList(Wrappers.<QualitySuperviseDetailsCorrectFile>lambdaQuery()
                .eq(QualitySuperviseDetailsCorrectFile::getSuperviseDetailsCorrectId, superviseDetailsCorrectId));
    }

    /**
     * 导出监督纠正措施
     * @param superviseDetailsCorrectId
     * @param response
     */
    @Override
    public void exportSuperviseDetaillCorrect(Integer superviseDetailsCorrectId, HttpServletResponse response) {
        QualitySuperviseDetailsCorrect detailsCorrect = qualitySuperviseDetailsCorrectMapper.selectById(superviseDetailsCorrectId);
        QualitySuperviseDetailsCorrectDto detailsCorrectDto = new QualitySuperviseDetailsCorrectDto();
        BeanUtils.copyProperties(detailsCorrect, detailsCorrectDto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 提出时间
        detailsCorrectDto.setRaiseTimeString(detailsCorrectDto.getRaiseTime() != null
                ? detailsCorrectDto.getRaiseTime().format(formatter) : null);
        // 原因分析时间
        detailsCorrectDto.setCauseTimeString(detailsCorrectDto.getCauseTime() != null
                ? detailsCorrectDto.getCauseTime().format(formatter) : null);

        // 纠正时间
        detailsCorrectDto.setCorrectTimeString(detailsCorrectDto.getCorrectTime() != null
                ? detailsCorrectDto.getCorrectTime().format(formatter) : null);

        // 验证时间
        detailsCorrectDto.setValidationTimeString(detailsCorrectDto.getValidationTime() != null
                ? detailsCorrectDto.getValidationTime().format(formatter) : null);


        // 提出人签名
        String raiseUrl = null;
        if (detailsCorrectDto.getRaiseUserId() != null) {
            raiseUrl = userMapper.selectById(detailsCorrectDto.getRaiseUserId()).getSignatureUrl();
            if (StringUtils.isBlank(raiseUrl)) {
                throw new ErrorException("找不到提出人的签名");
            }
        }

        // 原因分析人
        String causeUrl = null;
        if (detailsCorrectDto.getCauseUserId() != null) {
            causeUrl = userMapper.selectById(detailsCorrectDto.getCauseUserId()).getSignatureUrl();
            if (StringUtils.isBlank(causeUrl)) {
                throw new ErrorException("找不到原因分析人的签名");
            }
        }

        // 纠正人
        String correctUrl = null;
        if (detailsCorrectDto.getCorrectUserId() != null) {
            correctUrl = userMapper.selectById(detailsCorrectDto.getCorrectUserId()).getSignatureUrl();
            if (StringUtils.isBlank(correctUrl)) {
                throw new ErrorException("找不到纠正人的签名");
            }
        }

        // 验证人
        String validationUrl = null;
        if (detailsCorrectDto.getValidationUserId() != null) {
            validationUrl = userMapper.selectById(detailsCorrectDto.getValidationUserId()).getSignatureUrl();
            if (StringUtils.isBlank(validationUrl)) {
                throw new ErrorException("找不到验证人的签名");
            }
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/supervise-detail-correct.docx");
        ConfigureBuilder builder = Configure.builder();
        builder.useSpringEL(true);
        String finalRaiseUrl = raiseUrl;
        String finalCauseUrl = causeUrl;
        String finalCorrectUrl = correctUrl;
        String finalValidationUrl = validationUrl;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, builder.build()).render(
                new HashMap<String, Object>() {{
                    put("correct", detailsCorrectDto);
                    put("raiseUrl", StringUtils.isNotBlank(finalRaiseUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRaiseUrl).create() : null);
                    put("causeUrl", StringUtils.isNotBlank(finalCauseUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCauseUrl).create() : null);
                    put("correctUrl", StringUtils.isNotBlank(finalCorrectUrl) ? Pictures.ofLocal(imgUrl + "/" + finalCorrectUrl).create() : null);
                    put("validationUrl", StringUtils.isNotBlank(finalValidationUrl) ? Pictures.ofLocal(imgUrl + "/" + finalValidationUrl).create() : null);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "监督纠正措施", "UTF-8");
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


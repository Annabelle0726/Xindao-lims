package com.ruoyi.process.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
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
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.pojo.DeviceExamineRecord;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.inspect.util.XWPFDocumentUtils;
import com.ruoyi.process.dto.QualityMonitorDetailsDto;
import com.ruoyi.process.dto.QualityMonitorDetailsEvaluateDto;
import com.ruoyi.process.dto.QualityMonitorDto;
import com.ruoyi.process.excel.QualityMonitorDetailsUpload;
import com.ruoyi.process.mapper.*;
import com.ruoyi.process.pojo.*;
import com.ruoyi.process.service.QualityMonitorDetailsEvaluateService;
import com.ruoyi.process.service.QualityMonitorDetailsRatifyService;
import com.ruoyi.process.service.QualityMonitorDetailsService;
import com.ruoyi.process.service.QualityMonitorService;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 质量监控计划主表
 *
 * @author zhuo
 * @since 2024-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QualityMonitorServiceImpl extends ServiceImpl<QualityMonitorMapper, QualityMonitor> implements QualityMonitorService {

    @Resource
    private QualityMonitorDetailsService qualityMonitorDetailsService;
    @Resource
    private QualityMonitorDetailsMapper qualityMonitorDetailsMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private QualityMonitorDetailsRatifyMapper qualityMonitorDetailsRatifyMapper;
    @Resource
    private QualityMonitorDetailsRatifyService qualityMonitorDetailsRatifyService;
    @Resource
    private QualityMonitorDetailsEvaluateMapper qualityMonitorDetailsEvaluateMapper;
    @Resource
    private QualityMonitorDetailsEvaluateService qualityMonitorDetailsEvaluateService;
    @Resource
    private QualityMonitorDetailsEvaluateFileMapper qualityMonitorDetailsEvaluateFileMapper;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;


    /**
     * 导入监控计划
     * @param file
     * @return
     */
    @Override
    public boolean importQualityMonitor(MultipartFile file, QualityMonitor monitor) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 文件名称
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        QualityMonitor qualityMonitor = new QualityMonitor();
        qualityMonitor.setMonitorName(fileName);
        qualityMonitor.setMonitorYear(monitor.getMonitorYear());
        qualityMonitor.setWriteUserId(userId);
        qualityMonitor.setExamineUserId(monitor.getExamineUserId());
        qualityMonitor.setWriteTime(LocalDateTime.now());
        baseMapper.insert(qualityMonitor);

        if (monitor.getExamineUserId() == null) {
            throw new ErrorException("缺少审核人");
        }

        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS质量监控计划审核通知");
        info.setContent(fileName + "质量监控计划待审核");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(monitor.getExamineUserId());
        info.setJumpPath(MenuJumpPathConstants.QUALITY_MONITOR);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询发送人
            User people = userMapper.selectById(monitor.getExamineUserId());
            String message = "";
            message += "CNAS质量监控计划审核通知";
            message += "\n请去过程要求-质量监控计划";
            message += "\n" + fileName + "质量监控计划待审核";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(people.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        List<QualityMonitorDetails> detailsUploadList = new ArrayList<>();

        // 导入附件内容
        try {
            // excel解析
            EasyExcel.read(file.getInputStream(), QualityMonitorDetailsUpload.class, new AnalysisEventListener<QualityMonitorDetailsUpload>() {
                @Override
                public void invoke(QualityMonitorDetailsUpload detailsUpload, AnalysisContext analysisContext) {
                    // 判断是否为空
                    if (StringUtils.isBlank(detailsUpload.getPlannedTime())) {
                        throw new ErrorException("计划开展时间不能为空");
                    }
                        // 对象复制
                    QualityMonitorDetails monitorDetails = new QualityMonitorDetails();
                    BeanUtils.copyProperties(detailsUpload, monitorDetails);
                    monitorDetails.setQualityMonitorId(qualityMonitor.getQualityMonitorId());

                    detailsUploadList.add(monitorDetails);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).sheet().doRead();
            qualityMonitorDetailsService.saveBatch(detailsUploadList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 监控计划审核
     * @param qualityMonitor
     * @return
     */
    @Override
    public boolean examineQualityMonitor(QualityMonitor qualityMonitor) {
        if (qualityMonitor.getRatifyUserId() == null) {
            throw new ErrorException("缺少批准人");
        }

        // 当前登录用户
        baseMapper.update(null, Wrappers.<QualityMonitor>lambdaUpdate()
                .eq(QualityMonitor::getQualityMonitorId, qualityMonitor.getQualityMonitorId())
                .set(QualityMonitor::getRatifyUserId, qualityMonitor.getRatifyUserId())
                .set(QualityMonitor::getExamineRemark, qualityMonitor.getExamineRemark())
                .set(QualityMonitor::getExamineStatus, qualityMonitor.getExamineStatus())
                .set(QualityMonitor::getExamineTime, LocalDateTime.now())
        );

        QualityMonitor monitor = baseMapper.selectById(qualityMonitor.getQualityMonitorId());

        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS质量监控计划批准通知");
        info.setContent(monitor.getMonitorName() + "质量监控计划待批准");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(qualityMonitor.getRatifyUserId());
        info.setJumpPath(MenuJumpPathConstants.QUALITY_MONITOR);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询发送人
            User people = userMapper.selectById(qualityMonitor.getRatifyUserId());
            String message = "";
            message += "CNAS质量监控计划批准通知";
            message += "\n请去过程要求-质量监控计划";
            message += "\n" + monitor.getMonitorName() + "质量监控计划待批准";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(people.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return true;
    }

    /**
     * 监控计划批准
     * @param qualityMonitor
     * @return
     */
    @Override
    public boolean ratifyQualityMonitor(QualityMonitor qualityMonitor) {
        // 当前登录用户
        baseMapper.update(null, Wrappers.<QualityMonitor>lambdaUpdate()
                .eq(QualityMonitor::getQualityMonitorId, qualityMonitor.getQualityMonitorId())
                .set(QualityMonitor::getRatifyRemark, qualityMonitor.getRatifyRemark())
                .set(QualityMonitor::getRatifyStatus, qualityMonitor.getRatifyStatus())
                .set(QualityMonitor::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 监控计划列表
     * @param page
     * @param qualityMonitor
     * @return
     */
    @Override
    public IPage<QualityMonitorDto> pageQualityMonitor(Page page, QualityMonitor qualityMonitor) {
        return baseMapper.pageQualityMonitor(page, QueryWrappers.queryWrappers(qualityMonitor));
    }

    /**
     * 监控计划详情列表
     * @param page
     * @param qualityMonitorDetails
     * @return
     */
    @Override
    public IPage<QualityMonitorDetailsDto> pageQualityMonitorDetail(Page page, QualityMonitorDetails qualityMonitorDetails) {
        if (qualityMonitorDetails.getQualityMonitorId() == null) {
            return new Page();
        }
        return qualityMonitorDetailsMapper.pageQualityMonitorDetail(page, QueryWrappers.queryWrappers(qualityMonitorDetails));
    }

    /**
     * 导出监控计划
     * @param qualityMonitorId
     * @param response
     */
    @Override
    public void exportQualityMonitorDetail(Integer qualityMonitorId, HttpServletResponse response) {
        // 查询详情
        QualityMonitor qualityMonitor = baseMapper.selectById(qualityMonitorId);

        //获取提交人的签名地址
        String writeUrl = userMapper.selectById(qualityMonitor.getWriteUserId()).getSignatureUrl();
        if (ObjectUtils.isEmpty(writeUrl) || writeUrl.equals("")) {
            throw new ErrorException("找不到检验人的签名");
        }

        //获取复核人的签名地址
        String examineUrl = null;
        if (qualityMonitor.getExamineUserId() != null) {
            examineUrl = userMapper.selectById(qualityMonitor.getExamineUserId()).getSignatureUrl();
            if (StringUtils.isBlank(examineUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        //获取批准人的签名地址
        String ratifyUrl = null;
        if (qualityMonitor.getRatifyUserId() != null) {
            ratifyUrl = userMapper.selectById(qualityMonitor.getRatifyUserId()).getSignatureUrl();
            if (StringUtils.isBlank(ratifyUrl)) {
                throw new ErrorException("找不到复核人的签名");
            }
        }

        // 查询详情
        List<QualityMonitorDetails> qualityMonitorDetails = qualityMonitorDetailsMapper.selectList(Wrappers.<QualityMonitorDetails>lambdaQuery()
                .eq(QualityMonitorDetails::getQualityMonitorId, qualityMonitorId));

        // 判断监控目的一样的值
        AtomicInteger count = new AtomicInteger(1);
        Map<String, List<QualityMonitorDetails>> listMap = qualityMonitorDetails.stream().collect(Collectors.groupingBy(QualityMonitorDetails::getMonitorPurpose));
        listMap.forEach((s, details) -> {
            // 查询数量超过1的
            if (details.size() > 1) {
                for (QualityMonitorDetails detail : details) {
                    detail.setMonitorPurpose(detail.getMonitorPurpose() + "∑" + count);
                }
                count.getAndIncrement();
            }
        });

        int index = 1;
        for (QualityMonitorDetails qualityMonitorDetail : qualityMonitorDetails) {
            qualityMonitorDetail.setIndex(index);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/quality-monitor.docx");
        String finalExamineUrl = examineUrl;
        String finalRatifyUrl = ratifyUrl;
        Configure configure = Configure.builder()
                .bind("monitorDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("year", qualityMonitor.getMonitorYear());
                    put("monitorDetailList", qualityMonitorDetails);
                    put("writeUrl", StringUtils.isNotBlank(writeUrl) ? Pictures.ofLocal(imgUrl + "/" + writeUrl).create() : null);
                    put("examineUrl", StringUtils.isNotBlank(finalExamineUrl) ? Pictures.ofLocal(imgUrl + "/" + finalExamineUrl).create() : null);
                    put("ratifyUrl", StringUtils.isNotBlank(finalRatifyUrl) ? Pictures.ofLocal(imgUrl + "/" + finalRatifyUrl).create() : null);
                    put("writeDateUrl", qualityMonitor.getWriteTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(qualityMonitor.getWriteTime())).create() : null);
                    put("examineDateUrl", qualityMonitor.getExamineTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(qualityMonitor.getExamineTime())).create() : null);
                    put("ratifyDateUrl", qualityMonitor.getRatifyTime() != null ?
                            Pictures.ofStream(DateImageUtil.createDateImage(qualityMonitor.getRatifyTime())).create() : null);
                }});

        // 处理换行问题
        XWPFDocumentUtils.updateMergeByDocument(template.getXWPFDocument());
        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    qualityMonitor.getMonitorName(), "UTF-8");
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


    /************************************************************  批准  *******************************************************************/

    /**
     * 查询监控计划详情实施信息
     * @param qualityMonitorDetailsId
     * @return
     */
    @Override
    public QualityMonitorDetailsRatify getQualityMonitorRatify(Integer qualityMonitorDetailsId) {
        QualityMonitorDetailsRatify qualityMonitorDetailsRatify;
        // 查询监控部门id
        qualityMonitorDetailsRatify = qualityMonitorDetailsRatifyMapper.selectOne(Wrappers.<QualityMonitorDetailsRatify>lambdaQuery()
                .eq(QualityMonitorDetailsRatify::getQualityMonitorDetailsId, qualityMonitorDetailsId));

        if (qualityMonitorDetailsRatify == null) {
            // 查询详情计划
            QualityMonitorDetails qualityMonitorDetails = qualityMonitorDetailsMapper.selectById(qualityMonitorDetailsId);
            qualityMonitorDetailsRatify = new QualityMonitorDetailsRatify();
            qualityMonitorDetailsRatify.setQualityMonitorDetailsId(qualityMonitorDetailsId); // 详情id
            qualityMonitorDetailsRatify.setMonitorProject(qualityMonitorDetails.getMonitorProject()); // 监控项目
            qualityMonitorDetailsRatify.setMonitorData(DateUtil.format(new Date(), "yyyy-MM")); // 监控时间
            qualityMonitorDetailsRatify.setMonitorPurpose(qualityMonitorDetails.getMonitorPurpose()); // 监控目的
            qualityMonitorDetailsRatify.setParticipant(qualityMonitorDetails.getParticipant()); // 参加人员
            qualityMonitorDetailsRatify.setBudget(qualityMonitorDetails.getBudget()); // 预算
            qualityMonitorDetailsRatify.setInspectionDepartment(userMapper.selectUserDepartmentLimsName( SecurityUtils.getUserId().intValue()));
        }

        if (qualityMonitorDetailsRatify.getRatifyUserId() != null) {
            qualityMonitorDetailsRatify.setRatifyName(userMapper.selectById(qualityMonitorDetailsRatify.getRatifyUserId()).getName());
        }

        return qualityMonitorDetailsRatify;
    }

    /**
     * 新增监控批准实施
     * @param qualityMonitorDetailsRatify
     * @return
     */
    @Override
    public boolean addQualityMonitorRatify(QualityMonitorDetailsRatify qualityMonitorDetailsRatify) {
        if (qualityMonitorDetailsRatify.getQualityMonitorDetailsId() == null) {
            throw new ErrorException("缺少监控详细信息id");
        }
        qualityMonitorDetailsRatifyService.saveOrUpdate(qualityMonitorDetailsRatify);
        if (qualityMonitorDetailsRatify.getRatifyUserId() != null) {

            // 查询详情信息
            QualityMonitorDetails monitorDetails = qualityMonitorDetailsMapper.selectById(qualityMonitorDetailsRatify.getQualityMonitorDetailsId());

            Integer userId =SecurityUtils.getUserId().intValue();
            User user = userMapper.selectById(userId);
            // 消息发送
            InformationNotification info = new InformationNotification();
            // 发送人
            info.setCreateUser(user.getName());
            info.setMessageType("6");
            info.setTheme("CNAS质量监控实施批准通知");
            info.setContent("监控项目为: " + monitorDetails.getMonitorProject() + " 质量监控实施待批准");
            info.setSenderId(userId);
            // 接收人
            info.setConsigneeId(qualityMonitorDetailsRatify.getRatifyUserId());
            info.setJumpPath(MenuJumpPathConstants.QUALITY_MONITOR);
            informationNotificationService.addInformationNotification(info);

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询发送人
                User people = userMapper.selectById(qualityMonitorDetailsRatify.getRatifyUserId());
                String message = "";
                message += "CNAS质量监控实施批准通知";
                message += "\n请去过程要求-质量监控计划";
                message += "\n" + "监控项目为: " + monitorDetails.getMonitorProject() + " 质量监控实施待批准";
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(people.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        // 清空状态
        qualityMonitorDetailsRatifyService.update(Wrappers.<QualityMonitorDetailsRatify>lambdaUpdate()
                .eq(QualityMonitorDetailsRatify::getDetailsRatifyId, qualityMonitorDetailsRatify.getDetailsRatifyId())
                .set(QualityMonitorDetailsRatify::getIsFinish, null));
        return true;
    }

    /**
     * 监控计划详情批准意见
     * @param qualityMonitorDetailsRatify
     * @return
     */
    @Override
    public boolean addQualityMonitorRatifyOpinion(QualityMonitorDetailsRatify qualityMonitorDetailsRatify) {
        LambdaUpdateWrapper<QualityMonitorDetailsRatify> wrapper = Wrappers.<QualityMonitorDetailsRatify>lambdaUpdate()
                .eq(QualityMonitorDetailsRatify::getDetailsRatifyId, qualityMonitorDetailsRatify.getDetailsRatifyId())
                .set(QualityMonitorDetailsRatify::getRatifyOpinion, qualityMonitorDetailsRatify.getRatifyOpinion())
                .set(QualityMonitorDetailsRatify::getIsFinish, qualityMonitorDetailsRatify.getIsFinish());
        // 为0清除审核人
        if (qualityMonitorDetailsRatify.getIsFinish().equals(0)) {
            wrapper.set(QualityMonitorDetailsRatify::getRatifyUserId, null);
        }

        qualityMonitorDetailsRatifyService.update(wrapper);
        return true;
    }

    /**
     * 导出监控计划详情实施信息
     *
     * @param detailsRatifyId 监控计划详情实施id
     * @param response  响应
     */
    @Override
    public void exportQualityMonitorRatify(Integer detailsRatifyId, HttpServletResponse response) {
        QualityMonitorDetailsRatify qualityMonitorDetailsRatify = qualityMonitorDetailsRatifyMapper.selectOne(Wrappers.<QualityMonitorDetailsRatify>lambdaQuery().eq(QualityMonitorDetailsRatify::getQualityMonitorDetailsId, detailsRatifyId));

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/quality-monitor-details-ratify.docx");
        Configure configure = Configure.builder()
                .bind("processMethodVerifyMachineAttachmentList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("qualityMonitorDetailsRatify", qualityMonitorDetailsRatify);
                    put("ratifyUserUrl", UserUtils.getFinalUserSignatureUrl(qualityMonitorDetailsRatify.getRatifyUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "质量监控实施计划", "UTF-8");
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

    /************************************************************  评价  *******************************************************************/

    /**
     * 查询质量监控评价
     * @param qualityMonitorDetailsId
     * @return
     */
    @Override
    public QualityMonitorDetailsEvaluate getQualityMonitorEvaluate(Integer qualityMonitorDetailsId) {
        return qualityMonitorDetailsEvaluateMapper.getQualityMonitorEvaluate(qualityMonitorDetailsId);
    }

    /**
     * 新增监控评价
     * @param qualityMonitorDetailsEvaluate
     * @return
     */
    @Override
    public boolean addQualityMonitorEvaluate(QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate) {
        if (qualityMonitorDetailsEvaluate.getQualityMonitorDetailsId() == null) {
            throw new ErrorException("缺少监控详细信息id");
        }
        // 查询详情信息
        QualityMonitorDetails monitorDetails = qualityMonitorDetailsMapper.selectById(qualityMonitorDetailsEvaluate.getQualityMonitorDetailsId());
        // 发送消息通知
        // 判断评审结论人是否为空
        if (qualityMonitorDetailsEvaluate.getRatifyUserId() != null) {

            Integer userId =SecurityUtils.getUserId().intValue();
            User user = userMapper.selectById(userId);
            // 消息发送
            InformationNotification info = new InformationNotification();
            // 发送人
            info.setCreateUser(user.getName());
            info.setMessageType("6");
            info.setTheme("CNAS质量监控评价结论通知");
            info.setContent("监控项目为: " + monitorDetails.getMonitorProject() + " 质量监控待评价结论");
            info.setSenderId(userId);
            // 接收人
            info.setConsigneeId(qualityMonitorDetailsEvaluate.getRatifyUserId());
            info.setJumpPath(MenuJumpPathConstants.QUALITY_MONITOR);
            informationNotificationService.addInformationNotification(info);

            // 发送企业微信通知
            threadPoolTaskExecutor.execute(() -> {
                // 查询发送人
                User people = userMapper.selectById(qualityMonitorDetailsEvaluate.getRatifyUserId());
                String message = "";
                message += "CNAS质量监控评价结论通知";
                message += "\n请去过程要求-质量监控计划";
                message += "\n" + "监控项目为: " + monitorDetails.getMonitorProject() + " 质量监控待评价结论";
                //发送企业微信消息通知
                try {
                    WxCpUtils.inform(people.getAccount(), message, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } else {
            // 监控结果评价
            if (qualityMonitorDetailsEvaluate.getImplementUserId() != null) {
                Integer userId =SecurityUtils.getUserId().intValue();
                User user = userMapper.selectById(userId);
                // 消息发送
                InformationNotification info = new InformationNotification();
                // 发送人
                info.setCreateUser(user.getName());
                info.setMessageType("6");
                info.setTheme("CNAS质量监控结果评价");
                info.setContent("监控项目为: " + monitorDetails.getMonitorProject() + " 质量监控待评价结论");
                info.setSenderId(userId);
                // 接收人
                info.setConsigneeId(qualityMonitorDetailsEvaluate.getImplementUserId());
                info.setJumpPath(MenuJumpPathConstants.QUALITY_MONITOR);
                informationNotificationService.addInformationNotification(info);

                // 发送企业微信通知
                threadPoolTaskExecutor.execute(() -> {
                    // 查询发送人
                    User people = userMapper.selectById(qualityMonitorDetailsEvaluate.getImplementUserId());
                    String message = "";
                    message += "CNAS质量监控结果评价";
                    message += "\n请去过程要求-质量监控计划";
                    message += "\n" + "监控项目为: " + monitorDetails.getMonitorProject() + " 质量监控待评价结论";
                    //发送企业微信消息通知
                    try {
                        WxCpUtils.inform(people.getAccount(), message, null);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }


        return qualityMonitorDetailsEvaluateService.saveOrUpdate(qualityMonitorDetailsEvaluate);
    }

    /**
     * 监控评价审批意见
     * @param qualityMonitorDetailsEvaluate
     * @return
     */
    @Override
    public boolean addMonitorEvaluateOpinion(QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate) {
        qualityMonitorDetailsEvaluateService.update(Wrappers.<QualityMonitorDetailsEvaluate>lambdaUpdate()
                .eq(QualityMonitorDetailsEvaluate::getDetailsEvaluateId, qualityMonitorDetailsEvaluate.getDetailsEvaluateId())
                .set(QualityMonitorDetailsEvaluate::getRatifyOpinion, qualityMonitorDetailsEvaluate.getRatifyOpinion())
                .set(QualityMonitorDetailsEvaluate::getRatifyTime, LocalDateTime.now())
                .set(QualityMonitorDetailsEvaluate::getIsFinish, 1));
        return true;
    }

    /**
     * 新增监控评价附件表
     * @param qualityMonitorDetailsId
     * @param file
     * @return
     */
    @Override
    public boolean uploadEvaluateFile(Integer qualityMonitorDetailsId, MultipartFile file) {
        if (qualityMonitorDetailsId == null) {
            throw new ErrorException("缺少监控详情id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        QualityMonitorDetailsEvaluateFile evaluateFile = new QualityMonitorDetailsEvaluateFile();
        evaluateFile.setDetailsEvaluateId(qualityMonitorDetailsId);
        evaluateFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            evaluateFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            evaluateFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            evaluateFile.setFileUrl(pathName);
            qualityMonitorDetailsEvaluateFileMapper.insert(evaluateFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return false;
        }
    }

    /**
     * 查询监控评价附件列表
     * @return
     */
    @Override
    public List<QualityMonitorDetailsEvaluateFile> getEvaluateFileList(Integer qualityMonitorDetailsId) {
        return qualityMonitorDetailsEvaluateFileMapper.selectList(Wrappers.<QualityMonitorDetailsEvaluateFile>lambdaQuery()
                .eq(QualityMonitorDetailsEvaluateFile::getDetailsEvaluateId, qualityMonitorDetailsId));
    }


    /**
     * 导出监控评价
     * @param detailsEvaluateId 监控评价id
     */
    @Override
    public void exportQualityMonitorEvaluate(Integer detailsEvaluateId, HttpServletResponse response) {
        // 查询监控评价信息
        QualityMonitorDetailsEvaluate qualityMonitorDetailsEvaluate = qualityMonitorDetailsEvaluateMapper.selectOne(Wrappers.<QualityMonitorDetailsEvaluate>lambdaQuery().eq(QualityMonitorDetailsEvaluate::getQualityMonitorDetailsId,detailsEvaluateId));
        // 渲染word模板对象
        QualityMonitorDetailsEvaluateDto qualityMonitorDetailsEvaluateDto = new QualityMonitorDetailsEvaluateDto();
        BeanUtils.copyProperties(qualityMonitorDetailsEvaluate, qualityMonitorDetailsEvaluateDto);
        // 格式化时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        qualityMonitorDetailsEvaluateDto.setRatifyTimeStr(qualityMonitorDetailsEvaluate.getRatifyTime() == null ? null : qualityMonitorDetailsEvaluate.getRatifyTime().format(dateTimeFormatter));
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/quality-monitor-evaluate.docx");
        Configure configure = Configure.builder()
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("qualityMonitorDetailsEvaluate", qualityMonitorDetailsEvaluateDto);
                    put("implementUserUrl", UserUtils.getFinalUserSignatureUrl(Integer.valueOf(qualityMonitorDetailsEvaluate.getImplementUserId())));
                    put("ratifyUserUrl", UserUtils.getFinalUserSignatureUrl(qualityMonitorDetailsEvaluate.getRatifyUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "监控评价", "UTF-8");
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

    /************************************************** 监控完成报告 ****************************************************************/

    /**
     * 上传监控完成报告
     * @param file
     * @param qualityMonitorDetailsId
     * @return
     */
    @Override
    public boolean uploadFinishReport(MultipartFile file, Integer qualityMonitorDetailsId) {
        if (qualityMonitorDetailsId == null) {
            throw new ErrorException("缺少监控详情id");
        }
        LocalDateTime now = LocalDateTime.now();
        Integer userId =SecurityUtils.getUserId().intValue();
        String contentType = file.getContentType();
        String urlString;
        String pathName;
        try {
            String path = wordUrl;
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));

            // 判断是否是pdf
            if (!contentType.contains("pdf")) {
                wordInsertUrl(new HashMap<String, Object>() {{
                    put("writeUrl", UserUtils.getFinalUserSignatureUrl(userId));
                    put("writeDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(now)).create());
                }}, wordUrl + "/" + pathName.replace("/word", wordUrl));
            }

            qualityMonitorDetailsService.update(Wrappers.<QualityMonitorDetails>lambdaUpdate()
                    .eq(QualityMonitorDetails::getQualityMonitorDetailsId, qualityMonitorDetailsId)
                    .set(QualityMonitorDetails::getFinishReportUrl, pathName)
                    .set(QualityMonitorDetails::getWriteTime, now));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException("文件上传失败");
        }
    }

    /**
     * 批准完成报告
     * @param qualityMonitorDetails
     * @return
     */
    @Override
    public boolean ratifyFinishReport(QualityMonitorDetails qualityMonitorDetails) {
        LocalDateTime now = LocalDateTime.now();
        Integer userId =SecurityUtils.getUserId().intValue();
        LambdaUpdateWrapper<QualityMonitorDetails> wrapper = Wrappers.<QualityMonitorDetails>lambdaUpdate()
                .eq(QualityMonitorDetails::getQualityMonitorDetailsId, qualityMonitorDetails.getQualityMonitorDetailsId())
                .set(QualityMonitorDetails::getRatifyUserId, userId)
                .set(QualityMonitorDetails::getRatifyRemark, qualityMonitorDetails.getRatifyRemark())
                .set(QualityMonitorDetails::getRatifyStatus, qualityMonitorDetails.getRatifyStatus())
                .set(QualityMonitorDetails::getRatifyTime, now);
        if (qualityMonitorDetails.getRatifyStatus().equals(0)) {
            wrapper.set(QualityMonitorDetails::getFinishReportUrl, null);
        }
        qualityMonitorDetailsMapper.update(null, wrapper);

        // 添加批准人
        QualityMonitorDetails details = qualityMonitorDetailsMapper.selectById(qualityMonitorDetails.getQualityMonitorDetailsId());
        if (StringUtils.isNotBlank(details.getFinishReportUrl()) && !details.getFinishReportUrl().contains(".pdf")) {
            wordInsertUrl(new HashMap<String, Object>() {{
                put("ratifyUrl", UserUtils.getFinalUserSignatureUrl(userId));
                put("ratifyDateUrl", Pictures.ofStream(DateImageUtil.createDateImage(now)).create());
            }}, wordUrl + "/" + details.getFinishReportUrl().replace("/word", wordUrl));
        }
        return true;
    }

    public int wordInsertUrl(Map<String, Object> map, String url) {
        XWPFTemplate template = XWPFTemplate.compile(url).render(map);
        try {
            template.writeAndClose(Files.newOutputStream(Paths.get(url)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }


}


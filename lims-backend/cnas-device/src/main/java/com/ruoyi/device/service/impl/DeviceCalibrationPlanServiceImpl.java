package com.ruoyi.device.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.dto.DeviceCalibrationPlanDetailDto;
import com.ruoyi.device.dto.DeviceCalibrationPlanDto;
import com.ruoyi.device.excel.upload.DeviceCalibrationPlanDetailUpload;
import com.ruoyi.device.mapper.DeviceCalibrationPlanDetailMapper;
import com.ruoyi.device.mapper.DeviceCalibrationPlanMapper;
import com.ruoyi.device.pojo.DeviceCalibrationPlan;
import com.ruoyi.device.pojo.DeviceCalibrationPlanDetail;
import com.ruoyi.device.service.DeviceCalibrationPlanDetailService;
import com.ruoyi.device.service.DeviceCalibrationPlanService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 设备校准计划主表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 03:58:17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceCalibrationPlanServiceImpl extends ServiceImpl<DeviceCalibrationPlanMapper, DeviceCalibrationPlan> implements DeviceCalibrationPlanService {

    @Resource
    private DeviceCalibrationPlanDetailMapper deviceCalibrationPlanDetailMapper;
    @Resource
    private DeviceCalibrationPlanDetailService deviceCalibrationPlanDetailService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private UserMapper userMapper;


    /**
     * 新增设备校准计划
     * @param calibrationPlanDto
     * @return
     */
    @Override
    public boolean addDeviceCalibrationPlan(DeviceCalibrationPlanDto calibrationPlanDto) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 编制日期
        calibrationPlanDto.setWriteUserId(userId);
        calibrationPlanDto.setWriteTime(LocalDateTime.now());
        baseMapper.insert(calibrationPlanDto);

        // 添加详情
        if (CollectionUtils.isNotEmpty(calibrationPlanDto.getCalibrationPlanDetailList())) {
            for (DeviceCalibrationPlanDetail calibrationPlanDetail : calibrationPlanDto.getCalibrationPlanDetailList()) {
                calibrationPlanDetail.setPlanId(calibrationPlanDto.getPlanId());
            }
            deviceCalibrationPlanDetailService.saveBatch(calibrationPlanDto.getCalibrationPlanDetailList());
        }

        return true;
    }

    /**
     * 导入设备校准计划
     * @param file
     * @return
     */
    @Override
    public boolean importDeviceCalibrationPlan(MultipartFile file, String planYear) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 文件名称
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        DeviceCalibrationPlan calibrationPlan = new DeviceCalibrationPlan();
        calibrationPlan.setPlanName(fileName);
        calibrationPlan.setPlanYear(planYear);
        calibrationPlan.setWriteUserId(userId);
        calibrationPlan.setWriteTime(LocalDateTime.now());
        baseMapper.insert(calibrationPlan);

        List<DeviceCalibrationPlanDetail> detailsUploadList = new ArrayList<>();
        // 导入附件内容
        try {
            // excel解析
            EasyExcel.read(file.getInputStream(), DeviceCalibrationPlanDetailUpload.class, new AnalysisEventListener<DeviceCalibrationPlanDetailUpload>() {
                @Override
                public void invoke(DeviceCalibrationPlanDetailUpload detailsUpload, AnalysisContext analysisContext) {
                    // 判断是否为空
                    if (StringUtils.isNotBlank(detailsUpload.getDeviceName()) &&
                            StringUtils.isNotBlank(detailsUpload.getDeviceNumber())) {
                        // 对象复制
                        DeviceCalibrationPlanDetail calibrationPlanDetail = new DeviceCalibrationPlanDetail();
                        BeanUtils.copyProperties(detailsUpload, calibrationPlanDetail);
                        // 格式话最近检定时间和本年计划校准时间
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        calibrationPlanDetail.setLastDate(LocalDate.parse(detailsUpload.getLastDate(), formatter));
                        calibrationPlanDetail.setPlanDate(LocalDate.parse(detailsUpload.getLastDate(), formatter));

                        calibrationPlanDetail.setPlanId(calibrationPlan.getPlanId());
                        detailsUploadList.add(calibrationPlanDetail);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).sheet().doRead();
            deviceCalibrationPlanDetailService.saveBatch(detailsUploadList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 提交批准
     * @param deviceCalibrationPlan
     * @return
     */
    @Override
    public boolean submiatRatifyDeviceCalibrationPlan(DeviceCalibrationPlan deviceCalibrationPlan) {
        baseMapper.update(null, Wrappers.<DeviceCalibrationPlan>lambdaUpdate()
                .eq(DeviceCalibrationPlan::getPlanId, deviceCalibrationPlan.getPlanId())
                .set(DeviceCalibrationPlan::getRatifyUserId, deviceCalibrationPlan.getRatifyUserId())
                .set(DeviceCalibrationPlan::getRatifyRemark, null)
                .set(DeviceCalibrationPlan::getRatifyStatus, null)
                .set(DeviceCalibrationPlan::getRatifyTime, null)
        );

        DeviceCalibrationPlan calibrationPlan = baseMapper.selectById(deviceCalibrationPlan.getPlanId());

        // 发送消息
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS设备校准计划批准通知");
        info.setContent(calibrationPlan.getPlanName() + "的设备校准计划待批准");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(deviceCalibrationPlan.getRatifyUserId());
        info.setJumpPath(MenuJumpPathConstants.DEVICE);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询接收人
            User personnel = userMapper.selectById(deviceCalibrationPlan.getPlanId());

            String message = "";
            message += "CNAS设备校准计划批准通知";
            message += "\n请去资源管理-设备一层目录-设备校准计划填写";
            message += "\n" + calibrationPlan.getPlanName() + "的校准计划待批准";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(personnel.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    /**
     * 设备校准计划批准
     * @param deviceCalibrationPlan
     * @return
     */
    @Override
    public boolean ratifyDeviceCalibrationPlan(DeviceCalibrationPlan deviceCalibrationPlan) {
        baseMapper.update(null, Wrappers.<DeviceCalibrationPlan>lambdaUpdate()
                .eq(DeviceCalibrationPlan::getPlanId, deviceCalibrationPlan.getPlanId())
                .set(DeviceCalibrationPlan::getRatifyRemark, deviceCalibrationPlan.getRatifyRemark())
                .set(DeviceCalibrationPlan::getRatifyStatus, deviceCalibrationPlan.getRatifyStatus())
                .set(DeviceCalibrationPlan::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 设备校准计划列表
     * @param page
     * @param deviceCalibrationPlan
     * @return
     */
    @Override
    public IPage<DeviceCalibrationPlanDto> pageDeviceCalibrationPlan(Page page, DeviceCalibrationPlan deviceCalibrationPlan) {
        return baseMapper.pageDeviceCalibrationPlan(page, QueryWrappers.queryWrappers(deviceCalibrationPlan));
    }

    /**
     * 设备校准计划详情列表
     * @param page
     * @param deviceCalibrationPlanDetails
     * @return
     */
    @Override
    public IPage<DeviceCalibrationPlanDetail> pageDeviceCalibrationPlanDetail(Page page, DeviceCalibrationPlanDetail deviceCalibrationPlanDetails) {
        if (deviceCalibrationPlanDetails.getPlanId() == null) {
            return new Page();
        }
        return deviceCalibrationPlanDetailMapper.pageDeviceCalibrationPlanDetail(page, QueryWrappers.queryWrappers(deviceCalibrationPlanDetails));
    }

    /**
     * 导出设备校准计划
     * @param deviceCalibrationPlanId
     * @param response
     */
    @Override
    public void exportDeviceCalibrationPlanDetail(Integer deviceCalibrationPlanId, HttpServletResponse response) {
        // 查询设备校准计划
        DeviceCalibrationPlan deviceCalibrationPlan = baseMapper.selectById(deviceCalibrationPlanId);
        DeviceCalibrationPlanDto deviceCalibrationPlanDto = new DeviceCalibrationPlanDto();
        BeanUtils.copyProperties(deviceCalibrationPlan, deviceCalibrationPlanDto);
        // 设置编制和批准时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        deviceCalibrationPlanDto.setWriteTimeStr(deviceCalibrationPlan.getWriteTime() == null ? null : deviceCalibrationPlan.getWriteTime().format(formatter));
        deviceCalibrationPlanDto.setRatifyTimeStr(deviceCalibrationPlan.getRatifyTime() == null ? null : deviceCalibrationPlan.getRatifyTime().format(formatter));

        // 查询设备校准计划详情
        List<DeviceCalibrationPlanDetail> deviceCalibrationPlanDetailList = deviceCalibrationPlanDetailMapper.selectList(Wrappers.<DeviceCalibrationPlanDetail>lambdaQuery()
                .eq(DeviceCalibrationPlanDetail::getPlanId, deviceCalibrationPlanId)
                .orderByAsc(DeviceCalibrationPlanDetail::getPlanDate));
        // 设置序号 和 时间
        ArrayList<DeviceCalibrationPlanDetailDto> deviceCalibrationPlanDetailDtoList = new ArrayList<>();
        deviceCalibrationPlanDetailList.forEach(deviceCalibrationPlanDetail -> {
            DeviceCalibrationPlanDetailDto deviceCalibrationPlanDetailDto = new DeviceCalibrationPlanDetailDto();
            BeanUtils.copyProperties(deviceCalibrationPlanDetail, deviceCalibrationPlanDetailDto);
            deviceCalibrationPlanDetailDto.setIndex(deviceCalibrationPlanDetailList.indexOf(deviceCalibrationPlanDetail) + 1);
            deviceCalibrationPlanDetailDto.setLastDateStr(deviceCalibrationPlanDetail.getLastDate() == null ? null : deviceCalibrationPlanDetail.getLastDate().format(formatter));
            deviceCalibrationPlanDetailDto.setPlanDateStr(deviceCalibrationPlanDetail.getPlanDate() == null ? null : deviceCalibrationPlanDetail.getPlanDate().format(formatter));
            deviceCalibrationPlanDetailDtoList.add(deviceCalibrationPlanDetailDto);
        });

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-calibration-plan.docx");
        Configure configure = Configure.builder()
                .bind("deviceCalibrationPlanDetailDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceCalibrationPlan", deviceCalibrationPlanDto);
                    put("deviceCalibrationPlanDetailDtoList", deviceCalibrationPlanDetailDtoList);
                    //获取编制人的签名地址
                    put("organizationUrl", UserUtils.getFinalUserSignatureUrl(deviceCalibrationPlan.getRatifyUserId()));
                    //获取批准人的签名地址
                    put("approvedUrl", UserUtils.getFinalUserSignatureUrl(deviceCalibrationPlan.getWriteUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "设备校准计划表", "UTF-8");
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
     * 批量编辑设备校准
     * @param calibrationPlanDto
     * @return
     */
    @Override
    public boolean updateDeviceCalibrationPlan(DeviceCalibrationPlanDto calibrationPlanDto) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 编制日期
        calibrationPlanDto.setWriteUserId(userId);
        calibrationPlanDto.setWriteTime(LocalDateTime.now());
        baseMapper.updateById(calibrationPlanDto);

        // 删除原本的详情
        deviceCalibrationPlanDetailService.remove(Wrappers.<DeviceCalibrationPlanDetail>lambdaQuery().eq(DeviceCalibrationPlanDetail::getPlanId, calibrationPlanDto.getPlanId()));

        // 添加详情
        if (CollectionUtils.isNotEmpty(calibrationPlanDto.getCalibrationPlanDetailList())) {
            for (DeviceCalibrationPlanDetail calibrationPlanDetail : calibrationPlanDto.getCalibrationPlanDetailList()) {
                calibrationPlanDetail.setPlanId(calibrationPlanDto.getPlanId());
            }
            deviceCalibrationPlanDetailService.saveBatch(calibrationPlanDto.getCalibrationPlanDetailList());
        }
        return true;
    }

    /**
     * 查询设备校准详情
     * @param planId
     * @return
     */
    @Override
    public DeviceCalibrationPlanDto getDeviceCalibrationPlan(Integer planId) {
        DeviceCalibrationPlan calibrationPlan = baseMapper.selectById(planId);
        DeviceCalibrationPlanDto deviceCalibrationPlanDto = new DeviceCalibrationPlanDto();
        BeanUtils.copyProperties(calibrationPlan, deviceCalibrationPlanDto);
        // 查询详情
        List<DeviceCalibrationPlanDetail> list = deviceCalibrationPlanDetailService.list(Wrappers.<DeviceCalibrationPlanDetail>lambdaQuery()
                .eq(DeviceCalibrationPlanDetail::getPlanId, planId)
                .orderByAsc(DeviceCalibrationPlanDetail::getPlanDate));

        deviceCalibrationPlanDto.setCalibrationPlanDetailList(list);
        return deviceCalibrationPlanDto;
    }

}

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
import com.ruoyi.device.dto.DeviceExaminePlanDetailsDto;
import com.ruoyi.device.dto.DeviceExaminePlanDto;
import com.ruoyi.device.excel.upload.DeviceExaminePlanUpload;
import com.ruoyi.device.mapper.DeviceExaminePlanDetailsMapper;
import com.ruoyi.device.mapper.DeviceExaminePlanMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceExaminePlan;
import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import com.ruoyi.device.service.DeviceExaminePlanDetailsService;
import com.ruoyi.device.service.DeviceExaminePlanService;
import com.ruoyi.framework.exception.ErrorException;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 设备核查计划主表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceExaminePlanServiceImpl extends ServiceImpl<DeviceExaminePlanMapper, DeviceExaminePlan> implements DeviceExaminePlanService {

    @Resource
    private DeviceExaminePlanDetailsMapper deviceExaminePlanDetailsMapper;
    @Resource
    private DeviceExaminePlanDetailsService deviceExaminePlanDetailsService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增设备核查计划
     * @param examinePlanDto
     * @return
     */
    @Override
    public boolean addDeviceExaminePlan(DeviceExaminePlanDto examinePlanDto) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 文件名称
        examinePlanDto.setWriteUserId(userId);
        examinePlanDto.setWriteTime(LocalDateTime.now());
        baseMapper.insert(examinePlanDto);

        // 添加详情
        if (CollectionUtils.isNotEmpty(examinePlanDto.getExaminePlanDetailsList())) {
            for (DeviceExaminePlanDetails deviceExaminePlanDetails : examinePlanDto.getExaminePlanDetailsList()) {
                deviceExaminePlanDetails.setPlanId(examinePlanDto.getPlanId());
            }
            deviceExaminePlanDetailsService.saveBatch(examinePlanDto.getExaminePlanDetailsList());
        }
        return true;
    }

    /**
     * 导入设备核查计划
     * @param file
     * @return
     */
    @Override
    public boolean importDeviceExaminePlan(MultipartFile file) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 文件名称
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        DeviceExaminePlan examinePlan = new DeviceExaminePlan();
        examinePlan.setPlanName(fileName);
        examinePlan.setWriteUserId(userId);
        examinePlan.setWriteTime(LocalDateTime.now());
        baseMapper.insert(examinePlan);

        List<DeviceExaminePlanDetails> examinePlanDetails = new ArrayList<>();
        // 导入附件内容
        try {
            // excel解析
            EasyExcel.read(file.getInputStream(), DeviceExaminePlanUpload.class, new AnalysisEventListener<DeviceExaminePlanUpload>() {
                @Override
                public void invoke(DeviceExaminePlanUpload detailsUpload, AnalysisContext analysisContext) {
                    // 判断是否为空
                    if (StringUtils.isNotBlank(detailsUpload.getDeviceName()) &&
                            StringUtils.isNotBlank(detailsUpload.getDeviceNumber())) {
                        // 对象复制
                        DeviceExaminePlanDetails planDetails = new DeviceExaminePlanDetails();
                        BeanUtils.copyProperties(detailsUpload, planDetails);

                        planDetails.setPlanId(examinePlan.getPlanId());
                        examinePlanDetails.add(planDetails);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).sheet().doRead();
            for (DeviceExaminePlanDetails examinePlanDetail : examinePlanDetails) {
                Device device = deviceMapper.selectOne(Wrappers.<Device>lambdaQuery()
                        .eq(Device::getManagementNumber, examinePlanDetail.getDeviceNumber().trim()));
                if (device == null) {
                    throw new ErrorException("设备编号" + examinePlanDetail.getDeviceNumber() + "未查询到设备, 请重新导入");
                }
                User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                        .eq(User::getName, examinePlanDetail.getCheckChargerUser()));
                if (user == null) {
                    throw new ErrorException("设备编号" + examinePlanDetail.getDeviceNumber() + "未查询到核查人");
                }
                examinePlanDetail.setDeviceId(device.getId());
                examinePlanDetail.setCheckChargerUserId(user.getId());
            }

            deviceExaminePlanDetailsService.saveBatch(examinePlanDetails);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 提交批准
     * @param deviceExaminePlan
     * @return
     */
    @Override
    public boolean submitRatifyDeviceExaminePlan(DeviceExaminePlan deviceExaminePlan) {
        baseMapper.update(null, Wrappers.<DeviceExaminePlan>lambdaUpdate()
                .eq(DeviceExaminePlan::getPlanId, deviceExaminePlan.getPlanId())
                .set(DeviceExaminePlan::getRatifyUserId, deviceExaminePlan.getRatifyUserId())
                .set(DeviceExaminePlan::getRatifyRemark, null)
                .set(DeviceExaminePlan::getRatifyStatus, null)
                .set(DeviceExaminePlan::getRatifyTime, null)
        );

        DeviceExaminePlan examinePlan = baseMapper.selectById(deviceExaminePlan.getPlanId());

        // 发送消息
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS设备核查计划批准通知");
        info.setContent(examinePlan.getPlanName() + "的设备核查计划待批准");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(examinePlan.getRatifyUserId());
        info.setJumpPath(MenuJumpPathConstants.DEVICE);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            // 查询接收人
            User personnel = userMapper.selectById(examinePlan.getRatifyUserId());

            String message = "";
            message += "CNAS设备核查计划批准通知";
            message += "\n请去资源管理-设备一层目录-设备核查计划填写";
            message += "\n" + examinePlan.getPlanName() + "的校准核查划待批准";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(personnel.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return false;
    }

    /**
     * 设备核查计划批准
     * @param deviceExaminePlan
     * @return
     */
    @Override
    public boolean ratifyDeviceExaminePlan(DeviceExaminePlan deviceExaminePlan) {
        // 当前登录用户
        baseMapper.update(null, Wrappers.<DeviceExaminePlan>lambdaUpdate()
                .eq(DeviceExaminePlan::getPlanId, deviceExaminePlan.getPlanId())
                .set(DeviceExaminePlan::getRatifyRemark, deviceExaminePlan.getRatifyRemark())
                .set(DeviceExaminePlan::getRatifyStatus, deviceExaminePlan.getRatifyStatus())
                .set(DeviceExaminePlan::getRatifyTime, LocalDateTime.now())
        );
        return true;
    }

    /**
     * 设备核查计划列表
     * @param page
     * @param deviceExaminePlan
     * @return
     */
    @Override
    public IPage<DeviceExaminePlanDto> pageDeviceExaminePlan(Page page, DeviceExaminePlan deviceExaminePlan) {
        return baseMapper.deviceExaminePlanDetailsMapper(page, QueryWrappers.queryWrappers(deviceExaminePlan));
    }

    /**
     * 设备核查计划详情列表
     * @param page
     * @param deviceExaminePlanDetails
     * @return
     */
    @Override
    public IPage<DeviceExaminePlanDetailsDto> pageDeviceExaminePlanDetail(Page page, DeviceExaminePlanDetails deviceExaminePlanDetails) {
        if (deviceExaminePlanDetails.getPlanId() == null) {
            return new Page();
        }
        return deviceExaminePlanDetailsMapper.pageDeviceExaminePlanDetail(page, QueryWrappers.queryWrappers(deviceExaminePlanDetails));
    }

    /**
     * 导出设备核查计划
     * @param deviceExaminePlanId 设备核查计划id
     * @param response 响应体
     */
    @Override
    public void exportDeviceExaminePlanDetail(Integer deviceExaminePlanId, HttpServletResponse response) {
        // 查询设备核查计划
        DeviceExaminePlanDto deviceExaminePlanDto = baseMapper.selectExamineExaminePlanDto(deviceExaminePlanId);

        // 查询设备核查计划详情
        List<DeviceExaminePlanDetails> deviceExaminePlanDetailsList = deviceExaminePlanDetailsMapper.selectList(Wrappers.<DeviceExaminePlanDetails>lambdaQuery().eq(DeviceExaminePlanDetails::getPlanId, deviceExaminePlanId)
                .orderByAsc(DeviceExaminePlanDetails::getCheckTime));
        // 设置序号
        ArrayList<DeviceExaminePlanDetailsDto> deviceExaminePlanDetailsDtoList = new ArrayList<>();
        deviceExaminePlanDetailsList.forEach(deviceExamineRecordContrastDetail -> {
            DeviceExaminePlanDetailsDto deviceExaminePlanDetailsDto = new DeviceExaminePlanDetailsDto();
            BeanUtils.copyProperties(deviceExamineRecordContrastDetail, deviceExaminePlanDetailsDto);
            deviceExaminePlanDetailsDto.setIndex(deviceExaminePlanDetailsList.indexOf(deviceExamineRecordContrastDetail) + 1);
            deviceExaminePlanDetailsDtoList.add(deviceExaminePlanDetailsDto);
        });

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/examine-plan-detail.docx");
        Configure configure = Configure.builder()
                .bind("deviceExaminePlanDetailsDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceExaminePlan", deviceExaminePlanDto);
                    put("deviceExaminePlanDetailsDtoList", deviceExaminePlanDetailsDtoList);
                    //获取核查人的签名地址
                    put("writeUserUrl", UserUtils.getFinalUserSignatureUrl(deviceExaminePlanDto.getWriteUserId()));
                    //获取审查人的签名地址
                    put("reviewUserUrl", UserUtils.getFinalUserSignatureUrl(deviceExaminePlanDto.getRatifyUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "设备核查计划", "UTF-8");
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

    @Override
    public boolean updateDeviceExaminePlan(DeviceExaminePlanDto examinePlanDto) {
        // 当前登录用户
        Integer userId = SecurityUtils.getUserId().intValue();
        // 文件名称
        examinePlanDto.setWriteUserId(userId);
        examinePlanDto.setWriteTime(LocalDateTime.now());
        baseMapper.updateById(examinePlanDto);

        deviceExaminePlanDetailsService.remove(Wrappers.<DeviceExaminePlanDetails>lambdaQuery()
                .eq(DeviceExaminePlanDetails::getPlanId, examinePlanDto.getPlanId()));

        // 添加详情
        if (CollectionUtils.isNotEmpty(examinePlanDto.getExaminePlanDetailsList())) {
            for (DeviceExaminePlanDetails deviceExaminePlanDetails : examinePlanDto.getExaminePlanDetailsList()) {
                deviceExaminePlanDetails.setPlanId(examinePlanDto.getPlanId());
            }
            deviceExaminePlanDetailsService.saveBatch(examinePlanDto.getExaminePlanDetailsList());
        }
        return true;
    }

    @Override
    public DeviceExaminePlanDto getDeviceExaminePlan(Integer planId) {
        DeviceExaminePlan examinePlan = baseMapper.selectById(planId);
        DeviceExaminePlanDto deviceExaminePlanDto = new DeviceExaminePlanDto();
        BeanUtils.copyProperties(examinePlan, deviceExaminePlanDto);
        // 查询详情
        List<DeviceExaminePlanDetails> list = deviceExaminePlanDetailsService.list(Wrappers.<DeviceExaminePlanDetails>lambdaQuery()
                .eq(DeviceExaminePlanDetails::getPlanId, planId)
                .orderByAsc(DeviceExaminePlanDetails::getCheckTime));
        deviceExaminePlanDto.setExaminePlanDetailsList(list);

        return deviceExaminePlanDto;
    }
}

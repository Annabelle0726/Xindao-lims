package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.dto.DeviceExamineRecordDto;
import com.ruoyi.device.mapper.DeviceExaminePlanDetailsMapper;
import com.ruoyi.device.mapper.DeviceExamineRecordMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import com.ruoyi.device.pojo.DeviceExamineRecord;
import com.ruoyi.device.pojo.DeviceExamineRecordDetail;
import com.ruoyi.device.service.DeviceExamineRecordDetailService;
import com.ruoyi.device.service.DeviceExamineRecordService;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
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
 * 设备核查记录表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceExamineRecordServiceImpl extends ServiceImpl<DeviceExamineRecordMapper, DeviceExamineRecord> implements DeviceExamineRecordService {

    @Resource
    private DeviceExamineRecordDetailService deviceExamineRecordDetailService;
    @Resource
    private DeviceExaminePlanDetailsMapper deviceExaminePlanDetailsMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private InformationNotificationService informationNotificationService;

    /**
     * 查询核查记录
     *
     * @return
     */
    @Override
    public DeviceExamineRecordDto getExamineRecord(Integer planDetailsId) {
        DeviceExamineRecordDto deviceExamineRecord = baseMapper.getExamineRecord(planDetailsId);

        // 判断是否为空
        if (deviceExamineRecord == null) {
            deviceExamineRecord = new DeviceExamineRecordDto();
            // 查询设备详情
            DeviceExaminePlanDetails deviceExaminePlanDetails = deviceExaminePlanDetailsMapper.selectById(planDetailsId);
            Device device = deviceMapper.selectById(deviceExaminePlanDetails.getDeviceId());
            if (device != null) {
                deviceExamineRecord.setDeviceName(device.getDeviceName());
                deviceExamineRecord.setDeviceNumber(device.getManagementNumber());
                deviceExamineRecord.setPlanDetailsId(deviceExaminePlanDetails.getPlanDetailsId());
            }
        } else {
            // 查询详情
            List<DeviceExamineRecordDetail> list = deviceExamineRecordDetailService.list(Wrappers.<DeviceExamineRecordDetail>lambdaQuery()
                    .eq(DeviceExamineRecordDetail::getRecordId, deviceExamineRecord.getRecordId()));
            deviceExamineRecord.setRecordDetailList(list);
        }

        return deviceExamineRecord;
    }

    /**
     * 新增核查记录
     *
     * @return
     */
    @Override
    public boolean addExamineRecord(DeviceExamineRecordDto deviceExamineRecordDto) {
        if (deviceExamineRecordDto.getPlanDetailsId() == null) {
            throw new ErrorException("缺少计划详细信息id");
        }
        if (deviceExamineRecordDto.getRecordId() == null) {
            Integer userId = SecurityUtils.getUserId().intValue();
            User user = userMapper.selectById(SecurityUtils.getUserId());
            deviceExamineRecordDto.setCheckerUserId(userId);
            deviceExamineRecordDto.setCheckerUser(user.getName());

            // 查询审查人id
            if (deviceExamineRecordDto.getReviewUserId() != null) {
                User reviewUser = userMapper.selectById(deviceExamineRecordDto.getReviewUserId());
                deviceExamineRecordDto.setReviewUser(reviewUser.getName());

                // 消息发送
                InformationNotification info = new InformationNotification();
                // 发送人
                info.setCreateUser(user.getName());
                info.setMessageType("6");
                info.setTheme("CNAS设备核查计划待批准");
                info.setContent("设备编号为: " + deviceExamineRecordDto.getDeviceNumber() + " 设备核查计划待审批");
                info.setSenderId(userId);
                // 接收人
                info.setConsigneeId(deviceExamineRecordDto.getReviewUserId());
                info.setJumpPath(MenuJumpPathConstants.DEVICE);
                informationNotificationService.addInformationNotification(info);

                // 发送企业微信通知
                threadPoolTaskExecutor.execute(() -> {
                    // 查询发送人
                    User people = userMapper.selectById(deviceExamineRecordDto.getReviewUserId());
                    String message = "";
                    message += "CNAS设备核查计划待批准";
                    message += "\n请去资源要求-设备-设备核查计划审批";
                    message += "\n" + "设备编号为: " + deviceExamineRecordDto.getDeviceNumber() + "设备核查计划待审批";
                    //发送企业微信消息通知
                    try {
                        WxCpUtils.inform(people.getAccount(), message, null);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        }
        this.saveOrUpdate(deviceExamineRecordDto);

        // 删除全部详情
        deviceExamineRecordDetailService.remove(Wrappers.<DeviceExamineRecordDetail>lambdaQuery()
                .eq(DeviceExamineRecordDetail::getRecordId, deviceExamineRecordDto.getRecordId()));
        //添加详情
        if (CollectionUtils.isNotEmpty(deviceExamineRecordDto.getRecordDetailList())) {
            for (DeviceExamineRecordDetail deviceExamineRecordDetail : deviceExamineRecordDto.getRecordDetailList()) {
                deviceExamineRecordDetail.setRecordId(deviceExamineRecordDto.getRecordId());
            }
            deviceExamineRecordDetailService.saveBatch(deviceExamineRecordDto.getRecordDetailList());
        }

        return true;
    }


    /**
     * 复核核查记录
     *
     * @return
     */
    @Override
    public boolean reviewExamineRecord(DeviceExamineRecordDto dto) {
        if (dto.getPlanDetailsId() == null) {
            throw new ErrorException("缺少计划详细信息id");
        }
        LambdaUpdateWrapper<DeviceExamineRecord> wrapper = Wrappers.<DeviceExamineRecord>lambdaUpdate()
                .eq(DeviceExamineRecord::getPlanDetailsId, dto.getPlanDetailsId())
                .set(DeviceExamineRecord::getReviewStatus, dto.getReviewStatus())
                .set(DeviceExamineRecord::getReviewRemark, dto.getReviewRemark());

        // 为0清除审核人
        if (dto.getReviewStatus().equals(0)) {
            wrapper.set(DeviceExamineRecord::getReviewUserId, null)
                    .set(DeviceExamineRecord::getReviewUser, null);
        }

        this.update(wrapper);
        return true;
    }

    /**
     * 导出复核核查记录
     *
     * @param planDetailsId 复核核查记录id
     * @return
     */
    @Override
    public void exportReviewExamineRecordDetail(Integer planDetailsId, HttpServletResponse response) {
        // 查询复核核查记录
        DeviceExamineRecordDto deviceExamineRecordDto = baseMapper.selectReviewExamineRecordDto(planDetailsId);

        // 查询复核核查记录详情
        List<DeviceExamineRecordDetail> deviceExamineRecordDetailList = deviceExamineRecordDetailService.list(Wrappers.<DeviceExamineRecordDetail>lambdaQuery().eq(DeviceExamineRecordDetail::getRecordId, deviceExamineRecordDto.getRecordId()));

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/examine-record.docx");
        Configure configure = Configure.builder()
                .bind("deviceExamineRecordDetailList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceExamineRecordDto", deviceExamineRecordDto);
                    put("deviceExamineRecordDetailList", deviceExamineRecordDetailList);
                    //获取核查人的签名地址
                    put("checkerUserUrl", UserUtils.getFinalUserSignatureUrl(deviceExamineRecordDto.getCheckerUserId()));
                    //获取审查人的签名地址
                    put("reviewUserUrl", UserUtils.getFinalUserSignatureUrl(deviceExamineRecordDto.getReviewUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String deviceName = StringUtils.isNotEmpty(deviceExamineRecordDto.getDeviceName()) ? deviceExamineRecordDto.getDeviceName() : "";
            String fileName = URLEncoder.encode(
                    deviceName+ "核查记录", "UTF-8");
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

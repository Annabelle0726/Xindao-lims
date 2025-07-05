package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.dto.DeviceMaintenancePlanDetailsDto;
import com.ruoyi.device.dto.DeviceMaintenancePlanDto;
import com.ruoyi.device.mapper.DeviceMaintenancePlanDetailsMapper;
import com.ruoyi.device.mapper.DeviceMaintenancePlanMapper;
import com.ruoyi.device.pojo.DeviceMaintenancePlan;
import com.ruoyi.device.pojo.DeviceMaintenancePlanDetails;
import com.ruoyi.device.service.DeviceMaintenancePlanDetailsService;
import com.ruoyi.device.service.DeviceMaintenancePlanService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备保养计划表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 06:10:52
 */
@Service
public class DeviceMaintenancePlanServiceImpl extends ServiceImpl<DeviceMaintenancePlanMapper, DeviceMaintenancePlan> implements DeviceMaintenancePlanService {

    @Resource
    private DeviceMaintenancePlanDetailsService deviceMaintenancePlanDetailsService;
    @Resource
    private DeviceMaintenancePlanDetailsMapper deviceMaintenancePlanDetailsMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InformationNotificationService informationNotificationService;
    @Resource
    private UserMapper userMapper;

    /**
     * 分页查询设备保养计划
     *
     * @param page
     * @return
     */
    @Override
    public Result<IPage<DeviceMaintenancePlan>> selectDeviceMaintenancePlanByPage(Page page, DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        IPage<DeviceMaintenancePlan> iPage = baseMapper.selectDeviceParameterPage(page, QueryWrappers.queryWrappers(deviceMaintenancePlanDto));
        return Result.success(iPage);
    }

    /**
     * 新增设备保养计划
     *
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @Override
    public Result addMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        deviceMaintenancePlanDto.setCompilerId(userId);
        deviceMaintenancePlanDto.setCompiler(user.getName());
        deviceMaintenancePlanDto.setDatePreparation(LocalDateTime.now());

        // 查询审核人id
        if (deviceMaintenancePlanDto.getAuditId() != null) {
            User auditUser = userMapper.selectById(deviceMaintenancePlanDto.getAuditId());
            deviceMaintenancePlanDto.setAudit(auditUser.getName());
        }
        this.saveOrUpdate(deviceMaintenancePlanDto);

        // 详情赋值并保存
        List<DeviceMaintenancePlanDetailsDto> deviceMaintenancePlanDetails = deviceMaintenancePlanDto.getDeviceMaintenancePlanDetails();
        if (CollectionUtils.isNotEmpty(deviceMaintenancePlanDetails)) { // 详情不为空
            List<DeviceMaintenancePlanDetails> collect = deviceMaintenancePlanDetails.stream().map(deviceMaintenancePlanDetail -> { // 遍历详情
                deviceMaintenancePlanDetail.setDeviceId(deviceMaintenancePlanDetail.getDeviceId()); // 设备ID
                deviceMaintenancePlanDetail.setMaintenancePlanId(deviceMaintenancePlanDto.getMaintenancePlanId()); // 保养计划ID
                DeviceMaintenancePlanDetails planDetails = new DeviceMaintenancePlanDetails();
                BeanUtils.copyProperties(deviceMaintenancePlanDetail, planDetails);
                return planDetails;
            }).collect(Collectors.toList());
            deviceMaintenancePlanDetailsService.saveBatch(collect);
        }

        return Result.success();
    }

    /**
     * 修改设备保养计划
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @Override
    public Result updateMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        // 查询审核人id
        if (deviceMaintenancePlanDto.getAuditId() != null) {
            User auditUser = userMapper.selectById(deviceMaintenancePlanDto.getAuditId());
            deviceMaintenancePlanDto.setAudit(auditUser.getName());
        }
        this.saveOrUpdate(deviceMaintenancePlanDto);

        // 删除原本的详情
        deviceMaintenancePlanDetailsService.remove(Wrappers.<DeviceMaintenancePlanDetails>lambdaQuery().eq(DeviceMaintenancePlanDetails::getMaintenancePlanId, deviceMaintenancePlanDto.getMaintenancePlanId()));
        // 详情赋值并保存
        List<DeviceMaintenancePlanDetailsDto> deviceMaintenancePlanDetails = deviceMaintenancePlanDto.getDeviceMaintenancePlanDetails();
        if (CollectionUtils.isNotEmpty(deviceMaintenancePlanDetails)) { // 详情不为空
            List<DeviceMaintenancePlanDetails> collect = deviceMaintenancePlanDetails.stream().map(deviceMaintenancePlanDetail -> { // 遍历详情
                deviceMaintenancePlanDetail.setDeviceId(deviceMaintenancePlanDto.getDeviceId()); // 设备ID
                deviceMaintenancePlanDetail.setMaintenancePlanId(deviceMaintenancePlanDto.getMaintenancePlanId()); // 保养计划ID
                DeviceMaintenancePlanDetails planDetails = new DeviceMaintenancePlanDetails();
                BeanUtils.copyProperties(deviceMaintenancePlanDetail, planDetails);
                return planDetails;
            }).collect(Collectors.toList());
            deviceMaintenancePlanDetailsService.saveBatch(collect);
        }
        return Result.success();
    }

    /**
     * 删除设备保养计划
     *
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @Override
    public Result deleteMaintenancePlan(DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        this.removeById(deviceMaintenancePlanDto);
        deviceMaintenancePlanDetailsService.remove(Wrappers.<DeviceMaintenancePlanDetails>lambdaQuery().eq(DeviceMaintenancePlanDetails::getMaintenancePlanId, deviceMaintenancePlanDto.getMaintenancePlanId()));
        return Result.success();
    }

    /**
     * 导出设备保养计划
     *
     * @param maintenancePlanId 设备保养计划id
     * @param response          响应
     */
    @Override
    public Result exportDeviceMaintenancePlanDto(Integer maintenancePlanId, HttpServletResponse response) {
        // 查询设备保养计划
        DeviceMaintenancePlanDto deviceMaintenancePlan = baseMapper.selectMaintenancePlanById(maintenancePlanId);

        // 查询设备保养计划详情
        List<DeviceMaintenancePlanDetailsDto> deviceMaintenancePlanDetailsDtoList = deviceMaintenancePlanDetailsMapper.deviceInspectionRecordDetailsList(maintenancePlanId);
        // 设置序号
        deviceMaintenancePlanDetailsDtoList.forEach(deviceInspectionRecordDetails -> {
            deviceInspectionRecordDetails.setIndex(deviceMaintenancePlanDetailsDtoList.indexOf(deviceInspectionRecordDetails) + 1);
        });

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/maintenance-plan.docx");
        Configure configure = Configure.builder()
                .bind("deviceMaintenancePlanDetailsDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceMaintenancePlan", deviceMaintenancePlan);
                    put("deviceMaintenancePlanDetailsDtoList", deviceMaintenancePlanDetailsDtoList);
                    // 编制人签名地址
                    put("compilerUrl", UserUtils.getFinalUserSignatureUrl(deviceMaintenancePlan.getCompilerId()));
                    // 审核人签名地址
                    put("auditUrl", UserUtils.getFinalUserSignatureUrl(deviceMaintenancePlan.getAuditId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "设备保养计划表", "UTF-8");
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
        return Result.success();
    }

    /**
     * 查询设备保养计划详情
     *
     * @param maintenancePlanId 设备保养计划id
     */
    @Override
    public Result<DeviceMaintenancePlanDto> getMaintenancePlanDetail(Integer maintenancePlanId) {
        // 查询设备保养计划
        DeviceMaintenancePlan deviceMaintenancePlan = baseMapper.selectById(maintenancePlanId);
        // 查询详情
        DeviceMaintenancePlanDto deviceMaintenancePlanDto = new DeviceMaintenancePlanDto();
        BeanUtils.copyProperties(deviceMaintenancePlan, deviceMaintenancePlanDto);
        deviceMaintenancePlanDto.setDeviceMaintenancePlanDetails(deviceMaintenancePlanDetailsMapper.deviceInspectionRecordDetailsList(maintenancePlanId));
        return Result.success(deviceMaintenancePlanDto);
    }

    /**
     * 提交审核
     * @param deviceMaintenancePlanDto
     * @return
     */
    @Override
    public Result submitReviewMaintenancePlanStatus(DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        User audit = userMapper.selectById(deviceMaintenancePlanDto.getAuditId());
        this.update(Wrappers.<DeviceMaintenancePlan>lambdaUpdate()
                .eq(DeviceMaintenancePlan::getMaintenancePlanId, deviceMaintenancePlanDto.getMaintenancePlanId())
                .set(DeviceMaintenancePlan::getAuditId, audit.getId())
                .set(DeviceMaintenancePlan::getAudit, audit.getName())
                .set(DeviceMaintenancePlan::getStatus, null)
                .set(DeviceMaintenancePlan::getAuditRemark, null)
                .set(DeviceMaintenancePlan::getAuditDate, null)
        );

        DeviceMaintenancePlan deviceMaintenancePlan = baseMapper.selectById(deviceMaintenancePlanDto.getMaintenancePlanId());

        // 发送消息
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS设备保养计划审核通知");
        info.setContent(deviceMaintenancePlan.getPlanYear() + "年的设备保养计划待审核");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(audit.getId());
        info.setJumpPath(MenuJumpPathConstants.DEVICE);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            String message = "";
            message += "CNAS设备保养计划核通知";
            message += "\n请去资源管理-设备一层目录-设备保养计划填写";
            message += "\n" + deviceMaintenancePlan.getPlanYear() + "年的设备保养计划待核";
            //发送企业微信消息通知
            try {
                WxCpUtils.inform(audit.getAccount(), message, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return Result.success();
    }

    /**
     * 审核设备保养计划
     *
     * @param deviceMaintenancePlanDto 设备保养计划
     */
    @Override
    public Result reviewMaintenancePlanStatus(DeviceMaintenancePlanDto deviceMaintenancePlanDto) {
        LambdaUpdateWrapper<DeviceMaintenancePlan> wrapper = Wrappers.<DeviceMaintenancePlan>lambdaUpdate()
                .eq(DeviceMaintenancePlan::getMaintenancePlanId, deviceMaintenancePlanDto.getMaintenancePlanId())
                .set(DeviceMaintenancePlan::getStatus, deviceMaintenancePlanDto.getStatus())
                .set(DeviceMaintenancePlan::getAuditRemark, deviceMaintenancePlanDto.getAuditRemark())
                .set(DeviceMaintenancePlan::getAuditDate, LocalDateTime.now());

        this.update(wrapper); // 更新
        return Result.success();
    }


}

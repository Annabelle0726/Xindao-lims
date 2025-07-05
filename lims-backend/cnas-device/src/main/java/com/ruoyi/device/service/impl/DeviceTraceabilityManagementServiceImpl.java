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
import com.ruoyi.device.dto.DeviceTraceabilityManagementDetailsDto;
import com.ruoyi.device.dto.DeviceTraceabilityManagementDto;
import com.ruoyi.device.mapper.DeviceTraceabilityManagementDetailsMapper;
import com.ruoyi.device.mapper.DeviceTraceabilityManagementMapper;
import com.ruoyi.device.pojo.DeviceTraceabilityManagement;
import com.ruoyi.device.pojo.DeviceTraceabilityManagementDetails;
import com.ruoyi.device.service.DeviceTraceabilityManagementDetailsService;
import com.ruoyi.device.service.DeviceTraceabilityManagementService;
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
 * 设备量值溯源计划表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 02:27:44
 */
@Service
public class DeviceTraceabilityManagementServiceImpl extends ServiceImpl<DeviceTraceabilityManagementMapper, DeviceTraceabilityManagement> implements DeviceTraceabilityManagementService {

    @Resource
    private DeviceTraceabilityManagementDetailsService deviceTraceabilityManagementDetailsService;

    @Resource
    private DeviceTraceabilityManagementDetailsMapper deviceTraceabilityManagementDetailsMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InformationNotificationService informationNotificationService;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页查询设备量值溯源计划
     *
     * @param page
     * @return
     */
    @Override
    public Result<IPage<DeviceTraceabilityManagement>> selectDeviceTraceabilityManagementByPage(Page page, DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        IPage<DeviceTraceabilityManagement> iPage = baseMapper.selectDeviceParameterPage(page, QueryWrappers.queryWrappers(deviceTraceabilityManagementDto));
        return Result.success(iPage);
    }


    /**
     * 新增设备量值溯源计划
     *
     * @param deviceTraceabilityManagementDto 设备量值溯源计划
     */
    @Override
    public Result addTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        deviceTraceabilityManagementDto.setCompilerId(userId);
        deviceTraceabilityManagementDto.setCompiler(user.getName());
        deviceTraceabilityManagementDto.setDatePreparation(LocalDateTime.now());

        // 查询审核人id
        if (deviceTraceabilityManagementDto.getAuditId() != null) {
            User auditUser = userMapper.selectById(deviceTraceabilityManagementDto.getAuditId());
            deviceTraceabilityManagementDto.setAudit(auditUser.getName());
        }
        this.saveOrUpdate(deviceTraceabilityManagementDto);

        // 详情赋值并保存
        List<DeviceTraceabilityManagementDetailsDto> deviceTraceabilityManagementDetails = deviceTraceabilityManagementDto.getDeviceTraceabilityManagementDetails();
        if (CollectionUtils.isNotEmpty(deviceTraceabilityManagementDetails)) { // 详情不为空
            List<DeviceTraceabilityManagementDetails> collect = deviceTraceabilityManagementDetails.stream().map(detailsDto -> {
                detailsDto.setTraceabilityManagementId(deviceTraceabilityManagementDto.getTraceabilityManagementId()); // 量值溯源计划ID
                DeviceTraceabilityManagementDetails details = new DeviceTraceabilityManagementDetails();
                BeanUtils.copyProperties(detailsDto, details);
                return details;
            }).collect(Collectors.toList());
            deviceTraceabilityManagementDetailsService.saveBatch(collect);
        }

        return Result.success();
    }

    /**
     * 修改设备量值溯源计划
     *
     * @param deviceTraceabilityManagementDto 设备量值溯源计划
     */
    @Override
    public Result updateTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        // 查询审核人id
        if (deviceTraceabilityManagementDto.getAuditId() != null) {
            User auditUser = userMapper.selectById(deviceTraceabilityManagementDto.getAuditId());
            deviceTraceabilityManagementDto.setAudit(auditUser.getName());
        }
        this.saveOrUpdate(deviceTraceabilityManagementDto);

        // 删除原本的详情
        deviceTraceabilityManagementDetailsService.remove(Wrappers.<DeviceTraceabilityManagementDetails>lambdaQuery().eq(DeviceTraceabilityManagementDetails::getTraceabilityManagementId, deviceTraceabilityManagementDto.getTraceabilityManagementId()));
        // 详情赋值并保存
        List<DeviceTraceabilityManagementDetailsDto> deviceTraceabilityManagementDetails = deviceTraceabilityManagementDto.getDeviceTraceabilityManagementDetails();
        if (CollectionUtils.isNotEmpty(deviceTraceabilityManagementDetails)) { // 详情不为空
            List<DeviceTraceabilityManagementDetails> collect = deviceTraceabilityManagementDetails.stream().map(detailsDto -> {
                detailsDto.setTraceabilityManagementId(deviceTraceabilityManagementDto.getTraceabilityManagementId()); // 量值溯源计划ID
                DeviceTraceabilityManagementDetails details = new DeviceTraceabilityManagementDetails();
                BeanUtils.copyProperties(detailsDto, details);
                return details;
            }).collect(Collectors.toList());
            deviceTraceabilityManagementDetailsService.saveBatch(collect);
        }
        return Result.success();
    }

    /**
     * 删除设备量值溯源计划
     *
     * @param deviceTraceabilityManagementDto 设备量值溯源计划
     */
    @Override
    public Result deleteTraceabilityManagement(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        this.removeById(deviceTraceabilityManagementDto);
        deviceTraceabilityManagementDetailsService.remove(Wrappers.<DeviceTraceabilityManagementDetails>lambdaQuery().eq(DeviceTraceabilityManagementDetails::getTraceabilityManagementId, deviceTraceabilityManagementDto.getTraceabilityManagementId()));
        return Result.success();
    }

    /**
     * 查询设备量值溯源计划详情
     *
     * @param traceabilityManagementId 设备量值溯源计划id
     */
    @Override
    public Result<DeviceTraceabilityManagementDto> getTraceabilityManagementDetail(Integer traceabilityManagementId) {
        // 查询设备量值溯源计划
        DeviceTraceabilityManagement deviceTraceabilityManagement = baseMapper.selectById(traceabilityManagementId);
        // 查询详情
        DeviceTraceabilityManagementDto deviceTraceabilityManagementDto = new DeviceTraceabilityManagementDto();
        BeanUtils.copyProperties(deviceTraceabilityManagement, deviceTraceabilityManagementDto);
        deviceTraceabilityManagementDto.setDeviceTraceabilityManagementDetails(deviceTraceabilityManagementDetailsMapper.deviceTraceabilityManagementDetailsList(traceabilityManagementId));
        return Result.success(deviceTraceabilityManagementDto);
    }

    /**
     * 提交批准
     * @param deviceTraceabilityManagementDto
     * @return
     */
    @Override
    public Result submitReviewTraceabilityManagementStatus(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        User audit = userMapper.selectById(deviceTraceabilityManagementDto.getAuditId());
        this.update(Wrappers.<DeviceTraceabilityManagement>lambdaUpdate()
                .eq(DeviceTraceabilityManagement::getTraceabilityManagementId, deviceTraceabilityManagementDto.getTraceabilityManagementId())
                .set(DeviceTraceabilityManagement::getAuditId, audit.getId())
                .set(DeviceTraceabilityManagement::getAudit, audit.getName())
                .set(DeviceTraceabilityManagement::getAuditDate, null)
                .set(DeviceTraceabilityManagement::getStatus, null)
                .set(DeviceTraceabilityManagement::getAuditRemark, null));

        DeviceTraceabilityManagement deviceTraceabilityManagement = baseMapper.selectById(deviceTraceabilityManagementDto.getTraceabilityManagementId());

        // 发送消息
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        // 消息发送
        InformationNotification info = new InformationNotification();
        // 发送人
        info.setCreateUser(user.getName());
        info.setMessageType("6");
        info.setTheme("CNAS设备量值溯源计划批准通知");
        info.setContent(deviceTraceabilityManagement.getPlanYear() + "年的设备量值溯源计划待批准");
        info.setSenderId(userId);
        // 接收人
        info.setConsigneeId(audit.getId());
        info.setJumpPath(MenuJumpPathConstants.DEVICE);
        informationNotificationService.addInformationNotification(info);

        // 发送企业微信通知
        threadPoolTaskExecutor.execute(() -> {
            String message = "";
            message += "CNAS设备量值溯源计划核通知";
            message += "\n请去资源管理-设备一层目录-设备量值溯源计划填写";
            message += "\n" + deviceTraceabilityManagement.getPlanYear() + "年的设备量值溯源计划待批准";
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
     * 审核设备量值溯源计划
     *
     * @param deviceTraceabilityManagementDto 设备量值溯源计划
     */
    @Override
    public Result reviewTraceabilityManagementStatus(DeviceTraceabilityManagementDto deviceTraceabilityManagementDto) {
        LambdaUpdateWrapper<DeviceTraceabilityManagement> wrapper = Wrappers.<DeviceTraceabilityManagement>lambdaUpdate()
                .eq(DeviceTraceabilityManagement::getTraceabilityManagementId, deviceTraceabilityManagementDto.getTraceabilityManagementId())
                .set(DeviceTraceabilityManagement::getAuditDate, LocalDateTime.now())
                .set(DeviceTraceabilityManagement::getStatus, deviceTraceabilityManagementDto.getStatus())        // 审核状态
                .set(DeviceTraceabilityManagement::getAuditRemark, deviceTraceabilityManagementDto.getAuditRemark());// 审核备注

        this.update(wrapper); // 更新
        return Result.success();
    }

    /**
     * 导出设备量值溯源计划
     *
     * @param traceabilityManagementId 设备量值溯源计划id
     */
    @Override
    public Result exportDeviceTraceabilityManagementDto(Integer traceabilityManagementId, HttpServletResponse response) {
        // 查询设备保养计划
        DeviceTraceabilityManagementDto deviceTraceabilityManagement = baseMapper.selectDeviceTraceabilityManagementById(traceabilityManagementId);

        // 查询设备保养计划详情
        List<DeviceTraceabilityManagementDetailsDto> deviceTraceabilityManagementDetailsDtoList = deviceTraceabilityManagementDetailsMapper.deviceTraceabilityManagementDetailsList(traceabilityManagementId);
        // 设置序号
        deviceTraceabilityManagementDetailsDtoList.forEach(deviceTraceabilityManagementDetails -> {
            deviceTraceabilityManagementDetails.setIndex(deviceTraceabilityManagementDetailsDtoList.indexOf(deviceTraceabilityManagementDetails) + 1);
        });

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/traceability-management-details.docx");
        Configure configure = Configure.builder()
                .bind("deviceTraceabilityManagementDetailsDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceTraceabilityManagement", deviceTraceabilityManagement);
                    put("deviceTraceabilityManagementDetailsDtoList", deviceTraceabilityManagementDetailsDtoList);
                    // 编制人签名地址
                    put("compilerUrl", UserUtils.getFinalUserSignatureUrl(deviceTraceabilityManagement.getCompilerId()));
                    // 审核人签名地址
                    put("auditUrl", UserUtils.getFinalUserSignatureUrl(deviceTraceabilityManagement.getAuditId()));
                }});

        try {
            response.setContentType("application/msword");
            String finalFileName = deviceTraceabilityManagement.getFileName() == null ? "" : deviceTraceabilityManagement.getFileName() + "_";
            String fileName = URLEncoder.encode(
                    finalFileName+ "设备量值溯源计划", "UTF-8");
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

}

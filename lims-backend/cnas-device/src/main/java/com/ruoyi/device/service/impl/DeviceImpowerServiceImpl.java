package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.constant.MenuJumpPathConstants;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.InformationNotification;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WxCpUtils;
import com.ruoyi.device.dto.DeviceImpowerDetailsDto;
import com.ruoyi.device.dto.DeviceImpowerDto;
import com.ruoyi.device.mapper.DeviceImpowerDetailsMapper;
import com.ruoyi.device.pojo.DeviceImpower;
import com.ruoyi.device.mapper.DeviceImpowerMapper;
import com.ruoyi.device.pojo.DeviceImpower;
import com.ruoyi.device.pojo.DeviceImpowerDetails;
import com.ruoyi.device.service.DeviceImpowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.device.service.DeviceImpowerDetailsService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import com.ruoyi.system.service.InformationNotificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备使用表 服务实现类
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceImpowerServiceImpl extends ServiceImpl<DeviceImpowerMapper, DeviceImpower> implements DeviceImpowerService {


    @Resource
    private DeviceImpowerDetailsService deviceImpowerDetailsService;

    @Resource
    private DeviceImpowerDetailsMapper deviceImpowerDetailsMapper;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private InformationNotificationService informationNotificationService;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页查询设备使用
     *
     * @param page
     * @return
     */
    @Override
    public Result<IPage<DeviceImpower>> selectDeviceImpowerByPage(Page page, DeviceImpowerDto deviceImpowerDto) {
        IPage<DeviceImpower> iPage = baseMapper.selectDeviceParameterPage(page, QueryWrappers.queryWrappers(deviceImpowerDto));
        return Result.success(iPage);
    }


    /**
     * 新增设备使用
     *
     * @param deviceImpowerDto 设备使用
     */
    @Override
    public Result addImpower(DeviceImpowerDto deviceImpowerDto) {
        Integer userId = SecurityUtils.getUserId().intValue();
        User user = userMapper.selectById(userId);
        deviceImpowerDto.setCompilerId(userId);
        deviceImpowerDto.setCompiler(user.getName());
        deviceImpowerDto.setDatePreparation(LocalDateTime.now());

        // 查询审核人id
        if (deviceImpowerDto.getAuditId() != null) {
            User auditUser = userMapper.selectById(deviceImpowerDto.getAuditId());
            deviceImpowerDto.setAudit(auditUser.getName());
        }
        this.saveOrUpdate(deviceImpowerDto);

        // 详情赋值并保存
        List<DeviceImpowerDetailsDto> deviceImpowerDetails = deviceImpowerDto.getDeviceImpowerDetails();
        if (CollectionUtils.isNotEmpty(deviceImpowerDetails)) { // 详情不为空
            List<DeviceImpowerDetails> collect = deviceImpowerDetails.stream().map(detailsDto -> {
                detailsDto.setImpowerId(deviceImpowerDto.getImpowerId()); // 使用ID
                DeviceImpowerDetails details = new DeviceImpowerDetails();
                BeanUtils.copyProperties(detailsDto, details);
                return details;
            }).collect(Collectors.toList());
            deviceImpowerDetailsService.saveBatch(collect);
        }

        return Result.success();
    }

    /**
     * 修改设备使用
     *
     * @param deviceImpowerDto 设备使用
     */
    @Override
    public Result updateImpower(DeviceImpowerDto deviceImpowerDto) {
        // 查询审核人id
        if (deviceImpowerDto.getAuditId() != null) {
            User auditUser = userMapper.selectById(deviceImpowerDto.getAuditId());
            deviceImpowerDto.setAudit(auditUser.getName());
        }
        this.saveOrUpdate(deviceImpowerDto);

        // 删除原本的详情
        deviceImpowerDetailsService.remove(Wrappers.<DeviceImpowerDetails>lambdaQuery().eq(DeviceImpowerDetails::getImpowerId, deviceImpowerDto.getImpowerId()));
        // 详情赋值并保存
        List<DeviceImpowerDetailsDto> deviceImpowerDetails = deviceImpowerDto.getDeviceImpowerDetails();
        if (CollectionUtils.isNotEmpty(deviceImpowerDetails)) { // 详情不为空
            List<DeviceImpowerDetails> collect = deviceImpowerDetails.stream().map(detailsDto -> {
                detailsDto.setImpowerId(deviceImpowerDto.getImpowerId()); // 使用ID
                DeviceImpowerDetails details = new DeviceImpowerDetails();
                BeanUtils.copyProperties(detailsDto, details);
                return details;
            }).collect(Collectors.toList());
            deviceImpowerDetailsService.saveBatch(collect);
        }
        return Result.success();
    }

    /**
     * 删除设备使用
     *
     * @param deviceImpowerDto 设备使用
     */
    @Override
    public Result deleteImpower(DeviceImpowerDto deviceImpowerDto) {
        this.removeById(deviceImpowerDto);
        deviceImpowerDetailsService.remove(Wrappers.<DeviceImpowerDetails>lambdaQuery().eq(DeviceImpowerDetails::getImpowerId, deviceImpowerDto.getImpowerId()));
        return Result.success();
    }

    /**
     * 查询设备使用详情
     *
     * @param impowerId 设备使用id
     */
    @Override
    public Result<DeviceImpowerDto> getImpowerDetail(Integer impowerId) {
        // 查询设备使用
        DeviceImpower deviceImpower = baseMapper.selectById(impowerId);
        // 查询详情
        DeviceImpowerDto deviceImpowerDto = new DeviceImpowerDto();
        BeanUtils.copyProperties(deviceImpower, deviceImpowerDto);
        deviceImpowerDto.setDeviceImpowerDetails(deviceImpowerDetailsMapper.deviceImpowerDetailsList(impowerId));
        return Result.success(deviceImpowerDto);
    }

    /**
     * 提交批准
     * @param deviceImpowerDto
     * @return
     */
    @Override
    public Result submitReviewImpowerStatus(DeviceImpowerDto deviceImpowerDto) {
        User audit = userMapper.selectById(deviceImpowerDto.getAuditId());
        this.update(Wrappers.<DeviceImpower>lambdaUpdate()
                .eq(DeviceImpower::getImpowerId, deviceImpowerDto.getImpowerId())
                .set(DeviceImpower::getAuditId, audit.getId())
                .set(DeviceImpower::getAudit, audit.getName())
                .set(DeviceImpower::getAuditDate, null)
                .set(DeviceImpower::getStatus, null)
                .set(DeviceImpower::getAuditRemark, null));

        DeviceImpower deviceImpower = baseMapper.selectById(deviceImpowerDto.getImpowerId());

//        // 发送消息
//        Integer userId = SecurityUtils.getUserId().intValue();
//        User user = userMapper.selectById(userId);
//        // 消息发送
//        InformationNotification info = new InformationNotification();
//        // 发送人
//        info.setCreateUser(user.getName());
//        info.setMessageType("6");
//        info.setTheme("CNAS设备使用批准通知");
//        info.setContent(deviceImpower.getPlanYear() + "年的设备使用待批准");
//        info.setSenderId(userId);
//        // 接收人
//        info.setConsigneeId(audit.getId());
//        info.setJumpPath(MenuJumpPathConstants.DEVICE);
//        informationNotificationService.addInformationNotification(info);
//
//        // 发送企业微信通知
//        threadPoolTaskExecutor.execute(() -> {
//            String message = "";
//            message += "CNAS设备使用核通知";
//            message += "\n请去资源管理-设备一层目录-设备使用填写";
//            message += "\n" + deviceImpower.getPlanYear() + "年的设备使用待批准";
//            //发送企业微信消息通知
//            try {
//                WxCpUtils.inform(audit.getAccount(), message, null);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
        return Result.success();
    }

    /**
     *
     *
     * @param deviceImpowerDto
     */
    @Override
    public Result reviewImpowerStatus(DeviceImpowerDto deviceImpowerDto) {
        LambdaUpdateWrapper<DeviceImpower> wrapper = Wrappers.<DeviceImpower>lambdaUpdate()
                .eq(DeviceImpower::getImpowerId, deviceImpowerDto.getImpowerId())
                .set(DeviceImpower::getAuditDate, LocalDateTime.now())
                .set(DeviceImpower::getStatus, deviceImpowerDto.getStatus())        // 审核状态
                .set(DeviceImpower::getAuditRemark, deviceImpowerDto.getAuditRemark());// 审核备注

        this.update(wrapper); // 更新
        return Result.success();
    }

    /**
     * 导出设备使用
     *
     * @param impowerId 设备使用id
     */
    @Override
    public Result exportDeviceImpowerDto(Integer impowerId, HttpServletResponse response) {
        // 查询设备使用授权计划
        DeviceImpower deviceImpower = baseMapper.selectById(impowerId);
        DeviceImpowerDto deviceImpowerDto = new DeviceImpowerDto();
        BeanUtils.copyProperties(deviceImpower, deviceImpowerDto);
        // 格式化日期中英文
        DateTimeFormatter formatterCH = DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日");
        DateTimeFormatter formatterEN = DateTimeFormatter.ofPattern("yyyy 'Year' MM 'Month' dd 'Day'");
        deviceImpowerDto.setAuditDateCH(deviceImpower.getAuditDate() == null ? null : deviceImpower.getAuditDate().format(formatterCH));
        deviceImpowerDto.setAuditDateEN(deviceImpower.getAuditDate() == null ? null : deviceImpower.getAuditDate().format(formatterEN));

        // 查询设备保养计划详情
        List<DeviceImpowerDetailsDto> deviceImpowerDetailsDtoList = deviceImpowerDetailsMapper.deviceImpowerDetailsList(impowerId);
        // 设置序号
        deviceImpowerDetailsDtoList.forEach(deviceImpowerDetails -> {
            deviceImpowerDetails.setIndex(deviceImpowerDetailsDtoList.indexOf(deviceImpowerDetails) + 1);
        });

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-impower.docx");
        Configure configure = Configure.builder()
                .bind("deviceImpowerDetailsDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceImpower", deviceImpowerDto);
                    put("deviceImpowerDetailsDtoList", deviceImpowerDetailsDtoList);
                    // 授权人地址
                    put("auditUrl", UserUtils.getFinalUserSignatureUrl(deviceImpower.getAuditId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "设备使用授权", "UTF-8");
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

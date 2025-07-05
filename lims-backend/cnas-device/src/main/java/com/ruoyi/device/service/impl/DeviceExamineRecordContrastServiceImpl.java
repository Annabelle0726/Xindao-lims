package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceExamineRecordContrastDetailsDto;
import com.ruoyi.device.dto.DeviceExamineRecordContrastDto;
import com.ruoyi.device.mapper.DeviceExaminePlanDetailsMapper;
import com.ruoyi.device.mapper.DeviceExamineRecordContrastMapper;
import com.ruoyi.device.pojo.DeviceExaminePlanDetails;
import com.ruoyi.device.pojo.DeviceExamineRecordContrast;
import com.ruoyi.device.pojo.DeviceExamineRecordContrastDetails;
import com.ruoyi.device.service.DeviceExamineRecordContrastDetailsService;
import com.ruoyi.device.service.DeviceExamineRecordContrastService;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 设备核查记录对比表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-16 07:14:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceExamineRecordContrastServiceImpl extends ServiceImpl<DeviceExamineRecordContrastMapper, DeviceExamineRecordContrast> implements DeviceExamineRecordContrastService {

    @Resource
    private DeviceExamineRecordContrastDetailsService deviceExamineRecordContrastDetailsService;
    @Resource
    private DeviceExaminePlanDetailsMapper deviceExaminePlanDetailsMapper;
    @Resource
    private UserMapper userMapper;
    /**
     * 查询核查对比记录
     * @return
     */
    @Override
    public DeviceExamineRecordContrastDto getExamineRecordContrast(Integer planDetailsId) {
        DeviceExamineRecordContrastDto contrastDto = baseMapper.getExamineRecordContrast(planDetailsId);
        // 判断是否为空
        if (contrastDto == null) {
            contrastDto = new DeviceExamineRecordContrastDto();
            // 查询设备详情
            DeviceExaminePlanDetails deviceExaminePlanDetails = deviceExaminePlanDetailsMapper.selectById(planDetailsId);
            contrastDto.setPlanDetailsId(deviceExaminePlanDetails.getPlanDetailsId());

        } else {
            // 查询详情
            List<DeviceExamineRecordContrastDetails> list = deviceExamineRecordContrastDetailsService.list(Wrappers.<DeviceExamineRecordContrastDetails>lambdaQuery()
                    .eq(DeviceExamineRecordContrastDetails::getRecordContrastId, contrastDto.getRecordContrastId()));
            contrastDto.setRecordContrastDetailsList(list);
        }

        return contrastDto;
    }

    /**
     * 新增核查对比记录
     * @return
     */
    @Override
    public boolean addExamineRecordContrast(DeviceExamineRecordContrastDto dto) {
        if (dto.getPlanDetailsId() == null) {
            throw new ErrorException("缺少计划详细信息id");
        }
        if (dto.getRecordContrastId() == null) {
            Integer userId = SecurityUtils.getUserId().intValue();
            User user = userMapper.selectById(userId);
            dto.setCheckerUserId(userId);
            dto.setCheckerUser(user.getName());

            // 查询审查人id
            if (dto.getReviewUserId() != null) {
                User reviewUser = userMapper.selectById(dto.getReviewUserId());
                dto.setReviewUser(reviewUser.getName());
            }
        }
        this.saveOrUpdate(dto);

        // 删除全部详情
        deviceExamineRecordContrastDetailsService.remove(Wrappers.<DeviceExamineRecordContrastDetails>lambdaQuery()
                .eq(DeviceExamineRecordContrastDetails::getRecordContrastId, dto.getRecordContrastId()));
        //添加详情
        if (CollectionUtils.isNotEmpty(dto.getRecordContrastDetailsList())) {
            for (DeviceExamineRecordContrastDetails details : dto.getRecordContrastDetailsList()) {
                details.setRecordContrastId(dto.getRecordContrastId());
            }
            deviceExamineRecordContrastDetailsService.saveBatch(dto.getRecordContrastDetailsList());
        }
        return false;
    }


    /**
     * 审核核查对比记录
     * @return
     */
    @Override
    public boolean reviewExamineRecordContrast(DeviceExamineRecordContrastDto dto) {
        if (dto.getPlanDetailsId() == null) {
            throw new ErrorException("缺少计划详细信息id");
        }
        LambdaUpdateWrapper<DeviceExamineRecordContrast> wrapper = Wrappers.<DeviceExamineRecordContrast>lambdaUpdate()
                .eq(DeviceExamineRecordContrast::getPlanDetailsId, dto.getPlanDetailsId())
                .set(DeviceExamineRecordContrast::getReviewStatus, dto.getReviewStatus())
                .set(DeviceExamineRecordContrast::getReviewRemark, dto.getReviewRemark())
                .set(DeviceExamineRecordContrast::getReviewTime, LocalDateTime.now());
        // 为0清除审核人
        if (dto.getReviewStatus().equals(0)) {
            wrapper.set(DeviceExamineRecordContrast::getReviewUserId, null)
                    .set(DeviceExamineRecordContrast::getReviewUser, null);
        }

        this.update(wrapper);
        return true;
    }

    /**
     * 导出审核核查对比记录
     *
     * @param planDetailsId 详情id
     * @param response
     */
    @Override
    public void exportReviewExamineRecordContrast(Integer planDetailsId, HttpServletResponse response) {
        // 查询对比记录
        DeviceExamineRecordContrastDto deviceExamineRecordContrast = baseMapper.selectExamineRecordContrastDto(planDetailsId);

        // 查询对比记录详情
        List<DeviceExamineRecordContrastDetails> deviceExamineRecordContrastDetailList = deviceExamineRecordContrastDetailsService.list(Wrappers.<DeviceExamineRecordContrastDetails>lambdaQuery().eq(DeviceExamineRecordContrastDetails::getRecordContrastId, deviceExamineRecordContrast.getRecordContrastId()));
        // 设置序号
        ArrayList<DeviceExamineRecordContrastDetailsDto> deviceExamineRecordContrastDetailsDtoList = new ArrayList<>();
        deviceExamineRecordContrastDetailList.forEach(deviceExamineRecordContrastDetail -> {
            DeviceExamineRecordContrastDetailsDto deviceExamineRecordContrastDetailsDto = new DeviceExamineRecordContrastDetailsDto();
            BeanUtils.copyProperties(deviceExamineRecordContrastDetail, deviceExamineRecordContrastDetailsDto);
            deviceExamineRecordContrastDetailsDto.setIndex(deviceExamineRecordContrastDetailList.indexOf(deviceExamineRecordContrastDetail) + 1);
            deviceExamineRecordContrastDetailsDtoList.add(deviceExamineRecordContrastDetailsDto);
        });

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/review-examine-record-contrast.docx");
        Configure configure = Configure.builder()
                .bind("deviceExamineRecordContrastDetailsDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceExamineRecordContrast", deviceExamineRecordContrast);
                    put("deviceExamineRecordContrastDetailsDtoList", deviceExamineRecordContrastDetailsDtoList);
                    //获取核查人的签名地址
                    put("checkerUserUrl", UserUtils.getFinalUserSignatureUrl(deviceExamineRecordContrast.getCheckerUserId()));
                    //获取审查人的签名地址
                    put("reviewUserUrl", UserUtils.getFinalUserSignatureUrl(deviceExamineRecordContrast.getReviewUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "审核核查对比记录", "UTF-8");
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

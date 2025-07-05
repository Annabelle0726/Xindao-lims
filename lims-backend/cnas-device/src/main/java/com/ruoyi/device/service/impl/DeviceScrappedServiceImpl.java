package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceScrappedDto;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.mapper.DeviceScrappedMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceScrapped;
import com.ruoyi.device.service.DeviceScrappedService;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * <p>
 * 设备报废申请表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 01:53:47
 */
@Service
public class DeviceScrappedServiceImpl extends ServiceImpl<DeviceScrappedMapper, DeviceScrapped> implements DeviceScrappedService {

    @Resource
    private DeviceMapper deivceMapper;
    @Resource
    private UserMapper userMapper;


    /**
     * 设备报废申请列表
     *
     * @param deviceScrapped
     * @return
     */
    @Override
    public IPage<DeviceScrapped> pageDeviceScrapped(Page page, DeviceScrapped deviceScrapped) {
        return baseMapper.pageDeviceScrapped(page, QueryWrappers.queryWrappers(deviceScrapped));
    }

    /**
     * 新增设备报废申请
     *
     * @return
     */
    @Override
    public boolean addDeviceScrapped(DeviceScrapped deviceScrapped) {
        DeviceScrapped scrapped = new DeviceScrapped();
        // 当前登录用户信息和部门
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        switch (deviceScrapped.getFlowType()) {
            case 0:
                BeanUtils.copyProperties(deviceScrapped, scrapped);
                // 申请
                scrapped.setReasonsForScrap(deviceScrapped.getReasonsForScrap());
                scrapped.setApplicantUserId(user.getId());
                scrapped.setApplicantUser(user.getName());
                scrapped.setApplicantDate(LocalDate.now());

                // 处理人信息
                User departmentHeadUser = userMapper.selectById(deviceScrapped.getDepartmentHeadUserId());
                scrapped.setApplicantUserId(departmentHeadUser.getId());
                scrapped.setApplicantUser(departmentHeadUser.getName());

                baseMapper.insert(scrapped);
                break;
            case 1:
                scrapped.setScrappedId(deviceScrapped.getScrappedId());
                // 申请部门负责人意见
                scrapped.setDepartmentHeadOpinion(deviceScrapped.getDepartmentHeadOpinion());
                scrapped.setDepartmentHeadDate(LocalDate.now());

                // 计量室信息
                User meteringRoomUser = userMapper.selectById(deviceScrapped.getMeteringRoomUserId());
                scrapped.setMeteringRoomUserId(meteringRoomUser.getId());
                scrapped.setMeteringRoomUser(meteringRoomUser.getName());

                baseMapper.updateById(scrapped);
                break;
            case 2:
                scrapped.setScrappedId(deviceScrapped.getScrappedId());
                // 计量室意见
                scrapped.setMeteringRoomOpinion(deviceScrapped.getMeteringRoomOpinion());
                scrapped.setMeteringRoomDate(LocalDate.now());

                // 批准人信息
                User approverUser = userMapper.selectById(deviceScrapped.getApproverUserId());
                scrapped.setApproverUserId(approverUser.getId());
                scrapped.setApproverUser(approverUser.getName());

                baseMapper.updateById(scrapped);
                break;
            case 3:
                scrapped.setScrappedId(deviceScrapped.getScrappedId());
                //批准人
                scrapped.setApproverOpinion(deviceScrapped.getApproverOpinion());
                scrapped.setApproverDate(LocalDate.now());
                scrapped.setIsFinish(1);
                baseMapper.updateById(scrapped);
                break;
        }

        return true;
    }

    /**
     * 导出设备报废申请
     *
     * @param scrappedId 设备报废申请id
     * @param response   响应
     * @return 结果
     */
    @Override
    public Result<?> exportDeviceScrapped(Integer scrappedId, HttpServletResponse response) {
        // 查询报废数据
        DeviceScrappedDto deviceScrapped = baseMapper.selectDeviceScrappedById(scrappedId);
        if (deviceScrapped == null) {
            return Result.fail("设备报废申请不存在");
        }
        Device device = null;
        if (deviceScrapped.getDeviceId() != null) {
            device = deivceMapper.selectById(deviceScrapped.getDeviceId());
            device = device == null ? new Device() : device;
        }
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-scrapped.docx");
        Configure configure = Configure.builder()
                .build();
        Device finalDevice = device;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceScrapped", deviceScrapped);
                    put("device", finalDevice);
                    // 申请人签名
                    put("applicantUrl", UserUtils.getFinalUserSignatureUrl(deviceScrapped.getApplicantUserId()));
                    // 部门负责人签名
                    put("headUrl", UserUtils.getFinalUserSignatureUrl(deviceScrapped.getDepartmentHeadUserId()));
                    // 计量室签名
                    put("metrologyRoomUrl", UserUtils.getFinalUserSignatureUrl(deviceScrapped.getMeteringRoomUserId()));
                    // 批准人签名
                    put("approverUrl", UserUtils.getFinalUserSignatureUrl(deviceScrapped.getApproverUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String deviceName = device.getDeviceName() == null ? "" : device.getDeviceName();
            String fileName = URLEncoder.encode(
                    deviceName + "设备报废申请", "UTF-8");
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

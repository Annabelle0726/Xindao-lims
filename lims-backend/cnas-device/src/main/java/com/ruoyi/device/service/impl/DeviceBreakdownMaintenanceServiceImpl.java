package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceBreakdownMaintenance;
import com.ruoyi.device.mapper.DeviceBreakdownMaintenanceMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.service.DeviceBreakdownMaintenanceService;
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
 * 设备故障维修表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 04:50:57
 */
@Service
public class DeviceBreakdownMaintenanceServiceImpl extends ServiceImpl<DeviceBreakdownMaintenanceMapper, DeviceBreakdownMaintenance> implements DeviceBreakdownMaintenanceService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private DeviceMapper deivceMapper;

    /**
     * 设备故障维修列表
     * @param page
     * @param deviceBreakdownMaintenance
     * @return
     */
    @Override
    public IPage<DeviceBreakdownMaintenance> pageDeviceBreakdownMaintenance(Page page, DeviceBreakdownMaintenance deviceBreakdownMaintenance) {
        return baseMapper.pageDeviceBreakdownMaintenance(page, QueryWrappers.queryWrappers(deviceBreakdownMaintenance));
    }

    /**
     * 新增设备故障维修
     * @param deviceBreakdownMaintenance
     * @return
     */
    @Override
    public boolean addDeviceBreakdownMaintenance(DeviceBreakdownMaintenance deviceBreakdownMaintenance) {
        DeviceBreakdownMaintenance breakdownMaintenance = new DeviceBreakdownMaintenance();
        //当前登录用户
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());

        switch (deviceBreakdownMaintenance.getFlowType()) {
            case 0:
                BeanUtils.copyProperties(deviceBreakdownMaintenance, breakdownMaintenance);
                // 申请
                breakdownMaintenance.setDamageOrMalfunction(deviceBreakdownMaintenance.getDamageOrMalfunction());
                breakdownMaintenance.setApplicantUserId(user.getId());
                breakdownMaintenance.setApplicantUser(user.getName());
                breakdownMaintenance.setRepairDate(deviceBreakdownMaintenance.getRepairDate());

                // 处理人信息
                User departmentHeadUser = userMapper.selectById(deviceBreakdownMaintenance.getDepartmentHeadUserId());
                breakdownMaintenance.setApplicantUserId(departmentHeadUser.getId());
                breakdownMaintenance.setApplicantUser(departmentHeadUser.getName());

                baseMapper.insert(breakdownMaintenance);
                break;
            case 1:
                breakdownMaintenance.setMaintenanceId(deviceBreakdownMaintenance.getMaintenanceId());
                // 申请部门负责人意见
                breakdownMaintenance.setDepartmentHeadOpinion(deviceBreakdownMaintenance.getDepartmentHeadOpinion());
                breakdownMaintenance.setDepartmentHeadDate(LocalDate.now());

                baseMapper.updateById(breakdownMaintenance);
                break;
            case 2:
                breakdownMaintenance.setMaintenanceId(deviceBreakdownMaintenance.getMaintenanceId());
                // 计量室意见
                breakdownMaintenance.setMaintenanceRecord(deviceBreakdownMaintenance.getMaintenanceRecord());
                breakdownMaintenance.setMaintenanceUser(deviceBreakdownMaintenance.getMaintenanceUser());
                breakdownMaintenance.setMaintenanceDate(deviceBreakdownMaintenance.getMaintenanceDate());

                breakdownMaintenance.setIsFinish(1);

                baseMapper.updateById(breakdownMaintenance);
                break;
        }
        return true;
    }

    /**
     * 导出设备故障维修
     * @param maintenanceId 设备故障维修id
     * @param response 响应
     */
    @Override
    public void exportDeviceBreakdownMaintenance(Integer maintenanceId, HttpServletResponse response) {
        // 查询外部设备申请
        DeviceBreakdownMaintenance deviceBreakdownMaintenance = baseMapper.selectById(maintenanceId);

        Device device = null;
        if (deviceBreakdownMaintenance.getDeviceId() != null) {
            device = deivceMapper.selectById(deviceBreakdownMaintenance.getDeviceId());
            device = device == null ? new Device() : device;
        }
        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-breakdown-maintenance.docx");
        Configure configure = Configure.builder()
                .build();
        Device finalDevice = device;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceBreakdownMaintenance", deviceBreakdownMaintenance);
                    put("device", finalDevice);
                    // 申请人签名
                    put("applicantUrl", UserUtils.getFinalUserSignatureUrl(deviceBreakdownMaintenance.getApplicantUserId()));
                    // 部门负责人签名
                    put("headUrl", UserUtils.getFinalUserSignatureUrl(deviceBreakdownMaintenance.getDepartmentHeadUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String deviceName = device.getDeviceName() == null ? "" : device.getDeviceName();
            String fileName = URLEncoder.encode(
                    deviceName + "设备故障维修申请表", "UTF-8");
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

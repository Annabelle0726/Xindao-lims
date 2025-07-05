package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceAccidentReportDto;
import com.ruoyi.device.mapper.DeviceAccidentReportMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceAccidentReport;
import com.ruoyi.device.service.DeviceAccidentReportService;
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
 * 设备事故报告单 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 06:31:12
 */
@Service
public class DeviceAccidentReportServiceImpl extends ServiceImpl<DeviceAccidentReportMapper, DeviceAccidentReport> implements DeviceAccidentReportService {

    @Resource
    private UserMapper userMapper;


    @Resource
    private DeviceMapper deivceMapper;

    /**
     * 设备事故报告列表
     * @param page
     * @param deviceAccidentReport
     * @return
     */
    @Override
    public IPage<DeviceAccidentReport> pageDeviceAccidentReport(Page page, DeviceAccidentReport deviceAccidentReport) {
        return baseMapper.pageDeviceAccidentReport(page, QueryWrappers.queryWrappers(deviceAccidentReport));
    }

    /**
     * 新增设备事故报告
     * @param deviceAccidentReport
     * @return
     */
    @Override
    public boolean addDeviceAccidentReport(DeviceAccidentReport deviceAccidentReport) {
        DeviceAccidentReport accidentReport = new DeviceAccidentReport();
        User user = userMapper.selectById(SecurityUtils.getUserId().intValue());
        switch (deviceAccidentReport.getFlowType()) {
            case 0:
                // 报告
                BeanUtils.copyProperties(deviceAccidentReport, accidentReport);
                accidentReport.setDescriptionOfAccident(deviceAccidentReport.getDescriptionOfAccident());
                accidentReport.setReportUserId(user.getId());
                accidentReport.setReportUser(user.getName());
                accidentReport.setReportDate(LocalDate.now());

                // 评估人信息
                User assessorUser = userMapper.selectById(deviceAccidentReport.getAssessorUserId());
                accidentReport.setAssessorUserId(assessorUser.getId());
                accidentReport.setAssessorUser(assessorUser.getName());

                baseMapper.insert(accidentReport);
                break;
            case 1:
                accidentReport.setAccidentReportId(deviceAccidentReport.getAccidentReportId());
                // 评估
                accidentReport.setAssessorOpinion(deviceAccidentReport.getAssessorOpinion());
                accidentReport.setAssessorDate(LocalDate.now());

                // 部门负责人
                User departmentHeadUser = userMapper.selectById(deviceAccidentReport.getDepartmentHeadUserId());
                accidentReport.setDepartmentHeadUserId(departmentHeadUser.getId());
                accidentReport.setDepartmentHeadUser(departmentHeadUser.getName());

                baseMapper.updateById(accidentReport);

                break;
            case 2:
                accidentReport.setAccidentReportId(deviceAccidentReport.getAccidentReportId());
                // 部门负责人意见
                accidentReport.setDepartmentHeadOpinion(deviceAccidentReport.getDepartmentHeadOpinion());
                accidentReport.setDepartmentHeadDate(LocalDate.now());

                // 技术负责人信息
                User technicalDirectorUser = userMapper.selectById(deviceAccidentReport.getTechnicalDirectorUserId());
                accidentReport.setTechnicalDirectorUserId(technicalDirectorUser.getId());
                accidentReport.setTechnicalDirectorUser(technicalDirectorUser.getName());

                baseMapper.updateById(accidentReport);

                break;
            case 3:
                accidentReport.setAccidentReportId(deviceAccidentReport.getAccidentReportId());
                // 技术负责人意见
                accidentReport.setTechnicalDirectorOpinion(deviceAccidentReport.getTechnicalDirectorOpinion());
                accidentReport.setTechnicalDirectorDate(LocalDate.now());

                // 主任信息
                User directorHeadUser = userMapper.selectById(deviceAccidentReport.getDirectorHeadUserId());
                accidentReport.setDirectorHeadUserId(directorHeadUser.getId());
                accidentReport.setDirectorHeadUser(directorHeadUser.getName());

                baseMapper.updateById(accidentReport);

                break;
            case 4:
                accidentReport.setAccidentReportId(deviceAccidentReport.getAccidentReportId());
                // 主任意见
                accidentReport.setDirectorHeadOpinion(deviceAccidentReport.getDirectorHeadOpinion());
                accidentReport.setDirectorHeadDate(LocalDate.now());

                accidentReport.setIsFinish(1);
                baseMapper.updateById(accidentReport);
                break;
        }
        return true;
    }

    /**
     * 导出设备事故报告
     * @param accidentReportId 设备事故报告id
     * @param response 响应
     */
    @Override
    public void exportDeviceAccidentReport(Integer accidentReportId, HttpServletResponse response) {
        // 查询事故报告
        DeviceAccidentReportDto deviceAccidentReport = baseMapper.selectDeviceAccidentReportById(accidentReportId);

        Device device = deivceMapper.selectById(deviceAccidentReport.getDeviceId());
        device = device == null ? new Device() : device;

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-accident-report.docx");
        Configure configure = Configure.builder()
                .build();
        Device finalDevice = device;
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceAccidentReport", deviceAccidentReport);
                    put("device", finalDevice);
                    // 报告人签名
                    put("reportUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getReportUserId()));
                    // 评估人签名
                    put("assessorUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getAssessorUserId()));
                    // 部门负责人签名
                    put("departmentHeadUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getDepartmentHeadUserId()));
                    // 技术负责人签名
                    put("technicalDirectorUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getTechnicalDirectorUserId()));
                    // 主任签名
                    put("directorHeadUserUrl", UserUtils.getFinalUserSignatureUrl(deviceAccidentReport.getDepartmentHeadUserId()));
                }});

        try {
            response.setContentType("application/msword");
            String deviceName = device.getDeviceName() == null ? "" : device.getDeviceName();
            String fileName = URLEncoder.encode(
                    deviceName + "设备事故报告单", "UTF-8");
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

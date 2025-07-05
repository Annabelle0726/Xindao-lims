package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.basic.mapper.LaboratoryMapper;
import com.ruoyi.basic.pojo.Laboratory;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.device.dto.DeviceStateDto;
import com.ruoyi.device.mapper.DeviceStateMapper;
import com.ruoyi.device.pojo.DeviceState;
import com.ruoyi.device.service.DeviceStateService;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * <p>
 * 设备停用/启用 服务实现类
 * </p>
 *
 * @author
 * @since 2024-09-26 09:51:40
 */
@Service
public class DeviceStateServiceImpl extends ServiceImpl<DeviceStateMapper, DeviceState> implements DeviceStateService {

    @Resource
    private LaboratoryMapper laboratoryMapper;

    @Resource
    private UserMapper userMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Override
    public IPage<DeviceStateDto> getDeviceStatePage(Integer deviceId, Page page, String processNumber) {
        return baseMapper.getDeviceStatePage(deviceId, page, processNumber);
    }

    @Override
    public void exportDeviceStatus(Integer deviceId, String processNumber, HttpServletResponse response) {
        // 根据流程编号 查询cnas设备状态
        DeviceStateDto deviceStateDto = baseMapper.getDeviceStatePage(deviceId,new Page<DeviceStateDto>(1,1), processNumber).getRecords().get(0);

        // 对时间进行修改
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        deviceStateDto.setSubmitDateString(deviceStateDto.getSubmitDate() != null ? deviceStateDto.getSubmitDate().format(format) : "  年 月 日");
        deviceStateDto.setDepartmentDateString(deviceStateDto.getDepartmentDate() != null? deviceStateDto.getDepartmentDate().format(format) : "  年 月 日");
        deviceStateDto.setMeasuringRoomDateString(deviceStateDto.getMeasuringRoomDate() != null? deviceStateDto.getMeasuringRoomDate().format(format) : "  年 月 日");
        deviceStateDto.setApprovalDateString(deviceStateDto.getApprovalDate() != null? deviceStateDto.getApprovalDate().format(format) : "  年 月 日");

        // 查询设备属于哪个实验室
        String laboratoryName;
        String largeCategory = deviceStateDto.getLargeCategory();
        if (StringUtils.isNotBlank(largeCategory)) {
            largeCategory = largeCategory.substring(0, 1);
            Laboratory laboratory = laboratoryMapper.selectOne(Wrappers.<Laboratory>lambdaQuery()
                    .eq(Laboratory::getLaboratoryNumber, largeCategory)
                    .select(Laboratory::getLaboratoryName));
            laboratoryName = laboratory.getLaboratoryName();
        } else {
            laboratoryName = "";
        }

        //todo: 设备状态查询签名地址 暂时人名查询
        //获取申请人的签名地址
        String applicantUrl = null;
        if (deviceStateDto.getSubmitOperatingPersonnel() != null) {
            applicantUrl = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                            .eq(User::getName, deviceStateDto.getSubmitOperatingPersonnel()))
                    .getSignatureUrl();
            if (StringUtils.isBlank(applicantUrl)) {
                throw new ErrorException("找不到申请人的签名");
            }
        }

        //获取部门负责人的签名地址
        String headOfDepartmentUrl = null;
        if (deviceStateDto.getDepartmentNextPesponsible() != null) {
            headOfDepartmentUrl = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                    .eq(User::getName, deviceStateDto.getDepartmentNextPesponsible()))
                    .getSignatureUrl();
            if (StringUtils.isBlank(headOfDepartmentUrl)) {
                throw new ErrorException("找不到部门负责人的签名");
            }
        }

        //获取计量室复测人的签名地址
        String measurementRoomUrl = null;
        if (deviceStateDto.getMeasuringRoomNextPesponsible() != null) {
            measurementRoomUrl = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                            .eq(User::getName, deviceStateDto.getMeasuringRoomNextPesponsible()))
                    .getSignatureUrl();
            if (StringUtils.isBlank(measurementRoomUrl)) {
                throw new ErrorException("找不到计量室复测人的签名");
            }
        }

        //获取批准人的签名地址
        String approvedUrl = null;
        if (deviceStateDto.getApprovalNextPesponsible() != null) {
            approvedUrl = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                            .eq(User::getName, deviceStateDto.getApprovalNextPesponsible()))
                    .getSignatureUrl();
            if (StringUtils.isBlank(approvedUrl)) {
                throw new ErrorException("找不到批准人的签名");
            }
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-status.docx");
        Configure configure = Configure.builder()
                .build();
        String finalApplicantUrl = applicantUrl; // 申请人的签名地址
        String finalHeadOfDepartmentUrl = headOfDepartmentUrl; // 部门负责人的签名地址
        String finalMeasurementRoomUrl = measurementRoomUrl; // 计量室复测人的签名地址
        String finalApprovedUrl = approvedUrl; // 批准人的签名地址
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceStateDto", deviceStateDto);
                    put("submitOperatingPersonnelUrl", StringUtils.isNotBlank(finalApplicantUrl) ? Pictures.ofLocal(imgUrl + "/" + finalApplicantUrl).create() : null);
                    put("departmentNextPesponsibleUrl", StringUtils.isNotBlank(finalHeadOfDepartmentUrl) ? Pictures.ofLocal(imgUrl + "/" + finalHeadOfDepartmentUrl).create() : null);
                    put("measuringRoomNextPesponsibleUrl", StringUtils.isNotBlank(finalMeasurementRoomUrl) ? Pictures.ofLocal(imgUrl + "/" + finalMeasurementRoomUrl).create() : null);
                    put("approvalNextPesponsibleUrl", StringUtils.isNotBlank(finalApprovedUrl) ? Pictures.ofLocal(imgUrl + "/" + finalApprovedUrl).create() : null);
                    put("laboratory", laboratoryName);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "设备维护保养记录", "UTF-8");
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

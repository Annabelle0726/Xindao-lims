package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.device.mapper.DeviceAcceptanceFileMapper;
import com.ruoyi.device.mapper.DeviceAcceptanceMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceAcceptance;
import com.ruoyi.device.pojo.DeviceAcceptanceFile;
import com.ruoyi.device.service.DeviceAcceptanceService;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * <p>
 * 设备验收(装备) 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-20 01:45:14
 */
@Service
public class DeviceAcceptanceServiceImpl extends ServiceImpl<DeviceAcceptanceMapper, DeviceAcceptance> implements DeviceAcceptanceService {

    @Resource
    private DeviceAcceptanceFileMapper deviceAcceptanceFileMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Value("${file.path}")
    private String imgUrl;

    @Value("${wordUrl}")
    private String wordUrl;

    /**
     * 设备验收列表
     * @param page
     * @param deviceAcceptance
     * @return
     */
    @Override
    public IPage<DeviceAcceptance> pageDeviceAcceptance(Page page, DeviceAcceptance deviceAcceptance) {
        return baseMapper.pageDeviceAcceptance(page, QueryWrappers.queryWrappers(deviceAcceptance));
    }

    /**
     * 设备验收附件
     * @param acceptanceId
     * @param file
     * @return
     */
    @Override
    public boolean uploadDeviceAcceptanceFile(Integer acceptanceId, MultipartFile file) {
        if (acceptanceId == null) {
            throw new ErrorException("缺少验收id");
        }

        String urlString;
        String pathName;
        String path;
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        DeviceAcceptanceFile acceptanceFile = new DeviceAcceptanceFile();
        acceptanceFile.setAcceptanceId(acceptanceId);
        acceptanceFile.setFileName(filename);
        if (contentType != null && contentType.startsWith("image/")) {
            // 是图片
            path = imgUrl;
            acceptanceFile.setType(1);
        } else {
            // 是文件
            path = wordUrl;
            acceptanceFile.setType(2);
        }
        try {
            File realpath = new File(path);
            if (!realpath.exists()) {
                realpath.mkdirs();
            }
            pathName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + "_" + file.getOriginalFilename();
            urlString = realpath + "/" + pathName;
            file.transferTo(new File(urlString));
            acceptanceFile.setFileUrl(pathName);
            deviceAcceptanceFileMapper.insert(acceptanceFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("附件上传错误");
            return false;
        }
    }

    /**
     * 设备验收导出
     * @param acceptanceId  设备验收id
     * @param response   响应体
     * @return
     */
    @Override
    public void exportDeviceAcceptance(Integer acceptanceId, HttpServletResponse response) {
        DeviceAcceptance deviceAcceptance = baseMapper.selectById(acceptanceId);
        if (deviceAcceptance == null) {
            throw new ErrorException("设备验收不存在");
        }
        Device device = null;
        if (deviceAcceptance.getDeviceId() != null) {
            device = deviceMapper.selectById(deviceAcceptance.getDeviceId());
        }


        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/acceptance-certificate.docx");
        Configure configure = Configure.builder()
                .bind("deviceInspectionRecordDetailsList", new HackLoopTableRenderPolicy())
                .build();
        Device finalDevice = device;
        String deviceName = device.getDeviceName() == null ? "" : device.getDeviceName();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceAcceptance", deviceAcceptance);
                    put("device", finalDevice);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    deviceName+ "验收单", "UTF-8");
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

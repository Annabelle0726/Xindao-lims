package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.device.dto.DeviceRecordDto;
import com.ruoyi.device.excel.DeviceRecordExportWord;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.mapper.DeviceRecordMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceRecord;
import com.ruoyi.device.service.DeviceRecordService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.system.domain.vo.SysRoleVo;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * cnas设备使用记录表 服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 11:06:47
 */
@Service
public class DeviceRecordServiceImpl extends ServiceImpl<DeviceRecordMapper, DeviceRecord> implements DeviceRecordService {
    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public IPage<DeviceRecordDto> deviceRecordPage(Integer deviceId, Page page, String sampleCode, String managementNumber) {
        // todo:仅看自己
        Long userId = SecurityUtils.getUserId();
        // 获取权限判断是否是仅看我
        SysRoleVo sysRoleVo = sysRoleMenuMapper.selectRoleMenu("get:device:record", userId);

        Integer testUserId = null;
        //没有仅看我权限跳过
        if (sysRoleVo != null && sysRoleVo.getIsRersonal() != null && sysRoleVo.getIsRersonal().equals(1)) {
            testUserId = SecurityUtils.getUserId().intValue();
        }

        return baseMapper.deviceRecordPage(deviceId, page, sampleCode, managementNumber, testUserId);
    }


    @Override
    public void exportUseRecord(Integer deviceId, String exportDate, HttpServletResponse response) {
        // 查询cnas设备使用记录
        List<DeviceRecord> deviceList = baseMapper.selectExportList(deviceId, exportDate);
        // 设备信息
        Device device = deviceMapper.selectById(deviceId);


        // 查询设备属于哪个实验室
        String laboratoryName = "装备线缆实验室";

        // 要映射到word数据
        List<DeviceRecordExportWord> deviceExportList = new ArrayList<>();
        // deviceExportList 赋值
        for (DeviceRecord deviceRecord : deviceList) {
            // 处理设备 开始使用时间 和 结束时间
            String startTime = "";
            String endTime = "";
            String operationDate = "";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if (deviceRecord.getUseStartDate() != null) {
                startTime = deviceRecord.getUseStartDate().format(formatter);
                endTime = deviceRecord.getUseEndDate().format(formatter);
                String[] startTimeSplit = startTime.split(" ");
                String[] endTimeSplit = endTime.split(" ");
                if (startTimeSplit[0].equals(endTimeSplit[0])) {
                    startTime = startTimeSplit[1];
                    endTime = endTimeSplit[1];
                } else {
                    endTime = endTimeSplit[0] + '\n' + endTimeSplit[1];
                    startTime = startTimeSplit[0] + '\n' + startTimeSplit[1];
                }
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy.M.d");
                operationDate = deviceRecord.getUseStartDate().format(formatter2);
            }
            // 处理使用前和使用后
            String useBeforeString = deviceRecord.getUseBefore() == 0? "异常" : "良好";
            String useAfterString = deviceRecord.getUseAfter() == 0 ? "异常" : "良好";

            DeviceRecordExportWord deviceRecordExportWord = new DeviceRecordExportWord();
            // 进行赋值
            BeanUtils.copyProperties(deviceRecord, deviceRecordExportWord);
            deviceRecordExportWord.setUseBeforeString(useBeforeString); // 使用前
            deviceRecordExportWord.setUseAfterString(useAfterString); // 使用后
            deviceRecordExportWord.setOperationDate(operationDate); // 设备操作日期
            deviceRecordExportWord.setUseStartDateString(startTime); // 开始时间
            deviceRecordExportWord.setUseEndDateString(endTime); // 结束时间
            deviceExportList.add(deviceRecordExportWord);
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/use-record.docx");
        Configure configure = Configure.builder()
                .bind("useRecord", new HackLoopTableRenderPolicy())
                .build();
        String deviceName = device.getDeviceName();
        String managementNumber = device.getManagementNumber();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("useRecord", deviceExportList);
                    put("deviceName", deviceName);
                    put("managementNumber", managementNumber);
                    put("laboratory", laboratoryName);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "仪器使用记录表", "UTF-8");
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

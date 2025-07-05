package com.ruoyi.device.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.device.dto.DeviceImpowerDetailsDto;
import com.ruoyi.device.dto.DeviceImpowerDto;
import com.ruoyi.device.dto.DeviceMaintenanceDto;
import com.ruoyi.device.mapper.DeviceMaintenanceMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.pojo.DeviceImpower;
import com.ruoyi.device.pojo.DeviceMaintenance;
import com.ruoyi.device.service.DeviceMaintenanceService;
import com.ruoyi.device.service.DeviceService;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.inspect.util.UserUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * 设备维护保养
 */
@Service
public class DeviceMaintenanceImpl extends ServiceImpl<DeviceMaintenanceMapper, DeviceMaintenance> implements DeviceMaintenanceService {
    @Resource
    private DeviceService deviceService;
    @Resource
    private ISysDictTypeService iSysDictTypeService;

    @Value("${file.path}")
    private String imgUrl;

    /**
     * 设备维护分页查询
     * @return
     */
    @Override
    public IPage<DeviceMaintenanceDto> selectDeviceMaintenancePage(Page page, DeviceMaintenanceDto deviceMaintenance) {
        if (deviceMaintenance.getDeviceId() == null) {
            return new Page();
        }
        return baseMapper.selectDeviceMaintenancePage(page, QueryWrappers.queryWrappers(deviceMaintenance));
    }

    /**
     * 导出设备维护保养
     * @param deviceId
     * @param response
     */
    @Override
    public void exportDeviceMaintenance(Integer deviceId, HttpServletResponse response) {
        // 查询设备维护保养
        List<DeviceMaintenance> deviceMaintenanceList = baseMapper.selectList(Wrappers.<DeviceMaintenance>lambdaQuery()
                .eq(DeviceMaintenance::getDeviceId, deviceId));

        // 获取到第一个设备使用授权
        DeviceMaintenanceDto deviceMaintenanceDto = new DeviceMaintenanceDto();

        List<SysDictData> sysDictDataList = null;
        if (CollectionUtils.isNotEmpty(deviceMaintenanceList)) {
            BeanUtils.copyProperties(deviceMaintenanceList.get(0), deviceMaintenanceDto);
            // 查询设备信息
            Device device = deviceService.getById(deviceId);
            deviceMaintenanceDto.setDeviceName(device.getDeviceName());
            deviceMaintenanceDto.setManagementNumber(device.getManagementNumber());

            //查询维护保养导出字典值
            sysDictDataList = getDeviceMaintenanceDict(device);
        }

        // 添加字典枚举值
        for (DeviceMaintenance deviceMaintenance : deviceMaintenanceList) {
            StringBuilder maintenanceContent = new StringBuilder(deviceMaintenance.getMaintenanceContent());
            if (CollectionUtils.isNotEmpty(sysDictDataList) && StringUtils.isNotBlank(deviceMaintenance.getMaintenanceContent())) {
                maintenanceContent = new StringBuilder();
                List<String> splitList = StrUtil.split(deviceMaintenance.getMaintenanceContent(), ',');
                // 循环判断是否有一样key
                for (SysDictData sysDictData : sysDictDataList) {
                    if (splitList.contains(sysDictData.getDictLabel())) {
                        maintenanceContent.append("☑").append(sysDictData.getDictLabel()).append(" ");
                    } else {
                        maintenanceContent.append("□").append(sysDictData.getDictLabel()).append(" ");
                    }
                }
            }
            deviceMaintenance.setMaintenanceContent(maintenanceContent.toString());
            // 添加维护人图片
            deviceMaintenance.setMaintenanceUserUrlRender(UserUtils.getFinalUserSignatureUrl(deviceMaintenance.getMaintenanceUserId()));

        }


        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-maintenance.docx");
        Configure configure = Configure.builder()
                .bind("deviceMaintenances", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("device", deviceMaintenanceDto);
                    put("deviceMaintenances", deviceMaintenanceList);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "设备保养维护", "UTF-8");
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

    /**
     * ****查询维护保养导出字典值***
     * @param device
     */
    private List<SysDictData> getDeviceMaintenanceDict(Device device) {
        List<SysDictData> sysDictDataList = new ArrayList<>();
        // 查询维护保养导出字典值
        if (StringUtils.isNotBlank(device.getStoragePoint())) {
            String dictType = null;
            switch (device.getStoragePoint()) {
                case "老化室":
                    dictType = "aging_maintenance_content";
                    break;
                case "老化实验室":
                    dictType = "aging_maintenance_content";
                    break;
                case "电性能实验室":
                    dictType = "electrical_maintenance_content";
                    break;
                case "环境实验室":
                    dictType = "environmental_maintenance_content";
                    break;
                case "燃烧实验室":
                    dictType = "burn_maintenance_content";
                    break;
                case "混炼实验室":
                    dictType = "mixing_maintenance_content";
                    break;
                case "检测中心电性能实验室":
                    dictType = "center_electrical_maintenance_content";
                    break;
                case "恒温二":
                    dictType = "temperature2_maintenance_content";
                    break;
                case "恒温一":
                    dictType = "temperature1_maintenance_content";
                    break;
                case "化学实验室":
                    dictType = "chemistry_maintenance_content";
                    break;
                case "制样室":
                    dictType = "sample_maintenance_content";
                    break;
                case "低温实验室":
                    dictType = "hypothermia_maintenance_content";
                    break;
            }
            sysDictDataList = iSysDictTypeService.selectDictDataByType(dictType);
        }
        return sysDictDataList;
    }
}

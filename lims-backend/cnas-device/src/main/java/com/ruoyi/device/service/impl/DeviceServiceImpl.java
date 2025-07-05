package com.ruoyi.device.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.ruoyi.basic.mapper.StructureItemParameterMapper;
import com.ruoyi.basic.pojo.StructureItemParameter;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.common.utils.QueryWrappers;
import com.ruoyi.device.dto.*;
import com.ruoyi.device.mapper.CollectBridgeMapper;
import com.ruoyi.device.mapper.DeviceMaintenanceMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.mapper.DeviceMetricRecordMapper;
import com.ruoyi.device.pojo.*;
import com.ruoyi.device.service.DataConfigService;
import com.ruoyi.device.service.DeviceService;
import com.ruoyi.device.service.DeviceDocumentsService;
import com.ruoyi.device.utils.DataAcquisition;
import com.ruoyi.framework.exception.ErrorException;
import com.ruoyi.inspect.mapper.InsSampleMapper;
import com.ruoyi.inspect.pojo.InsProduct;
import com.ruoyi.inspect.util.HackLoopTableRenderPolicy;
import com.ruoyi.system.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备(Device)表服务实现类
 */
@Service
@AllArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    private DeviceMapper deviceMapper;

    private UserMapper userMapper;

    private StructureItemParameterMapper structureItemParameterMapper;

    private DataConfigService dataConfigService;

    private InsSampleMapper insSampleMapper;

    private DeviceDocumentsService documentService;

    private DeviceMetricRecordMapper deviceMetricRecordMapper;

    private DeviceMaintenanceMapper deviceMaintenanceMapper;

    private CollectBridgeMapper collectBridgeMapper;

    @Override
    public IPage<DeviceDto> selectDeviceParameter(Page page, DeviceDto itemParameter, Boolean laboratoryNameIsNull) {
        IPage<DeviceDto> iPage = deviceMapper.selectDeviceParameterPage(page, QueryWrappers.queryWrappers(itemParameter), laboratoryNameIsNull);
        return iPage;
    }

    @Override
    public int addDeviceParameter(Device itemParameter) {
        return deviceMapper.insert(itemParameter);
    }

    @Override
    public int delDeviceParameter(Integer id) {
        return deviceMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int upDeviceParameter(Device itemParameter) {

        return deviceMapper.updateById(itemParameter);
    }

    @Override
    public List<Device> selectEquipmentOverview() {
        return deviceMapper.selectEquipmentOverview(new Page(1, 10), QueryWrappers.queryWrappers(new Device()));
    }

    @Override
    public List<Device> authorizedPerson() {
        return deviceMapper.authorizedPerson();
    }

    @Override
    public List<Device> search(Integer status, String deviceName, String specificationModel, String largeCategory) {
        return deviceMapper.search(status, deviceName, specificationModel, largeCategory);
    }

    @Override
    public List<Device> selectDeviceByCategory(String inspectionItem, String inspectionItemSubclass, String laboratory) {
        List<Integer> id;

        try {
            id = structureItemParameterMapper.selectList(Wrappers.<StructureItemParameter>lambdaQuery()
                    .eq(StructureItemParameter::getInspectionItem, inspectionItem)
                    .eq(ObjectUtils.isNotEmpty(inspectionItemSubclass), StructureItemParameter::getInspectionItemSubclass, inspectionItemSubclass)
                    .eq(ObjectUtils.isNotEmpty(laboratory), StructureItemParameter::getLaboratory, laboratory)
                    .select(StructureItemParameter::getId)).stream().map(StructureItemParameter::getId).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
        List<Device> devices = deviceMapper.selectList(Wrappers.<Device>lambdaQuery()
                .eq(Device::getDeviceStatus, 0)
                .isNotNull(Device::getInsProductIds));
        List<Device> devices2 = new ArrayList<>();
        for (Device device : devices) {
            String[] ids = device.getInsProductIds().split(",");
            for (String i : ids) {
                if (ObjectUtils.isNotEmpty(i)) {
                    if (id.contains(Integer.parseInt(i))) {
                        devices2.add(device);
                        break;
                    }
                }
            }
        }
        return devices2;
    }

    @Override
    public DeviceDto selectDeviceByCode(Integer id) {
        DeviceDto deviceDto = deviceMapper.selectDeviceByCode(id);
        List<Integer> ids = new ArrayList<>();
        if (Strings.isNotEmpty(deviceDto.getAuthorizedPerson())) {
            if (deviceDto.getAuthorizedPerson().equals("null")) {
                deviceDto.setAuthorizedPerson("[]");
            }
            ids = JSON.parseArray(deviceDto.getAuthorizedPerson(), Integer.class);
        }
        String name = "";
        if (!ids.isEmpty()) {
            name = userMapper.selectBatchIds(ids).stream().map(User::getName).collect(Collectors.joining(","));
        }
        deviceDto.setAuthorizedPersonName(name);
        //查询设备校准信息
        DeviceMetricRecord calibrate = getDeviceMetricRecord(id, "calibrate");
        deviceDto.setCalibrateNo(calibrate.getCertificateSerialNumber());

        // 到了停用日期，自动将状态改为停用
        if (deviceDto.getNextCalibrationDate() != null) {
            if (LocalDateTime.now().isAfter(deviceDto.getNextCalibrationDate())) {
                // todo: 设备运行状态字典
//                List<Enums> enums = enumService.selectEnumByCategory("设备状态");
//                List<Enums> status = enums.stream().filter(item -> item.getLabel().equals("停用")).collect(Collectors.toList());
//                deviceDto.setDeviceStatus(Integer.parseInt(status.get(0).getValue()));
                deviceMapper.updateById(deviceDto);
            }
        }
        return deviceDto;
    }

    /**
     * 查询设备校准/核查记录
     * @param deviceId
     * @param type
     * @return
     */
    public DeviceMetricRecord getDeviceMetricRecord(int deviceId, String type){
        return Optional.ofNullable(
                deviceMetricRecordMapper.selectOne(Wrappers.<DeviceMetricRecord>lambdaQuery()
                        .eq(DeviceMetricRecord::getDeviceId, deviceId)
                        .eq(DeviceMetricRecord::getType, type)
                        .orderByDesc(DeviceMetricRecord::getCreateTime)
                        .last("limit 1"))).orElse(new DeviceMetricRecord());
    }

    @Override
    public Result<?> dataAcquisition(HttpServletRequest request, DeviceCollectionDto dto) {

        // 查询检验项
        List<Integer> itemIds = dto.getItemIds();
        if (CollectionUtils.isEmpty(itemIds)) {
            throw new ErrorException("没有需要数采的检验项");
        }
        List<InsProduct> insProducts = insSampleMapper.selectProductResult(itemIds);

        // 查询检验项绑定的id
        List<Integer> itemParameterIds = insProducts.stream().map(InsProduct::getStructureItemParameterId).collect(Collectors.toList());

        Set<String> deviceCodeSet = new LinkedHashSet<>();
        for (InsProduct product : insProducts) {
            // 查询设备
            // 添加设备编号
            if (product.getInsProductResult() != null) {
                List<JSONObject> jsonObjects = JSON.parseArray(product.getInsProductResult().getEquipValue(), JSONObject.class);
                for (JSONObject jsonObject : jsonObjects) {
                    if (!"".equals(jsonObject.get("v") + "")) {
                        List<String> v = StrUtil.split(jsonObject.get("v") + "", "，");
                        deviceCodeSet.addAll(v);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(deviceCodeSet)) {
            throw new ErrorException("未选择设备信息");
        }
        // 获取设备集合
        List<Device> deviceList = baseMapper.selectList(Wrappers.<Device>lambdaQuery()
                .in(Device::getManagementNumber, deviceCodeSet)
                .isNotNull(Device::getIp)
                .ne(Device::getIp, ""));

        if (CollectionUtils.isEmpty(deviceList)) {
            throw new ErrorException("无设备配置采集绑定信息");
        }

        // 数采返回信息
        Map<String, Object> map = new HashMap<>();
        for (Device device : deviceList) {
            String ip = device.getIp();
            // 根据检验项获取config
            List<DataConfig> list = dataConfigService.list(Wrappers.<DataConfig>lambdaQuery()
                    .in(DataConfig::getStructureItemParameterId, itemParameterIds)
                    .eq(DataConfig::getDeviceId, device.getId()));

            // 获取设备配置类
            // 判断设备是否是数字直桥
            if (device.getManagementNumber().equals("JCZX-ZB-ER02022")) {
                map.putAll(dataCollectBridge(list, device, dto.getEntrustCode()));
            } else {
                map.putAll(DataAcquisition.dataAcquisitionEntrance(list, device, dto.getEntrustCode(), dto.getEntrustCode(), ip, insProducts.get(0).getCableTag()));
            }
        }


        // 4、造循环次数，参与公式计算
        if (ObjectUtils.isNotEmpty(map)) {
            Map<String, Object> frequency = DataAcquisition.createFrequency(dto.getEntrustCode(), dto.getEntrustCode(), map);
            return Result.success(frequency);
        } else {
            return Result.success(null);
        }
    }

    /**
     * 获取数字直桥检测信息
     * @return
     */
    public Map<String, ?> dataCollectBridge(List<DataConfig> dataConfig, Device device, String entrustCode) {
        // 拼接名字
        Map<String, List<DataConfig>> userMap = dataConfig.stream()
                .peek(i -> {
                    String itemName = i.getInspectionItem();
                    if (StringUtils.isNotBlank(i.getInspectionItemClass())) {
                        itemName += "@" + i.getInspectionItemClass();
                    }
                    String name = i.getInspectionItem().equals(i.getInspectionItemSubclass()) ? itemName + "," : itemName + "," + i.getInspectionItemSubclass();

                    // 添加检验项名称
                    i.setInsProductItem(name);
                })
                .collect(Collectors.groupingBy(DataConfig::getInsProductItem));

        Map<String, Object> map = new HashMap<>();

        userMap.forEach((k, v) -> {
            List<String> resultValue = new ArrayList<>();
            // 查询直桥电流电阻数采值
            List<CollectBridge> collectBridges = collectBridgeMapper.selectList(Wrappers.<CollectBridge>lambdaQuery()
                    .like(CollectBridge::getEntrustCode, entrustCode)
                    .orderByAsc(CollectBridge::getCollectDate));

            resultValue = collectBridges.stream().map(CollectBridge::getCollectValue).collect(Collectors.toList());

            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("equipName", device.getDeviceName());
            hashMap.put("equipValue", device.getManagementNumber());
            hashMap.put("result", resultValue);
            map.put(k, hashMap);
        });
        return map;
    }


    @Override
    public List<Map<String, Object>> treeDevice(String deviceName) {
        List<Map<String, Object>> listMap = deviceMapper.treeDevice(deviceName);
        return listMap;
    }



    @Override
    public void exportDeviceFile(Integer deviceId, HttpServletResponse response) {

        // 设备信息
        Device device = baseMapper.selectById(deviceId);
        // 设备档案
        List<DeviceDocuments> documentList = documentService.list(Wrappers.<DeviceDocuments>lambdaQuery().eq(DeviceDocuments::getDeviceId, deviceId));
        // 设备校准表
        List<DeviceMetricRecord> deviceMetricRecordList = deviceMetricRecordMapper.selectList(Wrappers.<DeviceMetricRecord>lambdaQuery().eq(DeviceMetricRecord::getDeviceId, deviceId));
        // 设备维修表
        List<DeviceMaintenance> deviceMaintenanceList = deviceMaintenanceMapper.selectList(Wrappers.<DeviceMaintenance>lambdaQuery().eq(DeviceMaintenance::getDeviceId, deviceId));


        // 返回给word的数据列表 分为左右两列数据
        List<DocumentExportWordDto> documentExportWordDtoList = new ArrayList<>();
        // 给档案加序号 并左右分为左右两列在word中显示
        extracted(documentList, documentExportWordDtoList);


        // 将校准表和维修表放入一个对象中方便word表格中显示
        List<DeviceMetricRecordAndMaintenanceDto> deviceMetricRecordAndMaintenanceDtoList = getDeviceMetricRecordAndMaintenanceDtoList(deviceMetricRecordList, deviceMaintenanceList);


        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/device-document.docx");
        Configure configure = Configure.builder()
                .bind("document", new HackLoopTableRenderPolicy())
                .bind("deviceMetricRecordAndMaintenanceDtoList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("device", device);
                    put("document", documentExportWordDtoList); // 档案
                    put("deviceMetricRecordAndMaintenanceDtoList", deviceMetricRecordAndMaintenanceDtoList); // 校准表 和 维修表
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    device.getDeviceName() + "档案", "UTF-8");
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

    private List<DeviceMetricRecordAndMaintenanceDto> getDeviceMetricRecordAndMaintenanceDtoList(List<DeviceMetricRecord> deviceMetricRecordList, List<DeviceMaintenance> deviceMaintenanceList) {
        // 设备校准表和设备维修表的集合
        List<DeviceMetricRecordAndMaintenanceDto> deviceMetricRecordAndMaintenanceDtoList = new ArrayList<>();
        // 设备校准表和设备维修表的长度可能不一样 取最大值 不够的用空数据填充
        int metricRecordSize = deviceMetricRecordList.size();
        int maintenanceSize = deviceMaintenanceList.size();
        int size = Math.max(metricRecordSize, maintenanceSize);
        // 给 校验和维修对象 赋值
        for (int i = 0; i < size; i++) {
            // 校验和维修对象
            DeviceMetricRecordAndMaintenanceDto deviceMetricRecordAndMaintenanceDto = new DeviceMetricRecordAndMaintenanceDto();
            // 设置序号
            deviceMetricRecordAndMaintenanceDto.setIndex(i + 1);

            // 校准表数据
            if (metricRecordSize > i) {
                // 设置日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 获取设备校准表数据
                DeviceMetricRecord deviceMetricRecord = deviceMetricRecordList.get(i);
                // 设置校准日期
                deviceMetricRecordAndMaintenanceDto.setCalibrationDateString(sdf.format(deviceMetricRecord.getCalibrationDate()));
                // 设置证书编号
                deviceMetricRecordAndMaintenanceDto.setCertificateNumber(deviceMetricRecord.getCertificateSerialNumber());
                // 设置有效期
                deviceMetricRecordAndMaintenanceDto.setValidityDateString(sdf.format(deviceMetricRecord.getNextCalibrationDate()));
                // 设置校准有效日期
                deviceMetricRecordAndMaintenanceDto.setValidityDateString(sdf.format(deviceMetricRecord.getConfirmDate()));
                // 设置检验结果
                deviceMetricRecordAndMaintenanceDto.setJudgement(deviceMetricRecord.getStatus());
            }

            // 维修表数据
            if (maintenanceSize > i) {
                // 获取设备维修表数据
                DeviceMaintenance deviceMaintenance = deviceMaintenanceList.get(i);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                // 维修日期
                deviceMetricRecordAndMaintenanceDto.setMaintenanceDateString(deviceMaintenance.getMaintenanceDate().format(dateTimeFormatter));
                // 处理方法
                deviceMetricRecordAndMaintenanceDto.setHandlingMethod(deviceMaintenance.getMaintenanceContent());
                // 备注
                deviceMetricRecordAndMaintenanceDto.setComments(deviceMaintenance.getRemark());
            }

            deviceMetricRecordAndMaintenanceDtoList.add(deviceMetricRecordAndMaintenanceDto);
        }
        return deviceMetricRecordAndMaintenanceDtoList;
    }

    /**
     * 给档案加序号 并左右分为左右两列在word中显示
     *
     * @param documentList              档案列表
     * @param documentExportWordDtoList 返回给word的数据列表
     */
    private static void extracted(List<DeviceDocuments> documentList, List<DocumentExportWordDto> documentExportWordDtoList) {
        // 给档案加序号   并且分为左右两个列表在word中显示
        for (int i = 0; i < documentList.size(); i++) {
            // 创建word表格中一行的数据对象
            DocumentExportWordDto documentExportWordDto = new DocumentExportWordDto();
            // 获取档案信息
            DeviceDocuments document = documentList.get(i);
            // 格式化日期
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 根据序号 分别加入两个列表
            if (i % 2 == 0) {
                // 奇数在左列
                documentExportWordDto.setIndex1(i + 1);
                documentExportWordDto.setName1(document.getName());
                documentExportWordDto.setQuantity1(document.getQuantity());
                documentExportWordDto.setPageCount1(document.getPageCount());
                documentExportWordDto.setArchiveDateString1(document.getProvideDate().format(dateTimeFormatter));
            } else {
                // 偶数在右列
                documentExportWordDto.setIndex2(i + 1);
                documentExportWordDto.setName2(document.getName());
                documentExportWordDto.setQuantity2(document.getQuantity());
                documentExportWordDto.setPageCount2(document.getPageCount());
                documentExportWordDto.setArchiveDateString2(document.getProvideDate().format(dateTimeFormatter));
            }
            // 把一行数据对象加入列表
            documentExportWordDtoList.add(documentExportWordDto);
        }
    }

    @Override
    public void exportEquipmentDetails(HttpServletResponse response) {
        List<Device> deviceList = baseMapper.selectList(null);
        List<DeviceExport> deviceExportList = new ArrayList<>();

        int index = 1;
        for (Device device : deviceList) {
            Integer equipmentManager = device.getEquipmentManager();
            String equipmentManagerName = null;
            if (equipmentManager != null) {
                User user = userMapper.selectById(equipmentManager);
                if (user != null) {
                    equipmentManagerName = user.getName();
                }
            }
            DeviceExport deviceExport = new DeviceExport();
            BeanUtils.copyProperties(device, deviceExport);
            deviceExport.setIndex(index);
            deviceExport.setEquipmentManagerName(equipmentManagerName);
            deviceExportList.add(deviceExport);
            index++;
        }

        // 获取路径
        InputStream inputStream = this.getClass().getResourceAsStream("/static/word/quipment-details.docx");
        Configure configure = Configure.builder()
                .bind("deviceList", new HackLoopTableRenderPolicy())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(inputStream, configure).render(
                new HashMap<String, Object>() {{
                    put("deviceList", deviceExportList);
                }});

        try {
            response.setContentType("application/msword");
            String fileName = URLEncoder.encode(
                    "仪器设备一览表", "UTF-8");
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

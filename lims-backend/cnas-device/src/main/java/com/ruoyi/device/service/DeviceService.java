package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.entity.User;
import com.ruoyi.device.dto.DeviceCollectionDto;
import com.ruoyi.device.dto.DeviceDto;
import com.ruoyi.device.pojo.Device;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 设备(Device)表服务接口
 */
public interface DeviceService extends IService<Device> {


    IPage<DeviceDto> selectDeviceParameter(Page page, DeviceDto itemParameter, Boolean laboratoryNameIsNull);

    int addDeviceParameter(Device itemParameter);

    int delDeviceParameter(Integer id);

    int upDeviceParameter(Device itemParameter);

    List<Device> selectEquipmentOverview();

    List<Device> authorizedPerson();

    List<Device> search(Integer status, String deviceName, String specificationModel, String largeCategory);

    List<Device> selectDeviceByCategory(String inspectionItem, String inspectionItemSubclass,String laboratory);

    DeviceDto selectDeviceByCode(Integer id);

    Result<?> dataAcquisition(HttpServletRequest request, DeviceCollectionDto dto);

    List<Map<String, Object>> treeDevice(String deviceName);

    /**
     * 导出设备列表
     * @param deviceId
     * @param response
     */
    void exportDeviceFile(Integer deviceId, HttpServletResponse response);

    /**
     * 导出设备工具明细
     * @param response
     */
    void exportEquipmentDetails(HttpServletResponse response);
}

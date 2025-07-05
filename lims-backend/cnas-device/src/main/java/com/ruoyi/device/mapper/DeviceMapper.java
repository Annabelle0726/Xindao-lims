package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceDto;
import com.ruoyi.device.pojo.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备(Device)表数据库访问层
 */
public interface DeviceMapper extends BaseMapper<Device> {

    IPage<Device> selectDeviceParameter(Page page, QueryWrapper<Device> ew);
    List<Device> selectEquipmentOverview(Page page, QueryWrapper<Device> ew);

    //获取被授权人
    List<Device> authorizedPerson();

    //查询
    List<Device> search(@Param(value = "status") Integer status, @Param(value = "deviceName") String deviceName,
                        @Param(value = "specificationModel") String specificationModel, @Param(value = "largeCategory") String largeCategory);

    //获取图片数据
    void selectDeviceImage(@Param(value = "name") String name ,@Param(value = "id") Integer id);

    IPage<DeviceDto> selectDeviceParameterPage(Page page, @Param("ew") QueryWrapper<DeviceDto> queryWrappers, @Param("laboratoryNameIsNull") Boolean laboratoryNameIsNull);

    List<Map<String, Object>> getInspectionItemSubclass(@Param("id") Integer id);

    List<Map<String, Object>> treeDevice(@Param("deviceName") String deviceName);

    DeviceDto selectDeviceByCode(Integer id);

    /**
     * 查询到达校准有效期的设备-提前5天
     * @return
     */
    List<Device> selectOverdueDevice();
}


package com.ruoyi.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DataConfigDto;
import com.ruoyi.device.mapper.DataConfigMapper;
import com.ruoyi.device.mapper.DeviceMapper;
import com.ruoyi.device.pojo.DataConfig;
import com.ruoyi.device.pojo.Device;
import com.ruoyi.device.service.DataConfigService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-13 12:23:00
 */
@Service
@AllArgsConstructor
public class DataConfigServiceImpl extends ServiceImpl<DataConfigMapper, DataConfig> implements DataConfigService {

    private DeviceMapper deviceMapper;

    private DataConfigMapper dataConfigMapper;

    /**
     * 维护设备文件配置
     * @param dataConfigList
     */
    @Override
    public void saveDeviceFileConfiguration(DataConfigDto dataConfigList) {
        if (dataConfigList.getDeviceId() == null) {
            throw new RuntimeException("缺少设备Id");
        }

        Device device = new Device();
        BeanUtils.copyProperties(dataConfigList, device);
        device.setId(dataConfigList.getDeviceId());
        deviceMapper.updateById(device);
    }

    /**
     * 数采配置
     * @param dataConfigList
     */
    @Override
    public void saveDataAcquisitionConfiguration(DataConfigDto dataConfigList) {
        if (dataConfigList.getDeviceId() == null) {
            throw new RuntimeException("缺少设备id");
        }

        for (DataConfig dataConfig : dataConfigList.getDataConfigList()) {
            if (dataConfig.getStructureItemParameterId() == null) {
                throw new RuntimeException("缺少检验项id");
            }
            dataConfig.setDeviceId(dataConfigList.getDeviceId());
        }

        this.saveOrUpdateBatch(dataConfigList.getDataConfigList());

    }

    @Override
    public Result<?> queryDataAcquisitionConfiguration(DataConfig dataConfig) {
        return Result.success(dataConfigMapper.selectDataConfigList(dataConfig.getDeviceId()));

    }

    /**
     * 查询检验项数采配置
     * @param dataConfig
     * @return
     */
    @Override
    public Result<?> queryProductConfiguration(DataConfig dataConfig) {
        if (dataConfig.getStructureItemParameterId() == null) {
            throw new RuntimeException("缺少检测项id");
        }
        List<DataConfig> list = baseMapper.selectList(Wrappers.<DataConfig>lambdaQuery().eq(DataConfig::getStructureItemParameterId, dataConfig.getStructureItemParameterId()));
        return Result.success(list);
    }

    /**
     * 查询了绑定但是没有配置的检验项
     * @param deviceId
     * @return
     */
    @Override
    public IPage<DataConfig> getNoConfigProduct(Page page, Integer deviceId) {
        return baseMapper.getNoConfigProduct(page, deviceId);
    }
}

package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DataConfigDto;
import com.ruoyi.device.pojo.DataConfig;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-13 12:23:00
 */
public interface DataConfigService extends IService<DataConfig> {


    /**
     * 维护设备文件配置
     * @param dataConfigList
     */
    void saveDeviceFileConfiguration(DataConfigDto dataConfigList);

    /**
     * 数采配置
     * @param dataConfigList
     */
    void saveDataAcquisitionConfiguration(DataConfigDto dataConfigList);

    /**
     * 查询数采配置
     * @param dataConfig
     * @return
     */
    Result<?> queryDataAcquisitionConfiguration(DataConfig dataConfig);

    /**
     * 查询检验项数采配置
     * @param dataConfig
     * @return
     */
    Result<?> queryProductConfiguration(DataConfig dataConfig);

    /**
     * 查询了绑定但是没有配置的检验项
     * @param deviceId
     * @return
     */
    IPage<DataConfig> getNoConfigProduct(Page page, Integer deviceId);
}

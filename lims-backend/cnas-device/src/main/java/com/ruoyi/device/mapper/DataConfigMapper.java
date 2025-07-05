package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceConfigDtoPage;
import com.ruoyi.device.pojo.DataConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-07-13 12:23:00
 */
public interface DataConfigMapper extends BaseMapper<DataConfig> {

    List<DeviceConfigDtoPage> selectDataConfigList(Integer deviceId);

    List<Integer> deleteDataConfig();

    /**
     * 查询了绑定但是没有配置的检验项
     * @param deviceId
     * @return
     */
    IPage<DataConfig> getNoConfigProduct(@Param("page") Page page, @Param("deviceId") Integer deviceId);
}

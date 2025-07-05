package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.dto.DeviceScrappedDto;
import com.ruoyi.device.pojo.DeviceScrapped;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备报废申请表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 01:53:47
 */
public interface DeviceScrappedMapper extends BaseMapper<DeviceScrapped> {

    /**
     * 设备报废申请列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceScrapped> pageDeviceScrapped(Page page, @Param("ew") QueryWrapper<DeviceScrapped> ew);

    /**
     * 根据id查询设备报废申请
     * @param scrappedId
     * @return
     */
    DeviceScrappedDto selectDeviceScrappedById(@Param("scrappedId") Integer scrappedId);
}

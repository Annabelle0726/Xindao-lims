package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.pojo.DeviceExternalApply;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 利用外部设备申请表 Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-12-17 10:28:43
 */
public interface DeviceExternalApplyMapper extends BaseMapper<DeviceExternalApply> {

    /**
     * 利用外部设备申请列表
     * @param page
     * @param ew
     * @return
     */
    IPage<DeviceExternalApply> pageDeviceExternalApply(Page page, @Param("ew") QueryWrapper<DeviceExternalApply> ew);

    /**
     * 导出查询利用外部设备申请
     * @param externalApplyId 外部设备申请表id
     * @return
     */
    DeviceExternalApply selectDeviceExternalById(@Param("externalApplyId") Integer externalApplyId);
}

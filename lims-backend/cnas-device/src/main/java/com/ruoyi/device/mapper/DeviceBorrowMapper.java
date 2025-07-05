package com.ruoyi.device.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.device.pojo.DeviceBorrow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-09-21 10:53:51
 */
public interface DeviceBorrowMapper extends BaseMapper<DeviceBorrow> {

    IPage<DeviceBorrow> deviceBorrowPage(Page page, @Param("ew")QueryWrapper<DeviceBorrow> ew);

    List<DeviceBorrow> getDeviceBorrowBydeviceId(Integer deviceId);
}

package com.ruoyi.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.device.dto.DeviceImpowerDto;
import com.ruoyi.device.pojo.DeviceImpower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.device.pojo.DeviceImpower;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备使用授权表 服务类
 * </p>
 *
 * @author
 * @since 2025-04-17 03:23:23
 */
public interface DeviceImpowerService extends IService<DeviceImpower> {

    /**
     * 分页查询使用授权
     * @param page 分页参数
     * @param itemParameter 使用授权
     * @return
     */
    Result<IPage<DeviceImpower>> selectDeviceImpowerByPage(@Param("page") Page page, @Param("itemParameter") DeviceImpowerDto itemParameter);

    /**
     * 新增使用授权
     * @param deviceImpowerDto 使用授权
     */
    Result addImpower(DeviceImpowerDto deviceImpowerDto);

    /**
     * 修改使用授权
     * @param deviceImpowerDto 使用授权
     */
    Result updateImpower(DeviceImpowerDto deviceImpowerDto);

    /**
     * 删除使用授权
     * @param deviceImpowerDto 使用授权
     */
    Result deleteImpower(DeviceImpowerDto deviceImpowerDto);

    /**
     * 查询使用授权详情
     */
    Result<DeviceImpowerDto> getImpowerDetail(Integer impowerId);

    /**
     * 提交批准
     * @param deviceImpowerDto
     * @return
     */
    Result submitReviewImpowerStatus(DeviceImpowerDto deviceImpowerDto);

    /**
     * 使用授权审核
     */
    Result reviewImpowerStatus(DeviceImpowerDto deviceImpowerDto);

    /**
     * 使用授权导出
     */
    Result exportDeviceImpowerDto(Integer impowerId, HttpServletResponse response);
}

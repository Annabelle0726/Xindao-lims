package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHours;

import java.util.Map;

/**
 * <p>
 * 辅助工时 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-09 06:58:31
 */
public interface AuxiliaryWorkingHoursService extends IService<AuxiliaryWorkingHours> {
    IPage<AuxiliaryWorkingHours> selectAuxiliaryWorkingHours(Page page, AuxiliaryWorkingHours auxiliaryWorkingHours);

    int deleteAuxiliaryWorkingHours(Integer id);

    int upAuxiliaryWorkingHours(AuxiliaryWorkingHours auxiliaryWorkingHours);

    int insertAuxiliaryWorkingHours(AuxiliaryWorkingHours auxiliaryWorkingHours);
}

package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.dto.AuxiliaryCorrectionHoursDto;
import com.ruoyi.performance.pojo.AuxiliaryCorrectionHours;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工时统计的修正工时 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-29 02:38:19
 */
public interface AuxiliaryCorrectionHoursService extends IService<AuxiliaryCorrectionHours> {

    IPage<AuxiliaryCorrectionHoursDto> selectAuxiliaryCorrectionHours(Page page, AuxiliaryCorrectionHoursDto auxiliaryCorrectionHoursDto);

    /**
     * 导入上传
     * @param list
     */
    void importExcel(List<AuxiliaryCorrectionHoursDto> list);
}

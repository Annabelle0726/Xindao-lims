package com.ruoyi.performance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.performance.dto.AuxiliaryWorkingHoursDayDto;
import com.ruoyi.performance.dto.HoursDay;
import com.ruoyi.performance.pojo.AuxiliaryWorkingHoursDay;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 日工时管理的辅助工时 服务类
 * </p>
 *
 * @author 江苏鵷雏网络科技有限公司
 * @since 2024-05-28 02:22:19
 */
public interface AuxiliaryWorkingHoursDayService extends IService<AuxiliaryWorkingHoursDay> {

    IPage<AuxiliaryWorkingHoursDayDto> selectAuxiliaryWorkingHoursDay(Page page, AuxiliaryWorkingHoursDayDto auxiliaryWorkingHoursDayDto);

    int insertAuxiliaryWorkingHoursDay(AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay);

    AuxiliaryWorkingHoursDay selectAuxiliaryWorkingHoursByNumber(String number);

    boolean checkOrApprove(HoursDay hoursDay);

    int updateAuxiliaryWorkingHoursDay(AuxiliaryWorkingHoursDay auxiliaryWorkingHoursDay);

    int deleteAuxiliaryWorkingHoursDay(Integer id);

    String selectshiftByUser(LocalDateTime dateTime);

    /**
     * 导出辅助工时
     * @param entity
     * @param response
     */
    void exportWorkingHours(AuxiliaryWorkingHoursDayDto entity, HttpServletResponse response);
}

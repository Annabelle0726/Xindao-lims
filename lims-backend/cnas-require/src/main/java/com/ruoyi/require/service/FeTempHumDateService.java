package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.FeTempHumDateDto;
import com.ruoyi.require.pojo.FeTempHumDate;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度 区域 -父 服务类
 * </p>
 *
 * @author
 * @since 2024-11-09 11:02:18
 */
public interface FeTempHumDateService extends IService<FeTempHumDate> {

    IPage<FeTempHumDateDto> getFeTempHumDate(Page page, FeTempHumDateDto feTempHumDateDto);

    void exportTemperatureAndHumidityRecords(Integer dateId, HttpServletResponse response);

    /**
     * 温湿度确认
     * @param feTempHumDate
     */
    void affirmFeTempHumDate(FeTempHumDate feTempHumDate);
}

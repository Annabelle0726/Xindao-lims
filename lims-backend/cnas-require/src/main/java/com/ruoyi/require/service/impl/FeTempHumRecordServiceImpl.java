package com.ruoyi.require.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.require.dto.FeTempHumRecordDto;
import com.ruoyi.require.mapper.FeTempHumRecordMapper;
import com.ruoyi.require.pojo.FeTempHumRecord;
import com.ruoyi.require.service.FeTempHumRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度记录 服务实现类
 * </p>
 *
 * @author
 * @since 2024-11-07 04:28:52
 */
@Service
public class FeTempHumRecordServiceImpl extends ServiceImpl<FeTempHumRecordMapper, FeTempHumRecord> implements FeTempHumRecordService {

    @Override
    public IPage<FeTempHumRecordDto> getFeTempHumRecordPage(Page page, Integer dateId) {
        IPage<FeTempHumRecordDto> feTempHumRecordPage = baseMapper.getFeTempHumRecordPage(page, dateId);
        for (FeTempHumRecordDto record : feTempHumRecordPage.getRecords()) {
            // 判断是否是问题数据
            // 判断温湿度是否超过区间
            boolean flag = false;
            if (StringUtils.isNotBlank(record.getMorningTemp())) {
                Double value = Double.valueOf(record.getMorningTemp());
                if (value > record.getTemperatureHighest() || value < record.getTemperatureLowest()) {
                    flag = true;
                }
            }
            if (StringUtils.isNotBlank(record.getMorningHum())) {
                Double value = Double.valueOf(record.getMorningHum());
                if (value > record.getHumidityHighest() || value < record.getHumidityLowest()) {
                    flag = true;
                }
            }
            if (StringUtils.isNotBlank(record.getAfternoonTemp())) {
                Double value = Double.valueOf(record.getAfternoonTemp());
                if (value > record.getTemperatureHighest() || value < record.getTemperatureLowest()) {
                    flag = true;
                }
            }
            if (StringUtils.isNotBlank(record.getAfternoonHum())) {
                Double value = Double.valueOf(record.getAfternoonHum());
                if (value > record.getHumidityHighest() || value < record.getHumidityLowest()) {
                    flag = true;
                }
            }

            // 判断是否是恒温二, 是恒温二上下温度是否超过1摄氏度
            if (StringUtils.isNotBlank(record.getTestAreaName()) && record.getTestAreaName().equals("恒温二")) {
                if (StringUtils.isNotBlank(record.getMorningTemp()) && StringUtils.isNotBlank(record.getAfternoonTemp())) {
                    if (Double.valueOf(record.getAfternoonTemp()) - Double.valueOf(record.getMorningTemp()) > 1) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                record.setIsIssue(1);
            } else {
                record.setIsIssue(0);
            }


        }
        return feTempHumRecordPage;
    }
}

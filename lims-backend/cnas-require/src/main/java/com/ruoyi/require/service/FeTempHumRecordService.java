package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.FeTempHumRecordDto;
import com.ruoyi.require.pojo.FeTempHumRecord;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度记录 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:28:52
 */
public interface FeTempHumRecordService extends IService<FeTempHumRecord> {

    IPage<FeTempHumRecordDto> getFeTempHumRecordPage(Page page, Integer dateId);
}

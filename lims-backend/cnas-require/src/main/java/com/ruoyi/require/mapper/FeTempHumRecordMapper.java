package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.dto.FeTempHumRecordDto;
import com.ruoyi.require.pojo.FeTempHumRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 设施和环境条件-设施和环境条件要求-温湿度记录 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-07 04:28:52
 */
public interface FeTempHumRecordMapper extends BaseMapper<FeTempHumRecord> {

    IPage<FeTempHumRecordDto> getFeTempHumRecordPage(Page page, @Param("dateId") Integer dateId);

    /**
     * 查询有日期但是未填写名字的
     * @return
     */
    List<FeTempHumRecordDto> selectNoaffirm(LocalDate date);
}

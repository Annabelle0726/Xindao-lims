package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.process.pojo.QualitySuperviseDetailsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 质量监督详情记录表
 *
 * @author makejava
 * @since 2024-11-07
 */
@Mapper
public interface QualitySuperviseDetailsRecordMapper extends BaseMapper<QualitySuperviseDetailsRecord> {

    /**
     * 导出监督记录表
     * @param superviseDetailsId
     * @return
     */
    QualitySuperviseDetailsRecord selectSuperviseDetailRecord(@Param("superviseDetailsId") Integer superviseDetailsId);
}


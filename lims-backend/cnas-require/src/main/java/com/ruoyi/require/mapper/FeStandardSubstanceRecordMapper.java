package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.pojo.FeStandardSubstanceRecord;
import com.ruoyi.require.vo.SubstanceRecordVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标准物质清单借用归还记录表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-14 01:49:11
 */
public interface FeStandardSubstanceRecordMapper extends BaseMapper<FeStandardSubstanceRecord> {


    IPage<SubstanceRecordVo> getPage(Page page,@Param("ew") SubstanceRecordVo vo);
}

package com.ruoyi.require.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptance;
import com.ruoyi.require.vo.AcceptanceVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标准物质验收 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-14 03:29:41
 */
public interface FeStandardSubstanceAcceptanceMapper extends BaseMapper<FeStandardSubstanceAcceptance> {


    IPage<AcceptanceVo> getPageAcceptance(Page page, @Param("name")String name);
}

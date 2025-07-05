package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.InconsistentDistributionDto;
import com.ruoyi.process.pojo.InconsistentDistribution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 不符合项的分布 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-15 09:53:20
 */
@Mapper
public interface InconsistentDistributionMapper extends BaseMapper<InconsistentDistribution> {

    /**
     * 不符合项分布查询
     * @param page
     * @param ew
     * @return
     */
    IPage<InconsistentDistributionDto> pageInconsistentDistribution(Page page, @Param("ew") QueryWrapper<InconsistentDistribution> ew);
}

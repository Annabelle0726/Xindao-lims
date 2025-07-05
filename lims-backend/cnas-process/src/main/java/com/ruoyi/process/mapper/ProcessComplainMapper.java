package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.ProcessComplainDto;
import com.ruoyi.process.pojo.ProcessComplain;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 投诉 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-02 09:29:11
 */
public interface ProcessComplainMapper extends BaseMapper<ProcessComplain> {

    IPage<ProcessComplain> pageProcessComplain(@Param("page") Page page, @Param("ew") QueryWrapper<ProcessComplain> queryWrappers);

    ProcessComplainDto getProcessComplain(Long id);

}

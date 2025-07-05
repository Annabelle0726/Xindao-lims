package com.ruoyi.process.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.process.dto.ProcessMethodVerifyDto;
import com.ruoyi.process.pojo.ProcessMethodVerify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 标准方法验证
 *
 * @author zhuo
 * @since 2024-11-05
 */
@Mapper
public interface ProcessMethodVerifyMapper extends BaseMapper<ProcessMethodVerify> {

    /**
     * 标准方法变更, 验证
     * @param page
     * @param processMethodVerifyDtoQueryWrapper
     * @return
     */
    IPage<ProcessMethodVerify> pagesMethodVerify(Page page, @Param("ew") QueryWrapper<ProcessMethodVerifyDto> processMethodVerifyDtoQueryWrapper);
}


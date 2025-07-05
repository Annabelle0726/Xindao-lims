package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ClientSatisfactionAnalyseFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户满意度分析附件
 *
 * @author makejava
 * @since 2024-11-09
 */
@Mapper
public interface ClientSatisfactionAnalyseFileMapper extends BaseMapper<ClientSatisfactionAnalyseFile> {

    /**
     * 客户分析附件列表
     * @param page
     * @return
     */
    IPage<ClientSatisfactionAnalyseFile> pageAnalyseFile(Page page, @Param("ew") QueryWrapper<ClientSatisfactionAnalyseFile> ew);
}


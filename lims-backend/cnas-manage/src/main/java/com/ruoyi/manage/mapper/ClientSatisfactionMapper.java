package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.dto.ClientSatisfactionDto;
import com.ruoyi.manage.pojo.ClientSatisfaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户满意度
 *
 * @author zhuo
 * @since 2024-11-09
 */
@Mapper
public interface ClientSatisfactionMapper extends BaseMapper<ClientSatisfaction> {

    /**
     * 客户满意度调查列表
     * @param page
     * @param ew
     * @return
     */
    IPage<ClientSatisfaction> pageClientSatisfaction(Page page, @Param("ew") QueryWrapper<ClientSatisfaction> ew);

    /**
     * 客户满意度导出
     * @param clientSatisfactionId
     * @return
     */
    ClientSatisfactionDto exportWordClientSatisfaction(Integer clientSatisfactionId);
}


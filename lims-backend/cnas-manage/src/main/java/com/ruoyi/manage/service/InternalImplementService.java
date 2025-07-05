package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.dto.InternalImplementDto;
import com.ruoyi.manage.pojo.InternalImplement;

import javax.servlet.http.HttpServletResponse;

/**
 * 内审实施计划
 *
 * @author zhuo
 * @since 2024-11-11
 */
public interface InternalImplementService extends IService<InternalImplement> {

    /**
     * 内审实施计划分页查询
     * @param page
     * @param internalImplement
     * @return
     */
    IPage<InternalImplementDto> pageInternalImplement(Page page, InternalImplement internalImplement);

    /**
     * 内审实施计划新增
     * @param internalImplement
     * @return
     */
    boolean addInternalImplement(InternalImplementDto internalImplement);

    /**
     * 内审实施计划修改
     * @param internalImplement
     * @return
     */
    boolean updateInternalImplement(InternalImplementDto internalImplement);

    /**
     * 内审实施计划删除
     * @param implementId
     * @return
     */
    boolean delInternalImplement(Integer implementId);

    /**
     * 内审实施计划查看详情
     * @param implementId
     * @return
     */
    InternalImplementDto getInternalImplementOne(Integer implementId);

    /**
     * 内审实施计划批准
     * @param internalImplement
     * @return
     */
    boolean ratifyInternalImplement(InternalImplementDto internalImplement);

    /**
     * 导出内审实施计划
     * @param implementId
     * @param response
     */
    void exportInternalImplement(Integer implementId, HttpServletResponse response);
}


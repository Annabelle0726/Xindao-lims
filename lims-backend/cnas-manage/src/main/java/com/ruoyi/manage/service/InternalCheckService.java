package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.dto.InternalCheckDto;
import com.ruoyi.manage.pojo.InternalCheck;

import javax.servlet.http.HttpServletResponse;

/**
 * 内审检查表
 *
 * @author zhuo
 * @since 2024-11-11
 */
public interface InternalCheckService extends IService<InternalCheck> {

    /**
     * 内审检查分页查询
     * @param page
     * @param internalCheck
     * @return
     */
    IPage<InternalCheckDto> pageInternalCheck(Page page, InternalCheck internalCheck);

    /**
     * 内审检查新增
     * @param internalCheck
     * @return
     */
    boolean addInternalCheck(InternalCheckDto internalCheck);

    /**
     * 内审检查修改
     * @param internalCheck
     * @return
     */
    boolean updateInternalCheck(InternalCheckDto internalCheck);

    /**
     * 内审检查删除
     * @param checkId
     * @return
     */
    boolean delInternalCheck(Integer checkId);

    /**
     * 内审检查查看详情
     * @param checkId
     * @return
     */
    InternalCheckDto getInternalCheckOne(Integer checkId);

    /**
     * 内审检查批准
     * @param internalCheck
     * @return
     */
    boolean ratifyInternalCheck(InternalCheckDto internalCheck);

    /**
     * 导出内审检查
     * @param checkId
     * @param response
     */
    void exportInternalCheck(Integer checkId, HttpServletResponse response);
}


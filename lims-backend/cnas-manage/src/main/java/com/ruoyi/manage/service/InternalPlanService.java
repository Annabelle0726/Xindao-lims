package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.dto.InternalPlanDto;
import com.ruoyi.manage.pojo.InternalPlan;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 内审年度计划 服务类
 * </p>
 *
 * @author
 * @since 2024-11-13 03:27:47
 */
public interface InternalPlanService extends IService<InternalPlan> {

    /**
     * 内审年度计划分页查询
     * @param page
     * @param internalPlan
     * @return
     */
    IPage<InternalPlanDto> pageInternalPlan(Page page, InternalPlan internalPlan);

    /**
     * 内审年度计划新增
     * @param internalPlan
     * @return
     */
    boolean addInternalPlan(InternalPlanDto internalPlan);

    /**
     * 内审年度计划修改
     * @param internalPlan
     * @return
     */
    boolean updateInternalPlan(InternalPlanDto internalPlan);

    /**
     * 内审年度计划删除
     * @param planId
     * @return
     */
    boolean delInternalPlan(Integer planId);

    /**
     * 内审年度计划查看详情
     * @param planId
     * @return
     */
    InternalPlanDto getInternalPlanOne(Integer planId);

    /**
     * 内审年度计划审核
     * @param internalPlanDto
     * @return
     */
    boolean examineInternalPlan(InternalPlanDto internalPlanDto);

    /**
     * 内审年度计划批准
     * @param internalPlanDto
     * @return
     */
    boolean ratifyInternalPlan(InternalPlanDto internalPlanDto);

    /**
     * 导出内审年度计划
     * @param planId
     * @param response
     */
    void exportInternalImplement(Integer planId, HttpServletResponse response);
}

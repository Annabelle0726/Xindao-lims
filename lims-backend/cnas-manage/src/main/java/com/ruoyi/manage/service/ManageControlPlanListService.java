package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageControlPlanList;
import com.ruoyi.manage.vo.ManageControlPlanListVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 重大风险因素分析及控制计划清单 服务类
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:30
 */
public interface ManageControlPlanListService extends IService<ManageControlPlanList> {

    IPage<ManageControlPlanListVo> getPageList(Page page);

    void exportPersonTraining(HttpServletResponse response);
}

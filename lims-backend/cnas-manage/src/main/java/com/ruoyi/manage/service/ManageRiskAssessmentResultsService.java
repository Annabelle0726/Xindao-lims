package com.ruoyi.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.manage.pojo.ManageRiskAssessmentResults;
import com.ruoyi.manage.vo.ManageRiskAssessmentResultsVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 危险因素辨识与风险评价结果一览表 服务类
 * </p>
 *
 * @author 
 * @since 2024-11-15 02:58:51
 */
public interface ManageRiskAssessmentResultsService extends IService<ManageRiskAssessmentResults> {

    IPage<ManageRiskAssessmentResultsVo> getPageResults(Page page);

    void exportPersonTraining(HttpServletResponse response);
}

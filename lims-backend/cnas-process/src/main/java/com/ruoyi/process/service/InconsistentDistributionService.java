package com.ruoyi.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.process.dto.InconsistentDistributionDto;
import com.ruoyi.process.pojo.InconsistentDistribution;


import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 不符合项的分布 服务类
 * </p>
 *
 * @author
 * @since 2024-11-15 09:53:20
 */
public interface InconsistentDistributionService extends IService<InconsistentDistribution> {

    /**
     * 不符合项的分布分页查询
     * @param page
     * @param inconsistentDistribution
     * @return
     */
    IPage<InconsistentDistributionDto> pageInconsistentDistribution(Page page, InconsistentDistribution inconsistentDistribution);

    /**
     * 不符合项的分布新增
     * @param inconsistentDistribution
     * @return
     */
    boolean addInconsistentDistribution(InconsistentDistributionDto inconsistentDistribution);

    /**
     * 不符合项的分布修改
     * @param inconsistentDistribution
     * @return
     */
    boolean updateInconsistentDistribution(InconsistentDistributionDto inconsistentDistribution);

    /**
     * 不符合项的分布删除
     * @param distributionId
     * @return
     */
    boolean delInconsistentDistribution(Integer distributionId);

    /**
     * 不符合项的分布查看详情
     * @param distributionId
     * @return
     */
    InconsistentDistributionDto getInconsistentDistributionOne(Integer distributionId);

    /**
     * 导出不符合项的分布
     * @param distributionId
     * @param response
     */
    void exportInconsistentDistribution(Integer distributionId, HttpServletResponse response);
}

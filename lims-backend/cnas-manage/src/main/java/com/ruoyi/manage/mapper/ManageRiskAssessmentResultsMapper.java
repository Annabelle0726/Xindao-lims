package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageRiskAssessmentResults;
import com.ruoyi.manage.vo.ManageRiskAssessmentResultsVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 危险因素辨识与风险评价结果一览表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2024-11-15 02:58:51
 */
public interface ManageRiskAssessmentResultsMapper extends BaseMapper<ManageRiskAssessmentResults> {

    IPage<ManageRiskAssessmentResultsVo> getPageResults(Page page, @Param("itSExporting") Boolean itSExporting);
}

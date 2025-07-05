package com.ruoyi.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.manage.pojo.ManageControlPlanList;
import com.ruoyi.manage.vo.ManageControlPlanListVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 重大风险因素分析及控制计划清单 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-11-15 02:58:30
 */
public interface ManageControlPlanListMapper extends BaseMapper<ManageControlPlanList> {

    IPage<ManageControlPlanListVo> getPageList(Page page, @Param("itSExporting") Boolean itSExporting);
}

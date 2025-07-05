package com.ruoyi.require.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.require.dto.AcceptanceDto;
import com.ruoyi.require.pojo.FeStandardSubstanceAcceptance;
import com.ruoyi.require.vo.AcceptanceDetailsVo;
import com.ruoyi.require.vo.AcceptanceVo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 标准物质验收 服务类
 * </p>
 *
 * @author
 * @since 2024-11-14 03:29:41
 */
public interface FeStandardSubstanceAcceptanceService extends IService<FeStandardSubstanceAcceptance> {


    void addAcceptance(AcceptanceDto dto);

    IPage<AcceptanceVo> getPageAcceptance(Page page, String name);

    AcceptanceDetailsVo getAcceptanceDetails(Integer id);

    Integer deleteAcceptance(Integer id);

    void exportFeStandardSubstanceAcceptance(HttpServletResponse response);
}
